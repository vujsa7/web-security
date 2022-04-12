package com.security.certificate.controller;

import com.security.certificate.dto.CertificateDto;
import com.security.certificate.dto.UserCertificateDto;
import com.security.certificate.dto.ValidCertificateDto;
import com.security.certificate.model.Certificate;
import com.security.certificate.model.CertificateTemplateType;
import com.security.certificate.service.CertificateGeneratorService;
import com.security.certificate.service.CertificateService;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;
import com.security.data.service.DataService;
import com.security.user.service.RoleService;
import com.security.user.service.UserService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class CertificateController {
    private final CertificateGeneratorService certificateGeneratorService;
    private final CertificateService certificateService;
    private final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;


    @Autowired
    public CertificateController(CertificateGeneratorService certificateGeneratorService, CertificateService certificateService,
                                 UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, DataService dataService){
        this.certificateGeneratorService = certificateGeneratorService;
        this.certificateService = certificateService;
        this.userService = userService;
        this.roleService = roleService;
        this.dataService = dataService;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = new SecureRandom();
    }

    @GetMapping(value = "/certificates")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserCertificateDto> getAllCertificates(){
        List<UserCertificateDto> userCertificates = new ArrayList<>();
        List<Certificate> certificates = certificateService.getAllCertificates();
        for (Certificate cert : certificates){
            Certificate issuerCert = certificateService.getCertificateByAlias(cert.getIssuerAlias());
            String issuerName = issuerCert.getCommonName();
            userCertificates.add(new UserCertificateDto(cert.getSerialNumber(), cert.getCommonName(), issuerName, cert.getValidFrom(), cert.getValidTo(), cert.getRevoked()));
        }
        return userCertificates;
    }

    @GetMapping(value = "/validCertificates/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ValidCertificateDto> getValidCertificates(@PathVariable() String username){
        return certificateService.getValidCertificatesByUsersEmail(username).stream()
                .map(cert -> new ValidCertificateDto(cert.getSerialNumber(), cert.getCommonName(), cert.getValidFrom(), cert.getValidTo()))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/rootCertificates")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> issueRootCertificate(@RequestBody CertificateDto certificateDto){
        // TODO: Validate parameters coming from the request (for example isCa must be true)
        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        IssuerData issuerData = dataService.generateIssuerData(certificateDto, keyPair.getPrivate());
        SubjectData subjectData = dataService.generateSubjectData(certificateDto, keyPair.getPublic());

        X509Certificate certificate = certificateGeneratorService.generate(subjectData, issuerData, certificateDto.getKeyUsage(),
                certificateDto.getExtendedKeyUsage(), null);
        certificateService.saveRootCertificate(certificate, keyPair.getPrivate(), certificateDto.getEmail());

        return new ResponseEntity<>("Root certificate successfully created.", HttpStatus.CREATED);
    }

    @PostMapping(value = "/certificates")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> createCertificate(Principal principal, @RequestBody CertificateDto certificateDto,
                                                    @RequestParam(required = false) CertificateTemplateType certificateTemplateType){
        Certificate issuerCertificate = certificateService.getValidCertificateBySerialNumber(certificateDto.getSignWith());
        if(issuerCertificate == null)
            return new ResponseEntity<>("Sorry, certificate used for signing is not valid anymore or it does not exist in our records.", HttpStatus.BAD_REQUEST);
        if(!issuerCertificate.getUser().getEmail().equals(principal.getName()) || issuerCertificate.getCertificateType().equals("ee"))
            return new ResponseEntity<>("Unable to sign with the requested certificate.", HttpStatus.FORBIDDEN);
        if(!issuerCertificate.isInValidDateRange(certificateDto.getValidFrom(), certificateDto.getValidTo())){
            return new ResponseEntity<>("Unsuccessful. Invalid dates used for signing, please check the date ranges and try again.", HttpStatus.BAD_REQUEST);
        }

        // TODO: Create a method for validating certificate chain (check if any of certificates is revoked in the chain)
        if(!certificateService.isCertificateChainValid(issuerCertificate))
            return new ResponseEntity<>("Unable to issue new certificate because of invalid certificate chain. Either a " +
                    "certificate in the chain was revoked or isn't valid anymore.", HttpStatus.BAD_REQUEST);

        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        SubjectData subjectData = dataService.generateSubjectData(certificateDto, keyPair.getPublic());
        IssuerData issuerData = certificateService.getIssuerDataFromKeyStore(issuerCertificate.getAlias(), issuerCertificate.getCertificateType());

        X509Certificate certificate = certificateGeneratorService.generate(subjectData, issuerData, certificateDto.getKeyUsage(),
                certificateDto.getExtendedKeyUsage(), null);

        certificateService.saveCertificate(certificate, keyPair.getPrivate(), certificateDto.getEmail(), certificateDto.getCa() == true ? "ca" : "ee", issuerCertificate);

        return new ResponseEntity<>(principal.getName(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/revoke/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> revokeCertificate(@PathVariable() String serialNumber){
        certificateService.revokeCertificate(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
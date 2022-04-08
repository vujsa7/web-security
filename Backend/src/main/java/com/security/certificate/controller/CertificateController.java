package com.security.certificate.controller;

import com.security.certificate.dto.CertificateDto;
import com.security.certificate.dto.ValidCertificateDto;
import com.security.certificate.model.Certificate;
import com.security.certificate.model.CertificateType;
import com.security.certificate.service.CertificateGeneratorService;
import com.security.certificate.service.CertificateService;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;
import com.security.data.service.DataService;
import com.security.user.model.User;
import com.security.user.service.RoleService;
import com.security.user.service.UserService;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
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

    @GetMapping(value = "/validCertificates/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ValidCertificateDto> getValidCertificates(@PathVariable() String username){
        return certificateService.getValidCertificatesByUsersEmail(username).stream()
                .map(cert -> new ValidCertificateDto(cert.getSerialNumber(), cert.getValidFrom(), cert.getValidTo()))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/rootCertificates")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> createRootCertificate(@RequestBody CertificateDto certificateDto){

        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        IssuerData issuerData = dataService.generateIssuerData(certificateDto, keyPair.getPrivate());
        SubjectData subjectData = dataService.generateSubjectData(certificateDto, keyPair.getPublic());

        X509Certificate certificate = certificateGeneratorService.generate(subjectData, issuerData, certificateDto.getKeyUsage(),
                certificateDto.getExtendedKeyUsage(), null);
        certificateService.saveCertificate(certificate, keyPair.getPrivate(), certificateDto.getEmail());

        return new ResponseEntity<>("Root certificate successfully created.", HttpStatus.CREATED);
    }

    @PostMapping(value = "/certificates")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> createCertificate(@RequestBody CertificateDto certificateDto, @RequestParam(required = false) CertificateType certificateType){
        // TODO: get issuer data by serial num of cert which is going to be used for signing (from keystore) and validate user
//        IssuerData issuerData = generateIssuerData();
//
//        // construct subject data
//        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
//        builder.addRDN(BCStyle.CN, certificateDto.getCommonName());
//        builder.addRDN(BCStyle.O, "AllSafe");
//        builder.addRDN(BCStyle.C, certificateDto.getCountry());
//        builder.addRDN(BCStyle.ST, certificateDto.getState());
//        builder.addRDN(BCStyle.L, certificateDto.getLocal());
//        SubjectData subjectData = new SubjectData(builder.build(), new BigInteger(8 * 40, secureRandom).toString(),
//                certificateDto.getValidFrom(), certificateDto.getValidTo(), certificateDto.getCa());
//
//        // generate cert
//        certificateGeneratorService.generate(subjectData, issuerData, certificateDto.getKeyUsage(), certificateDto.getExtendedKeyUsage(), certificateType);
//
//        // create acc || find existing acc => user
//        // TODO: replace "getAliasHere" with subjects alias
//        User user = userService.findByEmail(certificateDto.getEmail());
//        if(user == null){
//            user = new User(certificateDto.getEmail(), passwordEncoder.encode("allsafe"), roleService.findByName("ROLE_USER"));
//            Certificate certificate = new Certificate(subjectData.getSerialNumber(), "getAliasHere", subjectData.getStartDate(), subjectData.getEndDate(), user);
//            userService.save(user, certificate);
//
//            return new ResponseEntity<>("Certificate generated", HttpStatus.CREATED);
//        }
//
//        // save (serialNum, alias, validFrom, validTo, user) to db
//        Certificate certificate = new Certificate(subjectData.getSerialNumber(), "getAliasHere", subjectData.getStartDate(), subjectData.getEndDate(), user);
//        certificateService.save(certificate);

        return new ResponseEntity<>("Certificate generated", HttpStatus.CREATED);
    }



}
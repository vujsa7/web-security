package com.security.certificate.controller;

import com.security.certificate.dto.*;
import com.security.certificate.model.Certificate;
import com.security.certificate.model.CertificateTemplateType;
import com.security.certificate.service.CertificateGeneratorService;
import com.security.certificate.service.CertificateService;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;
import com.security.data.service.DataService;
import com.security.user.model.User;
import com.security.user.service.RoleService;
import com.security.user.service.UserService;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.security.*;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
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

    @GetMapping(value = "/certificates/{serialNumber}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> getCertificateFile(@PathVariable() String serialNumber){
        File certFile = certificateService.getCertificateFile(serialNumber);
        FileSystemResource resource = new FileSystemResource(certFile);

        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ContentDisposition disposition = ContentDisposition.attachment().filename(resource.getFilename()).build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/certificates/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<UserCertificateDto> getCertificates(Principal principal){
        return certificateService.getCertificatesByUsersEmail(principal.getName()).stream()
                .map(cert -> {
                    Certificate issuerCert = certificateService.getCertificateByAlias(cert.getIssuerAlias());
                    String issuerName = issuerCert.getCommonName();
                    return new UserCertificateDto(cert.getSerialNumber(), cert.getCommonName(), issuerName, cert.getValidFrom(), cert.getValidTo(), cert.getRevoked());
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/issuingCertificates/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ValidCertificateDto> getIssuingCertificates(@PathVariable() String username){
        return certificateService.getIssuingCertificatesByUsersEmail(username).stream()
                .map(cert -> new ValidCertificateDto(cert.getSerialNumber(), cert.getCommonName(), cert.getValidFrom(), cert.getValidTo()))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/rootCertificates")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> issueRootCertificate(@RequestBody CertificateDto certificateDto,
                                                       @RequestParam(required = false) CertificateTemplateType certificateTemplateType){
        if(certificateTemplateType != null){
            if(certificateTemplateType != CertificateTemplateType.rootCa){
                return new ResponseEntity<>("Invalid template type.", HttpStatus.BAD_REQUEST);
            }
        }

        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        IssuerData issuerData = dataService.generateIssuerData(certificateDto, keyPair.getPrivate());
        SubjectData subjectData = dataService.generateSubjectData(certificateDto, keyPair.getPublic());

        X509Certificate certificate = certificateGeneratorService.generate(subjectData, issuerData, certificateDto.getKeyUsage(),
                certificateDto.getExtendedKeyUsage(), certificateDto.getCommonName(), certificateTemplateType);
        certificateService.saveRootCertificate(certificate, keyPair.getPrivate(), certificateDto.getEmail());

        return new ResponseEntity<>("Root certificate successfully created.", HttpStatus.CREATED);
    }

    @PostMapping(value = "/certificates")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> createCertificate(Principal principal, @RequestBody CertificateDto certificateDto,
                                                    @RequestParam(required = false) CertificateTemplateType certificateTemplateType){
        if(certificateTemplateType != null){
            if(certificateTemplateType == CertificateTemplateType.rootCa){
                return new ResponseEntity<>("Invalid template type.", HttpStatus.BAD_REQUEST);
            }
        }

        Certificate issuerCertificate = certificateService.getValidCertificateBySerialNumber(certificateDto.getSignWith());
        if(issuerCertificate == null)
            return new ResponseEntity<>("Sorry, certificate used for signing is not valid anymore or it does not exist in our records.", HttpStatus.BAD_REQUEST);
        if(!issuerCertificate.getUser().getEmail().equals(principal.getName()) || issuerCertificate.getCertificateType().equals("ee"))
            return new ResponseEntity<>("Unable to sign with the requested certificate.", HttpStatus.FORBIDDEN);
        if(!issuerCertificate.isInValidDateRange(certificateDto.getValidFrom(), certificateDto.getValidTo())){
            return new ResponseEntity<>("Unsuccessful. Invalid dates used for signing, please check the date ranges and try again.", HttpStatus.BAD_REQUEST);
        }

        if(!certificateService.isCertificateChainValid(issuerCertificate))
            return new ResponseEntity<>("Unable to issue new certificate because of invalid certificate chain. Either a " +
                    "certificate in the chain was revoked or isn't valid anymore.", HttpStatus.BAD_REQUEST);

        KeyPair keyPair = certificateGeneratorService.generateKeyPair();
        SubjectData subjectData = dataService.generateSubjectData(certificateDto, keyPair.getPublic());
        IssuerData issuerData = certificateService.getIssuerDataFromKeyStore(issuerCertificate.getAlias(), issuerCertificate.getCertificateType());

        X509Certificate certificate = certificateGeneratorService.generate(subjectData, issuerData, certificateDto.getKeyUsage(),
                certificateDto.getExtendedKeyUsage(), certificateDto.getCommonName(), certificateTemplateType);

        certificateService.saveCertificate(certificate, keyPair.getPrivate(), certificateDto.getEmail(), certificateDto.getCa() ? "ca" : "ee", issuerCertificate);

        return new ResponseEntity<>(principal.getName(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/revoke/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> revokeCertificate(@PathVariable() String serialNumber){
        certificateService.revokeCertificate(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/revocationStatus/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserCertificateDto> checkCertificateStatus(@PathVariable() String serialNumber){
        Certificate cert = certificateService.getCertificateBySerialNumber(serialNumber);
        Certificate issuerCert = certificateService.getCertificateByAlias(cert.getIssuerAlias());
        String issuerName = issuerCert.getCommonName();
        UserCertificateDto userCertificate = new UserCertificateDto(cert.getSerialNumber(), cert.getCommonName(), issuerName, cert.getValidFrom(), cert.getValidTo(), cert.getRevoked());
        return new ResponseEntity<>(userCertificate, HttpStatus.OK);
    }

    @GetMapping(value = "/certificateFull/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CertificateFullDto> getCertificateFull(@PathVariable() String serialNumber, Principal principal){
        X509Certificate certX509 = certificateService.getX509Certificate(serialNumber);
        Certificate cert = certificateService.getCertificateBySerialNumber(serialNumber);
        Certificate certIssuer = certificateService.getCertificateByAlias(cert.getIssuerAlias());
        try {
            CertificateFullDto certFull = new CertificateFullDto(
                    "v" + String.valueOf(certX509.getVersion()), certX509.getSerialNumber().toString(), certX509.getSigAlgName(),
                    getNameInfo(certX509.getIssuerX500Principal(), "CN"), getNameInfo(certX509.getIssuerX500Principal(), "O"),
                    getNameInfo(certX509.getIssuerX500Principal(), "C"), getNameInfo(certX509.getIssuerX500Principal(), "S"),
                    getNameInfo(certX509.getIssuerX500Principal(), "L"), getNameInfo(certX509.getSubjectX500Principal(), "CN"),
                    getNameInfo(certX509.getSubjectX500Principal(), "O"), getNameInfo(certX509.getSubjectX500Principal(), "C"),
                    getNameInfo(certX509.getSubjectX500Principal(), "S"), getNameInfo(certX509.getSubjectX500Principal(), "L"),
                    certX509.getNotBefore(), certX509.getNotAfter(), cert.getCertificateType().equals("ee") ? "End Enitity" : "Certificate Authority",
                    getKeyUsages(certX509.getKeyUsage()), certX509.getExtendedKeyUsage().toArray(new String[0]), publicKeyToString(certX509.getPublicKey()), certIssuer.getSerialNumber());
            return new ResponseEntity<CertificateFullDto>(certFull, HttpStatus.OK);
        } catch (CertificateParsingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private KeyUsageDto[] getKeyUsages(boolean[] keyUsages) {
        List<KeyUsageDto> keyUsagesList = new ArrayList<KeyUsageDto>();
        for(int i = 0; i < keyUsages.length; i++){
            if(i == 0 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("digitalSignature", 128));
            else if(i == 1 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("nonRepudiation", 64));
            else if(i == 2 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("keyEncipherment", 32));
            else if(i == 3 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("dataEncipherment", 16));
            else if(i == 4 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("keyAgreement", 8));
            else if(i == 5 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("keyCertSign", 4));
            else if(i == 6 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("cRLSign", 2));
            else if(i == 7 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("encipherOnly", 1));
            else if(i == 8 && keyUsages[i])
                keyUsagesList.add(new KeyUsageDto("decipherOnly", 32768));
        }
        KeyUsageDto[] keyUsagesArray = new KeyUsageDto[keyUsagesList.size()];
        return keyUsagesList.toArray(keyUsagesArray);
    }

    private String getNameInfo(X500Principal principal, String requestedNameInfo){
        X500Name x500name = new X500Name( principal.getName() );
        RDN cn = null;
        if(requestedNameInfo.equals("CN"))
            cn = x500name.getRDNs(BCStyle.CN)[0];
        if(requestedNameInfo.equals("O"))
            cn = x500name.getRDNs(BCStyle.O)[0];
        if(requestedNameInfo.equals("L"))
            cn = x500name.getRDNs(BCStyle.L)[0];
        if(requestedNameInfo.equals("S"))
            cn = x500name.getRDNs(BCStyle.ST)[0];
        if(requestedNameInfo.equals("C"))
            cn = x500name.getRDNs(BCStyle.C)[0];
        if(cn == null)
            return "";
        return IETFUtils.valueToString(cn.getFirst().getValue());
    }

    private String publicKeyToString(PublicKey publicKey){
        byte[] bytePublicKey = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytePublicKey);
    }


}
package com.security.certificate.service.impl;

import com.security.certificate.dao.CertificateRepository;
import com.security.certificate.dto.CertificateDto;
import com.security.certificate.model.Certificate;
import com.security.certificate.service.CertificateService;
import com.security.data.model.IssuerData;
import com.security.keystore.KeyStoreInfo;
import com.security.keystore.KeyStoreReader;
import com.security.keystore.KeyStoreWriter;
import com.security.user.model.User;
import com.security.user.service.UserService;
import com.security.util.ArrayUtils;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.oer.its.CertificateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final UserService userService;
    private final SecureRandom secureRandom;
    private final KeyStoreReader keyStoreReader;
    private final KeyStoreWriter keyStoreWriter;
    private final KeyStoreInfo keyStoreInfo;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, UserService userService, KeyStoreInfo keyStoreInfo) {
        this.certificateRepository = certificateRepository;
        this.userService = userService;
        this.secureRandom = new SecureRandom();
        this.keyStoreReader = new KeyStoreReader();
        this.keyStoreWriter = new KeyStoreWriter();
        this.keyStoreInfo = keyStoreInfo;
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    @Override
    public List<Certificate> getValidCertificatesByUsersEmail(String email) {
        return certificateRepository.findValidCertificates(email);
        // TODO: Validate signature + check if revoked
    }

    @Override
    public List<Certificate> getIssuingCertificatesByUsersEmail(String email) {
        return certificateRepository.findIssuingCertificates(email);
    }

    @Override
    public X509Certificate saveRootCertificate(X509Certificate certificate, PrivateKey privateKey, String email) {

        String keyStoreFile = keyStoreInfo.getKeyStoreFileLocation("root");
        String keyStorePass = keyStoreInfo.getKeyStorePass("root");

        X509Certificate[] certificateChain = new X509Certificate[1];
        certificateChain[0] = certificate;
        String alias = generateUniqueAlias(keyStoreFile, keyStorePass);

        writeCertificateChainToKeyStore(keyStoreFile, keyStorePass, privateKey, certificateChain, alias);

        User user = userService.fetchOrCreateDefaultUser(email);
        Certificate cert = new Certificate(certificate.getSerialNumber().toString(), getCommonNameFromCertificate(certificate), "root",
                alias, alias, certificate.getNotBefore(), certificate.getNotAfter(), user);
        userService.save(user, cert);
        return certificate;
    }

    @Override
    public Certificate getCertificateBySerialNumber(String serialNumber) {
        return certificateRepository.findOneBySerialNumber(serialNumber);
    }

    @Override
    public Certificate getCertificateByAlias(String alias) {
        return certificateRepository.findOneByAlias(alias);
    }

    @Override
    public Certificate getValidCertificateBySerialNumber(String serialNumber) {
        return certificateRepository.findValidCertificateBySerialNumber(serialNumber);
    }

    @Override
    public boolean isCertificateChainValid(Certificate certificate) {
        return true;
    }

    @Transactional
    @Override
    public void revokeCertificate(String serialNumber) {
        Certificate certificate = certificateRepository.findOneBySerialNumber(serialNumber);
        certificate.setRevoked(true);
        certificateRepository.save(certificate);
        if(isCertificateCA(certificate.getAlias(), certificate.getCertificateType())){
            List<Certificate> issuedCertificates = certificateRepository.findAllByIssuerAlias(certificate.getAlias());
            for(Certificate cert : issuedCertificates){
                if(!certificate.equals(cert)) {
                    revokeCertificate(cert.getSerialNumber());
                }
            }
        }
    }

    public boolean isCertificateCA(String alias, String certificateType) {
        String keyStoreFile = keyStoreInfo.getKeyStoreFileLocation(certificateType);
        String keyStorePass = keyStoreInfo.getKeyStorePass(certificateType);
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        X509Certificate cert = keyStoreReader.readX509Certificate(keyStoreFile, keyStorePass, alias);
        if(cert.getBasicConstraints() != -1) {
            return true;
        }
        return false;
    }

    @Override
    public IssuerData getIssuerDataFromKeyStore(String alias, String certificateType) {
        String keyStoreFile = keyStoreInfo.getKeyStoreFileLocation(certificateType);
        String keyStorePass = keyStoreInfo.getKeyStorePass(certificateType);
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        return keyStoreReader.readIssuerFromStore(keyStoreFile, alias, keyStorePass.toCharArray(), keyStorePass.toCharArray());
    }

    @Override
    public X509Certificate saveCertificate(X509Certificate certificate, PrivateKey privateKey, String email, String certificateType, Certificate issuerCertificate) {
        // Getting subject keystore loading information
        String keyStoreFile = keyStoreInfo.getKeyStoreFileLocation(certificateType);
        String keyStorePass = keyStoreInfo.getKeyStorePass(certificateType);

        X509Certificate[] aboveCertificateChain = extractCertificateChain(issuerCertificate);
        X509Certificate[] subjectCertificate = { certificate };

        // Merging subject certificate and above certificate chain into one chain
        X509Certificate[] certificateChain = ArrayUtils.concatWithCollection(subjectCertificate, aboveCertificateChain);

        String alias = generateUniqueAlias(keyStoreFile, keyStorePass);
        User user = userService.fetchOrCreateDefaultUser(email);
        Certificate cert = new Certificate(certificate.getSerialNumber().toString(), getCommonNameFromCertificate(certificate), certificateType,
                alias, issuerCertificate.getAlias(), certificate.getNotBefore(), certificate.getNotAfter(), user);
        userService.save(user, cert);

        writeCertificateChainToKeyStore(keyStoreFile, keyStorePass, privateKey, certificateChain, alias);

        return certificate;
    }

    private void writeCertificateChainToKeyStore( String keyStoreFile, String keyStorePass, PrivateKey privateKey, X509Certificate[] certificateChain, String alias) {
        keyStoreWriter.loadKeyStore(keyStoreFile, keyStorePass.toCharArray());
        keyStoreWriter.write(alias, privateKey, keyStorePass.toCharArray(), certificateChain);
        keyStoreWriter.saveKeyStore(keyStoreFile, keyStorePass.toCharArray());
    }

    private X509Certificate[] extractCertificateChain(Certificate cert) {
        String keyStoreFile = keyStoreInfo.getKeyStoreFileLocation(cert.getCertificateType());
        String keyStorePass = keyStoreInfo.getKeyStorePass(cert.getCertificateType());

        X509Certificate[] certificateChain = keyStoreReader.readCertificateChain(keyStoreFile, keyStorePass, cert.getAlias());
        return certificateChain;
    }

    // TODO: Generate unique alias from db (not from keystore)
    private String generateUniqueAlias(String keyStoreFile, String keyStorePass) {
        String alias = "";
        do {
            alias = new BigInteger(8 * 40, secureRandom).toString();
        } while(keyStoreReader.containsAlias(keyStoreFile, keyStorePass, alias));
        return alias;
    }

    private String getCommonNameFromCertificate(X509Certificate certificate) {
        X500Principal principal = certificate.getSubjectX500Principal();
        X500Name x500name = new X500Name( principal.getName() );
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        return IETFUtils.valueToString(cn.getFirst().getValue());
    }

    public List<CertificateDto> getCertificates(){
        KeyStoreReader reader=new KeyStoreReader();
        List<CertificateDto> certificateDtos=new ArrayList<CertificateDto>();
        List<X509Certificate> rootCertificates=keyStoreReader.getCertificatesInKeyStore(keyStoreInfo.getKeyStoreFileLocation("root"),keyStoreInfo.getKeyStorePass("root"));
        List<X509Certificate> caCertificates=keyStoreReader.getCertificatesInKeyStore(keyStoreInfo.getKeyStoreFileLocation("ca"),keyStoreInfo.getKeyStorePass("ca"));
        List<X509Certificate> endCertificates=keyStoreReader.getCertificatesInKeyStore(keyStoreInfo.getKeyStoreFileLocation("end"),keyStoreInfo.getKeyStorePass("end"));
        for(X509Certificate rootCertificate:rootCertificates){
            certificateDtos.add(ConvertX509ToCertificateDto(rootCertificate));
        }
        for(X509Certificate caCertificate:caCertificates){
            certificateDtos.add(ConvertX509ToCertificateDto(caCertificate));
        }
        for(X509Certificate endCertificate:endCertificates){
            certificateDtos.add(ConvertX509ToCertificateDto(endCertificate));
        }
        return certificateDtos;

    }

    private CertificateDto ConvertX509ToCertificateDto(X509Certificate certificate){
        //TODO do conversion
        return null;

    }


}

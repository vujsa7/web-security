package com.security.certificate.service.impl;

import com.security.certificate.dao.CertificateRepository;
import com.security.certificate.model.Certificate;
import com.security.certificate.service.CertificateService;
import com.security.data.model.IssuerData;
import com.security.keystore.KeyStoreReader;
import com.security.keystore.KeyStoreWriter;
import com.security.user.model.User;
import com.security.user.service.UserService;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;

    private final UserService userService;
    private final SecureRandom secureRandom;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, UserService userService) {
        this.certificateRepository = certificateRepository;
        this.userService = userService;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public List<Certificate> getValidCertificatesByUsersEmail(String email) {
        return certificateRepository.findValidCertificates(email);
        // TODO: Validate signature + check if revoked
    }

    @Override
    public X509Certificate saveRootCertificate(X509Certificate certificate, PrivateKey privateKey, boolean ca, String email) {
        String rootKeyStoreLocation = "files/keystores/root.jks";
        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(rootKeyStoreLocation, "changeit".toCharArray());
        X509Certificate[] certificateChain = new X509Certificate[1];
        certificateChain[0] = certificate;
        String alias = "root-" + new BigInteger(8 * 40, secureRandom).toString();
        keyStoreWriter.write(alias, privateKey, "changeit".toCharArray(), certificateChain);
        keyStoreWriter.saveKeyStore(rootKeyStoreLocation, "changeit".toCharArray());

        User user = userService.fetchOrCreateDefaultUser(email);
        Certificate cert = new Certificate(certificate.getSerialNumber().toString(), getCommonNameFromCertificate(certificate), ca,
                alias, certificate.getNotBefore(), certificate.getNotAfter(), user);
        userService.save(user, cert);
        return certificate;
    }

    @Override
    public Certificate getValidCertificateBySerialNumber(String serialNumber) {
        return certificateRepository.findValidCertificateBySerialNumber(serialNumber);
    }

    @Override
    public boolean isCertificateChainValid(Certificate certificate) {
        return true;
    }

    @Override
    public IssuerData getIssuerData(String alias) {
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        return keyStoreReader.readIssuerFromStore("files/keystores/allsafeKeystore.jks", alias, "changeit".toCharArray(), "changeit".toCharArray());
    }

    private String getCommonNameFromCertificate(X509Certificate certificate) {
        X500Principal principal = certificate.getSubjectX500Principal();
        X500Name x500name = new X500Name( principal.getName() );
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        return IETFUtils.valueToString(cn.getFirst().getValue());
    }

}

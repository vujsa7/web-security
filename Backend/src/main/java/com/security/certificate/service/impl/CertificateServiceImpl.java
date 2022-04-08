package com.security.certificate.service.impl;

import com.security.certificate.dao.CertificateRepository;
import com.security.certificate.model.Certificate;
import com.security.certificate.service.CertificateService;
import com.security.keystore.KeyStoreWriter;
import com.security.user.model.User;
import com.security.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;

    private final UserService userService;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, UserService userService) {
        this.certificateRepository = certificateRepository;
        this.userService = userService;
    }

    @Override
    public List<Certificate> getValidCertificatesByUsersEmail(String email) {
        return certificateRepository.findValidCertificates(email);
        // TODO: Validate signature + check if revoked
    }

    @Override
    public void save(Certificate certificate) {
        certificateRepository.save(certificate);
    }

    @Override
    public void saveCertificate(X509Certificate certificate, PrivateKey privateKey, String email) {
        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null, "changeit".toCharArray());
        X509Certificate[] certificateChain = new X509Certificate[1];
        certificateChain[0] = certificate;
        String alias = "test";
        keyStoreWriter.write(alias, privateKey, "test".toCharArray(), certificateChain);
        keyStoreWriter.saveKeyStore("files/keystores/testKeyStore.jks", "changeit".toCharArray());

        User user = fetchOrCreateUser(email);
        Certificate cert = new Certificate(certificate.getSerialNumber().toString(), alias, certificate.getNotBefore(), certificate.getNotAfter(), user);
        userService.save(user, cert);
    }

    private User fetchOrCreateUser(String email) {
        User user = userService.findByEmail(email);
        if(user == null) {
            user = userService.generateDefaultUser(email);
        }
        return user;
    }
}

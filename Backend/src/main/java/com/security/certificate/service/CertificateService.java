package com.security.certificate.service;

import com.security.certificate.model.Certificate;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateService {
    List<Certificate> getValidCertificatesByUsersEmail(String email);
    void save(Certificate certificate);

    /** Method that saves a certificate to a keystore as well tie it to a user in the database,
     * if the user is not present, new user is created using the email provided and default password
     * Params: certificate to save to key store, privateKey, email for the user */
    void saveCertificate(X509Certificate certificate, PrivateKey privateKey, String email);
}

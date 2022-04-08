package com.security.certificate.service;

import com.security.certificate.model.Certificate;
import com.security.data.model.IssuerData;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateService {
    List<Certificate> getValidCertificatesByUsersEmail(String email);

    /** Method that saves a certificate and private key to a keystore and assign certificate to a user in the database,
     * if the user is not present, new user is created using the email provided and default password 'changeit'
     * Params: certificate to save to key store, privateKey, email for the user */
    X509Certificate saveRootCertificate(X509Certificate certificate, PrivateKey privateKey, boolean ca, String email);

    /** Method that fetches valid certificate from the database based on serial number */
    Certificate getValidCertificateBySerialNumber(String serialNumber);

    boolean isCertificateChainValid(Certificate certificate);

    IssuerData getIssuerData(String alias);
}

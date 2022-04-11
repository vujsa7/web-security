package com.security.certificate.service;

import com.security.certificate.model.Certificate;
import com.security.data.model.IssuerData;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateService {
    List<Certificate> getAllCertificates();

    List<Certificate> getValidCertificatesByUsersEmail(String email);

    /** Method that saves a root certificate, it's private key to a keystore and assigns root certificate to a user in the database,
     * if the user is not present, new user is created using the email provided and default password 'changeit'
     * Params: root certificate to save to key store, privateKey, email for the user */
    X509Certificate saveRootCertificate(X509Certificate certificate, PrivateKey privateKey, String email);

    /** Method that fetches certificate from the database based on serial number */
    Certificate getCertificateBySerialNumber(String serialNumber);

    Certificate getCertificateByAlias(String alias);

    /** Method that fetches valid certificate from the database based on serial number */
    Certificate getValidCertificateBySerialNumber(String serialNumber);

    /** Method that checks if the certificate chain is valid */
    boolean isCertificateChainValid(Certificate certificate);

    /** Method that extracts issuer data from keystore, including private key information */
    IssuerData getIssuerDataFromKeyStore(String alias, String certificateType);

    /** Method that saves a CA or EE certificate, it's private key to a keystore, entire chain and assigns certificate to a user in the database,
     * if the user is not present, new user is created using the email provided and default password 'changeit'
     * Params: certificate to save to key store, privateKey, email for the user, certificate type ("ca" or "ee"), issuerCertificate */
    X509Certificate saveCertificate(X509Certificate certificate, PrivateKey privateKey, String email, String certificateType, Certificate issuerCertificate);
}

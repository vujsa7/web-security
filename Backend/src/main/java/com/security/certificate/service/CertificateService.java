package com.security.certificate.service;

import com.security.certificate.model.Certificate;
import com.security.data.model.IssuerData;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateService {
    List<Certificate> getAllCertificates();

    List<Certificate> getValidCertificatesByUsersEmail(String email);

    List<Certificate> getIssuingCertificatesByUsersEmail(String email);

    X509Certificate saveRootCertificate(X509Certificate certificate, PrivateKey privateKey, String email);

    Certificate getCertificateBySerialNumber(String serialNumber);

    Certificate getCertificateByAlias(String alias);

    Certificate getValidCertificateBySerialNumber(String serialNumber);

    boolean isCertificateChainValid(Certificate certificate);

    void revokeCertificate(String serialNumber);

    IssuerData getIssuerDataFromKeyStore(String alias, String certificateType);

    X509Certificate saveCertificate(X509Certificate certificate, PrivateKey privateKey, String email, String certificateType, Certificate issuerCertificate);

    X509Certificate getX509Certificate(String serialNumber);
}

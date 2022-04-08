package com.security.certificate.service;

import com.security.certificate.model.CertificateType;
import com.security.certificate.model.KeyPurposeIdType;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public interface CertificateGeneratorService {
    X509Certificate generate(SubjectData subjectData, IssuerData issuerData, int[] keyUsage, KeyPurposeIdType[] extendedKeyUsage, CertificateType certificateType);
    KeyPair generateKeyPair();
}

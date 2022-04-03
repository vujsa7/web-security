package com.security.certificate.service;

import com.security.certificate.model.CertificateType;
import com.security.certificate.model.KeyPurposeIdType;
import com.security.data.IssuerData;
import com.security.data.SubjectData;

import java.security.cert.X509Certificate;

public interface CertificateGeneratorService {
    X509Certificate generate(SubjectData subjectData, IssuerData issuerData, int[] keyUsage, KeyPurposeIdType[] extendedKeyUsage, CertificateType certificateType);
}

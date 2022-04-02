package com.security.certificate.service;

import com.security.data.IssuerData;
import com.security.data.SubjectData;
import org.bouncycastle.asn1.x509.KeyPurposeId;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateGeneratorService {
    X509Certificate generate(SubjectData subjectData, IssuerData issuerData, int[] keyUsage, KeyPurposeId[] extendedKeyUsage);
}

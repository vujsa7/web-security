package com.security.certificate.service;

import com.security.data.IssuerData;
import com.security.data.SubjectData;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateGeneratorService {
    X509Certificate generate(SubjectData subjectData, IssuerData issuerData) throws CertificateException;
}

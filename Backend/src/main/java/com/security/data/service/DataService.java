package com.security.data.service;

import com.security.certificate.dto.CertificateDto;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface DataService {
    SubjectData generateSubjectData(CertificateDto certificateDto, PublicKey publicKey);
    IssuerData generateIssuerData(CertificateDto certificateDto, PrivateKey privateKey);
}

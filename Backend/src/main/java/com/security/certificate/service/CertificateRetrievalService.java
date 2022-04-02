package com.security.certificate.service;

import com.security.certificate.model.Certificate;

import java.util.List;

public interface CertificateRetrievalService {
    List<Certificate> getValidCertificatesByUsersEmail(String email);
}

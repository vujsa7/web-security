package com.security.certificate.service.impl;

import com.security.certificate.dao.CertificateRepository;
import com.security.certificate.model.Certificate;
import com.security.certificate.service.CertificateRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateRetrievalServiceImpl implements CertificateRetrievalService {
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateRetrievalServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public List<Certificate> getValidCertificatesByUsersEmail(String email) {
        return certificateRepository.findValidCertificates(email);
        // TODO: Validate signature + check if revoked
    }
}
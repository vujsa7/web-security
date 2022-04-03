package com.security.certificate.service.impl;

import com.security.certificate.dao.CertificateRepository;
import com.security.certificate.model.Certificate;
import com.security.certificate.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public void save(Certificate certificate) {
        certificateRepository.save(certificate);
    }
}

package com.security.certificate.controller;

import com.security.certificate.dto.ValidCertificateDto;
import com.security.certificate.service.CertificateRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class CertificateController {
    private final CertificateRetrievalService certificateRetrievalService;

    @Autowired
    public CertificateController(CertificateRetrievalService certificateRetrievalService){
        this.certificateRetrievalService = certificateRetrievalService;
    }

    @GetMapping(value = "/validCertificates/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ValidCertificateDto> getValidCertificates(@PathVariable() String username){
        return certificateRetrievalService.getValidCertificatesByUsersEmail(username).stream()
                .map(cert -> new ValidCertificateDto(cert.getSerialNumber(), cert.getValidFrom(), cert.getValidTo()))
                .collect(Collectors.toList());
    }
}
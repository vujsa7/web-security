package com.security.certificate.dto;

import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.util.Date;
import java.util.List;

public class CertificateDto {
    private String commonName;
    private String country;
    private String state;
    private String local;
    private String email;
    private Date notBefore;
    private Date notAfter;
    private Boolean ca;
    private int[] keyUsage;
    private KeyPurposeId[] enhancedKeyUsage;
}

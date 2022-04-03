package com.security.certificate.dto;

import com.security.certificate.model.KeyPurposeIdType;

import java.util.Date;

public class CertificateDto {
    private String signWith;
    private String commonName;
    private String country;
    private String state;
    private String local;
    private String email;
    private Date validFrom;
    private Date validTo;
    private Boolean ca;
    private int[] keyUsage;
    private KeyPurposeIdType[] extendedKeyUsage;

    public CertificateDto() {
    }

    public String getSignWith() {
        return signWith;
    }

    public void setSignWith(String signWith) {
        this.signWith = signWith;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Boolean getCa() {
        return ca;
    }

    public void setCa(Boolean ca) {
        this.ca = ca;
    }

    public int[] getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(int[] keyUsage) {
        this.keyUsage = keyUsage;
    }

    public KeyPurposeIdType[] getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(KeyPurposeIdType[] extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }
}

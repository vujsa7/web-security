package com.security.certificate.dto;

import java.math.BigInteger;
import java.util.Date;

public class CertificateFullDto {
    private String version;
    private String serialNumber;
    private String signatureAlgorithm;
    private String issuerCN;
    private String issuerO;
    private String issuerC;
    private String issuerS;
    private String issuerL;
    private String subjectCN;
    private String subjectO;
    private String subjectC;
    private String subjectS;
    private String subjectL;
    private Date validFrom;
    private Date validTo;
    private String subjectType;
    private KeyUsageDto[] keyUsages;
    private String[] extendedKeyUsages;
    private String publicKey;
    private String issuerSerialNumber;


    public CertificateFullDto(String version, String serialNumber, String signatureAlgorithm, String issuerCN, String issuerO,
                              String issuerC, String issuerS, String issuerL, String subjectCN, String subjectO, String subjectC, String subjectS, String subjectL,
                              Date validFrom, Date validTo, String subjectType, KeyUsageDto[] keyUsages, String[] extendedKeyUsages,
                              String publicKey, String issuerSerialNumber) {
        this.version = version;
        this.serialNumber = serialNumber;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuerCN = issuerCN;
        this.issuerO = issuerO;
        this.issuerC = issuerC;
        this.issuerS = issuerS;
        this.issuerL = issuerL;
        this.subjectCN = subjectCN;
        this.subjectO = subjectO;
        this.subjectC = subjectC;
        this.subjectS = subjectS;
        this.subjectL = subjectL;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.subjectType = subjectType;
        this.keyUsages = keyUsages;
        this.extendedKeyUsages = extendedKeyUsages;
        this.publicKey = publicKey;
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getIssuerCN() {
        return issuerCN;
    }

    public void setIssuerCN(String issuerCN) {
        this.issuerCN = issuerCN;
    }

    public String getIssuerO() {
        return issuerO;
    }

    public void setIssuerO(String issuerO) {
        this.issuerO = issuerO;
    }

    public String getIssuerC() {
        return issuerC;
    }

    public void setIssuerC(String issuerC) {
        this.issuerC = issuerC;
    }

    public String getIssuerS() {
        return issuerS;
    }

    public void setIssuerS(String issuerS) {
        this.issuerS = issuerS;
    }

    public String getIssuerL() {
        return issuerL;
    }

    public void setIssuerL(String issuerL) {
        this.issuerL = issuerL;
    }

    public String getSubjectCN() {
        return subjectCN;
    }

    public void setSubjectCN(String subjectCN) {
        this.subjectCN = subjectCN;
    }

    public String getSubjectO() {
        return subjectO;
    }

    public void setSubjectO(String subjectO) {
        this.subjectO = subjectO;
    }

    public String getSubjectC() {
        return subjectC;
    }

    public void setSubjectC(String subjectC) {
        this.subjectC = subjectC;
    }

    public String getSubjectS() {
        return subjectS;
    }

    public void setSubjectS(String subjectS) {
        this.subjectS = subjectS;
    }

    public String getSubjectL() {
        return subjectL;
    }

    public void setSubjectL(String subjectL) {
        this.subjectL = subjectL;
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

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public KeyUsageDto[] getKeyUsages() {
        return keyUsages;
    }

    public void setKeyUsages(KeyUsageDto[] keyUsage) {
        this.keyUsages = keyUsage;
    }

    public String[] getExtendedKeyUsages() {
        return extendedKeyUsages;
    }

    public void setExtendedKeyUsages(String[] extendedKeyUsages) {
        this.extendedKeyUsages = extendedKeyUsages;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIssuerSerialNumber() { return issuerSerialNumber; }

    public void setIssuerSerialNumber(String issuerSerialNumber) { this.issuerSerialNumber = issuerSerialNumber; }
}

package com.security.certificate.dto;

import java.util.Date;

public class UserCertificateDto {
    private String serialNumber;
    private String subjectName;
    private String issuerName;
    private Date validFrom;
    private Date validTo;

    public UserCertificateDto(String serialNumber, String subjectName, String issuerName, Date validFrom, Date validTo) {
        this.serialNumber = serialNumber;
        this.subjectName = subjectName;
        this.issuerName = issuerName;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
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
}

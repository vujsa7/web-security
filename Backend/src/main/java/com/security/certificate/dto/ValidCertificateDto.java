package com.security.certificate.dto;

import java.util.Date;

public class ValidCertificateDto {
    private String serialNumber;
    private Date validFrom;
    private Date validTo;

    public ValidCertificateDto(String serialNumber, Date validFrom, Date validTo) {
        this.serialNumber = serialNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
package com.security.data;

import org.bouncycastle.asn1.x500.X500Name;

import java.security.PublicKey;
import java.util.Date;

public class SubjectData {
    private PublicKey publicKey;
    private X500Name x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;
    private Boolean ca;

    public SubjectData() {
    }

    public SubjectData(X500Name x500name, String serialNumber, Date startDate, Date endDate, Boolean ca) {
        this.x500name = x500name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ca = ca;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Boolean getCa() {
        return ca;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCa(Boolean ca) {
        ca = ca;
    }
}

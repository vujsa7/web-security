package com.security.certificate.model;

import com.security.user.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false, unique = true)
    private String alias;

    @Column()
    private String issuerAlias;

    @Column(nullable = false)
    private String commonName;

    @Column(nullable = false)
    private String certificateType;

    @Column(nullable = false)
    private Date validFrom;

    @Column(nullable = false)
    private Date validTo;

    @Column(nullable = false)
    private Boolean isRevoked;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public Certificate() {
    }

    public Certificate(String serialNumber, String commonName, String certificateType, String alias, String issuerAlias, Date validFrom, Date validTo, User user) {
        this.serialNumber = serialNumber;
        this.commonName = commonName;
        this.certificateType = certificateType;
        this.alias = alias;
        this.issuerAlias = issuerAlias;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.user = user;
        this.isRevoked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIssuerAlias() {
        return issuerAlias;
    }

    public void setIssuesAlias(String issuerAlias) {
        this.issuerAlias = issuerAlias;
    }

    public String getCommonName() { return commonName; }

    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getCertificateType() { return certificateType; }

    public void setCertificateType(String certificateType) { this.certificateType = certificateType; }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getRevoked() {
        return isRevoked;
    }

    public void setRevoked(Boolean revoked) {
        isRevoked = revoked;
    }

    public boolean isInValidDateRange(Date validFrom, Date validTo){
        if(this.validFrom.compareTo(validFrom) < 0 && this.validTo.compareTo(validTo) > 0)
            return true;
        return false;
    }
}
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

    @Column(nullable = false)
    private String commonName;

    @Column(nullable = false)
    private boolean ca;

    @Column(nullable = false)
    private Date validFrom;

    @Column(nullable = false)
    private Date validTo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public Certificate() {
    }

    public Certificate(String serialNumber, String commonName, boolean ca, String alias, Date validFrom, Date validTo, User user) {
        this.serialNumber = serialNumber;
        this.commonName = commonName;
        this.ca = ca;
        this.alias = alias;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.user = user;
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

    public String getCommonName() { return commonName; }

    public void setCommonName(String commonName) { this.commonName = commonName; }

    public boolean isCa() { return ca; }

    public void setCa(boolean ca) { this.ca = ca; }

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

    public boolean isInValidDateRange(Date validFrom, Date validTo){
        if(this.validFrom.compareTo(validFrom) < 0 && this.validTo.compareTo(validTo) > 0)
            return true;
        return false;
    }
}
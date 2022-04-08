package com.security.data.model;

import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;

public class IssuerData {
    private X500Name x500name;
    private PrivateKey privateKey;

    public IssuerData() {
    }

    public IssuerData(X500Name x500name, PrivateKey privateKey) {
        this.x500name = x500name;
        this.privateKey = privateKey;
    }

    public X500Name getX500name() {
        return x500name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setX500name(X500Name x500name) {
        this.x500name = x500name;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}

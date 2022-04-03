package com.security.certificate.dto;

public class KeyUsageDto {
    private String keyUsageName;
    private int keyUsageValue;

    public KeyUsageDto() {
    }

    public KeyUsageDto(String keyUsageName, int keyUsageValue) {
        this.keyUsageName = keyUsageName;
        this.keyUsageValue = keyUsageValue;
    }

    public String getKeyUsageName() {
        return keyUsageName;
    }

    public void setKeyUsageName(String keyUsageName) {
        this.keyUsageName = keyUsageName;
    }

    public int getKeyUsageValue() {
        return keyUsageValue;
    }

    public void setKeyUsageValue(int keyUsageValue) {
        this.keyUsageValue = keyUsageValue;
    }
}

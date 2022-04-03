package com.security.certificate.controller;

import com.security.certificate.dto.KeyUsageDto;
import com.security.certificate.model.KeyPurposeIdType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class KeyUsageController {

    @GetMapping(value = "/keyUsage")
    public List<KeyUsageDto> getKeyUsage(){
        List<KeyUsageDto> keyUsage = new ArrayList<>();
        keyUsage.add(new KeyUsageDto("digitalSignature", 128));
        keyUsage.add(new KeyUsageDto("nonRepudiation", 64));
        keyUsage.add(new KeyUsageDto("keyEncipherment", 32));
        keyUsage.add(new KeyUsageDto("dataEncipherment", 16));
        keyUsage.add(new KeyUsageDto("keyAgreement", 8));
        keyUsage.add(new KeyUsageDto("keyCertSign", 4));
        keyUsage.add(new KeyUsageDto("cRLSign", 2));
        keyUsage.add(new KeyUsageDto("encipherOnly", 1));
        keyUsage.add(new KeyUsageDto("decipherOnly", 32768));

        return keyUsage;
    }

    @GetMapping(value = "/extendedKeyUsage")
    public List<KeyPurposeIdType> getExtendedKeyUsage(){
        List<KeyPurposeIdType> extendedKeyUsage = new ArrayList<>();
        extendedKeyUsage.add(KeyPurposeIdType.serverAuth);
        extendedKeyUsage.add(KeyPurposeIdType.clientAuth);

        return extendedKeyUsage;
    }
}

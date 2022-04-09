package com.security.certificate.factory.key.usage;

import com.security.certificate.model.CertificateTemplateType;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.springframework.stereotype.Component;

@Component
public class KeyUsageFactory {

    public KeyUsage createInstance(int[] keyUsage){
        var bitwiseKeyUsage = keyUsage[0];
        for(int i = 1; i < keyUsage.length; i++){
            bitwiseKeyUsage |= keyUsage[i];
        }

        return new KeyUsage(bitwiseKeyUsage);
    }

    public KeyUsage createInstance(CertificateTemplateType certificateTemplateType){
        switch (certificateTemplateType){
            case template1:
                return new KeyUsage(KeyUsage.keyCertSign | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment);
            case template2:
                return new KeyUsage(KeyUsage.keyCertSign | KeyUsage.keyEncipherment | KeyUsage.cRLSign);
            case template3:
                return new KeyUsage(KeyUsage.keyCertSign | KeyUsage.keyEncipherment | KeyUsage.decipherOnly);
            default:
                return new KeyUsage(KeyUsage.keyCertSign);
        }
    }
}

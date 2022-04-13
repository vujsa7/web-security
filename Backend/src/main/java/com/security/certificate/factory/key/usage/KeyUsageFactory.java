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
            case rootCa:
            case intermediateCa:
                return new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.keyEncipherment | KeyUsage.cRLSign);
            default:
                return new KeyUsage(KeyUsage.digitalSignature);
        }
    }
}

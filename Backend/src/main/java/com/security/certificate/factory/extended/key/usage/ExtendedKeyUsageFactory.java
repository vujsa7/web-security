package com.security.certificate.factory.extended.key.usage;

import com.security.certificate.model.CertificateTemplateType;
import com.security.certificate.model.KeyPurposeIdType;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.springframework.stereotype.Component;

@Component
public class ExtendedKeyUsageFactory {

    public ExtendedKeyUsage createInstance(KeyPurposeIdType[] extendedKeyUsage){
        if(extendedKeyUsage == null){
            return new ExtendedKeyUsage(new KeyPurposeId[] {});
        }
        if(extendedKeyUsage.length == 1){
            if(extendedKeyUsage[0].equals(KeyPurposeIdType.serverAuth)){
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth});
            } else if (extendedKeyUsage[0].equals(KeyPurposeIdType.clientAuth)){
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_clientAuth});
            }
        }
        if(extendedKeyUsage.length > 1){
            if((extendedKeyUsage[0].equals(KeyPurposeIdType.serverAuth) && extendedKeyUsage[1].equals(KeyPurposeIdType.clientAuth)) || (extendedKeyUsage[1].equals(KeyPurposeIdType.serverAuth) && extendedKeyUsage[0].equals(KeyPurposeIdType.clientAuth))){
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth});
            }
        }
        return new ExtendedKeyUsage(new KeyPurposeId[] {});
    }

    public ExtendedKeyUsage createInstance(CertificateTemplateType certificateTemplateType){
        switch (certificateTemplateType){
            case rootCa:
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth});
            case intermediateCa:
            case endEntity:
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth});
            default:
                return new ExtendedKeyUsage(new KeyPurposeId[] {});
        }
    }
}

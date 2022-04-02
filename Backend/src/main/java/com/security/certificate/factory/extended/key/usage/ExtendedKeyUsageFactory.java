package com.security.certificate.factory.extended.key.usage;

import com.security.certificate.model.CertificateType;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.springframework.stereotype.Component;

@Component
public class ExtendedKeyUsageFactory {

    public ExtendedKeyUsage createInstance(KeyPurposeId[] extendedKeyUsage){
        return new ExtendedKeyUsage(extendedKeyUsage);
    }

    public ExtendedKeyUsage createInstance(CertificateType certificateType){
        switch (certificateType){
            case template1:
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth});
            case template2:
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_clientAuth});
            case template3:
                return new ExtendedKeyUsage(new KeyPurposeId[] {KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth});
            default:
                return new ExtendedKeyUsage(new KeyPurposeId[] {});
        }
    }
}

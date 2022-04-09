package com.security.certificate.service.impl;

import com.security.certificate.factory.extended.key.usage.ExtendedKeyUsageFactory;
import com.security.certificate.factory.key.usage.KeyUsageFactory;
import com.security.certificate.model.CertificateTemplateType;
import com.security.certificate.model.KeyPurposeIdType;
import com.security.certificate.service.CertificateGeneratorService;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CertificateGeneratorServiceImpl implements CertificateGeneratorService {
    private final KeyUsageFactory keyUsageFactory;
    private final ExtendedKeyUsageFactory extendedKeyUsageFactory;

    @Autowired
    public CertificateGeneratorServiceImpl(KeyUsageFactory keyUsageFactory, ExtendedKeyUsageFactory extendedKeyUsageFactory){
        this.keyUsageFactory = keyUsageFactory;
        this.extendedKeyUsageFactory = extendedKeyUsageFactory;
    }

    @Override
    public X509Certificate generate(SubjectData subjectData, IssuerData issuerData, int[] keyUsage, KeyPurposeIdType[] extendedKeyUsage, CertificateTemplateType certificateTemplateType) {
        try {
            // Creating builder for signing the certificate
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            KeyPair subjectKeyPair = generateKeyPair();
            subjectData.setPublicKey(subjectKeyPair.getPublic());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey()
            );

            certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(subjectData.getCa()));
            certGen.addExtension(Extension.keyUsage, false,
                    certificateTemplateType == null ? keyUsageFactory.createInstance(keyUsage) : keyUsageFactory.createInstance(certificateTemplateType));
            certGen.addExtension(Extension.extendedKeyUsage, true,
                    certificateTemplateType == null ? extendedKeyUsageFactory.createInstance(extendedKeyUsage) : extendedKeyUsageFactory.createInstance(certificateTemplateType));

            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            X509Certificate cert = certConverter.getCertificate(certHolder);
            return cert;
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException | CertIOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


}

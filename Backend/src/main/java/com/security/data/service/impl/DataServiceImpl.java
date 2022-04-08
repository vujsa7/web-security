package com.security.data.service.impl;

import com.security.certificate.dto.CertificateDto;
import com.security.certificate.service.CertificateGeneratorService;
import com.security.data.model.IssuerData;
import com.security.data.model.SubjectData;
import com.security.data.service.DataService;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;

@Service
public class DataServiceImpl implements DataService {

    private final CertificateGeneratorService certificateGeneratorService;
    private final SecureRandom secureRandom;

    @Autowired
    public DataServiceImpl(CertificateGeneratorService certificateGeneratorService){
        this.certificateGeneratorService = certificateGeneratorService;
        this.secureRandom = new SecureRandom();
    }

    @Override
    /** Generating data for the subject of the certificate,
     * subject is an entity who is receiving a certificate
     * this is publicly available data for the certificate */
    public SubjectData generateSubjectData(CertificateDto certificateDto, PublicKey publicKey) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificateDto.getCommonName());
        builder.addRDN(BCStyle.O, "AllSafe");
        builder.addRDN(BCStyle.C, certificateDto.getCountry());
        builder.addRDN(BCStyle.ST, certificateDto.getState());
        builder.addRDN(BCStyle.L, certificateDto.getLocal());
        SubjectData subjectData = new SubjectData(publicKey, builder.build(), new BigInteger(8 * 40, secureRandom).toString(),
                certificateDto.getValidFrom(), certificateDto.getValidTo(), certificateDto.getCa());
        return subjectData;
    }

    @Override
    /** Generating data for the issuer of the certificate
     * Use this method for the generating Root CA issuer data
     * Generating data is private key and basic info for the issuer */
    public IssuerData generateIssuerData(CertificateDto certificateDto, PrivateKey privateKey) {
        //KeyPair k = certificateGeneratorService.generateKeyPair();
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificateDto.getCommonName());
        builder.addRDN(BCStyle.O, "AllSafe");
        builder.addRDN(BCStyle.OU, "Internal AllSafe Unit");
        builder.addRDN(BCStyle.C, certificateDto.getCountry());
        builder.addRDN(BCStyle.ST, certificateDto.getState());
        builder.addRDN(BCStyle.L, certificateDto.getLocal());
        builder.addRDN(BCStyle.E, "nikola.luburic@uns.ac.rs");
        return new IssuerData(builder.build(), privateKey);
    }

}

package com.security.keystore;

import com.security.data.model.IssuerData;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyStoreReader {
    private KeyStore keyStore;

    public KeyStoreReader(){
        try{
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, password);
            Certificate cert = keyStore.getCertificate(alias);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(issuerName, privateKey);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Reads last certificate in the certificate chain, last being the latest issued certificate */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                return ks.getCertificate(alias);
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Reads last certificate in the certificate chain, last being the latest issued certificate */
    public X509Certificate readX509Certificate(String keyStoreFile, String keyStorePass, String alias) {
        Certificate certificate = readCertificate(keyStoreFile, keyStorePass, alias);
        if(certificate == null){
            return null;
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(certificate.getEncoded());
            X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(bais);
            return x509Certificate;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public X509Certificate[] readCertificateChain(String keyStoreFile, String keyStorePass, String alias) {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                Certificate[] chain = ks.getCertificateChain(alias);
                List<X509Certificate> listX509 = new ArrayList<X509Certificate>();
                for(int i = 0; i < chain.length; i++){
                    Certificate certificate = chain[i];
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream bais = new ByteArrayInputStream(certificate.getEncoded());
                    X509Certificate x509Certificate = (X509Certificate) cf.generateCertificate(bais);
                    listX509.add(x509Certificate);
                }
                X509Certificate[] x509array = new X509Certificate[listX509.size()];
                return listX509.toArray(x509array);
            }
        } catch (KeyStoreException | NoSuchProviderException | CertificateException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                return (PrivateKey) ks.getKey(alias, pass.toCharArray());
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean containsAlias(String keyStoreFile, String keyStorePass, String alias) {
        try{
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
            return ks.containsAlias(alias);
        } catch (KeyStoreException | NoSuchProviderException | CertificateException | IOException | NoSuchAlgorithmException e) {
            System.out.println("No keystore found! We'll assume that alias doesn't exist.");
            return false;
        }
    }

    public List<X509Certificate> getCertificatesInKeyStore(String keyStoreFile,String keyStorePass){
        List<X509Certificate> certificates=new ArrayList<>();
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
            List<String> aliases= Collections.list(ks.aliases());
            for(String alias:aliases){
                certificates.add((X509Certificate)ks.getCertificate(alias));
            }

            return certificates;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}

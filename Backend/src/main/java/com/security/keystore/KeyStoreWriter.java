package com.security.keystore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class KeyStoreWriter {
    private KeyStore keyStore;

    public KeyStoreWriter(){
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password){
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password){
        try{
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void write(String alias, PrivateKey privateKey, char[] password, Certificate[] certificateChain){
        try{
            keyStore.setKeyEntry(alias, privateKey, password, certificateChain);
        } catch (KeyStoreException e){
            e.printStackTrace();
        }
    }
}

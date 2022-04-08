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
            // Try to load existing keystore file
            keyStore.load(new FileInputStream(fileName), password);
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
            try {
                // Try to create new keystore file if there is no existing
                keyStore.load(null, password);
            } catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
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

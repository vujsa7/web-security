package com.security.keystore;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "keystore.pass")
@Configuration("keyStoreInfo")
public class KeyStoreInfo {

    private String root;
    private String ca;
    private String ee;
    private String rootKeyStoreFile = "files/keystores/root.jks";
    private String caKeyStoreFile = "files/keystores/ca.jks";
    private String eeKeyStoreFile = "files/keystores/ee.jks";

    public String getRoot() {return root;}

    public void setRoot(String root) {this.root = root;}

    public String getCa() {return ca;}

    public void setCa(String ca) {this.ca = ca;}

    public String getEe() {return ee;}

    public void setEe(String ee) {this.ee = ee;}

    public String getRootKeyStoreFile() {return rootKeyStoreFile;}

    public void setRootKeyStoreFile(String rootKeyStoreFile) {this.rootKeyStoreFile = rootKeyStoreFile;}

    public String getCaKeyStoreFile() {return caKeyStoreFile;}

    public void setCaKeyStoreFile(String caKeyStoreFile) {this.caKeyStoreFile = caKeyStoreFile;}

    public String getEeKeyStoreFile() {return eeKeyStoreFile;}

    public void setEeKeyStoreFile(String eeKeyStoreFile) {this.eeKeyStoreFile = eeKeyStoreFile;}

    public String getKeyStoreFileLocation(String certificateType) {
        if(certificateType.equals("root")){
            return getRootKeyStoreFile();
        } else if(certificateType.equals("ca")){
            return getCaKeyStoreFile();
        } else if(certificateType.equals("ee")){
            return getEeKeyStoreFile();
        }
        return null;
    }

    public String getKeyStorePass(String certificateType) {
        if(certificateType.equals("root")){
            return getRoot();
        } else if(certificateType.equals("ca")){
            return getCa();
        } else if(certificateType.equals("ee")){
            return getEe();
        }
        return null;
    }

}

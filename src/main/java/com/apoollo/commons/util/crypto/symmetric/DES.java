/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

/**
 * @author liuyulong
 */
public class DES extends DefaultSymmetricEncryption {

    public DES(String transformation) {
        super("BC", "DES", transformation);
    }

    public DES() {
        this("DES/ECB/PKCS5Padding");
    }
}

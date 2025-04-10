/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

/**
 * @author liuyulong
 */
public class AES extends DefaultSymmetricEncryption {

    public AES(String transformation) {
        super("BC", "AES", transformation);
    }

    public AES() {
        this("AES/ECB/PKCS5Padding");
    }
}

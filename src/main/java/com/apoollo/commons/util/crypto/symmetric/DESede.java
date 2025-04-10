/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

/**
 * @author liuyulong
 */
public class DESede extends DefaultSymmetricEncryption {

    public DESede(String transformation) {
        super("BC", "DESede", transformation);
    }

    public DESede() {
        this("DESede/ECB/PKCS5Padding");
    }
}

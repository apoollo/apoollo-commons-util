/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

/**
 * @author liuyulong
 */
public class SM4 extends DefaultSymmetricEncryption {

    public SM4(String transformation) {
        super("BC", "SM4", transformation);
    }

    public SM4() {
        this("SM4/ECB/PKCS5Padding");
    }
}

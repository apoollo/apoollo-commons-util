/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

/**
 * @author liuyulong
 */
public class PBKDF2WithHmacSHA256 extends DefaultPBESymmetricEncryption {

    public PBKDF2WithHmacSHA256(SymmetricEncryption symmetricEncryption, int iterationCount, int keyLength) {
        super(symmetricEncryption, iterationCount, keyLength, "PBKDF2WithHmacSHA256");
    }

    public PBKDF2WithHmacSHA256(SymmetricEncryption symmetricEncryption, int keyLength) {
        this(symmetricEncryption, 1000, keyLength);
    }
}

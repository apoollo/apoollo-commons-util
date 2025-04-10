/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

/**
 * @author liuyulong
 */
public interface PBESymmetricEncryption {

    public byte[] encrypt(char[] password, byte[] salt, byte[] input);

    public default byte[] encrypt(String password, String salt, byte[] input) {
        return encrypt(password.toCharArray(), salt.getBytes(), input);
    }

    public byte[] decrypt(char[] password, byte[] salt, byte[] input);

    public default byte[] decrypt(String password, String salt, byte[] input) {
        return decrypt(password.toCharArray(), salt.getBytes(), input);
    }
}

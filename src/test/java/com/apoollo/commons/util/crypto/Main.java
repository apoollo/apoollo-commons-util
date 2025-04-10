/**
 * 
 */
package com.apoollo.commons.util.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.apoollo.commons.util.Assert;
import com.apoollo.commons.util.Base64Utils;
import com.apoollo.commons.util.crypto.symmetric.SM4;
import com.apoollo.commons.util.crypto.symmetric.SymmetricEncryption;

/**
 * @author liuyulong
 */
public class Main {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {

        // 16 * 8 = 128 位
        byte[] keyBytes = ("abc45678abE45678").getBytes(StandardCharsets.UTF_8);

        encryptAndDecrypt(new SM4(), keyBytes, "中文");
        // encryptAndDecrypt(new DESede(), keyBytes, "1213");
        // encryptAndDecrypt(new AES(), keyBytes, "1213");
        // encryptAndDecrypt(new DESCryptor(), keyBytes, "1213");
    }

    public static void encryptAndDecrypt(SymmetricEncryption symmetricEncryption, byte[] key, String input) {

        byte[] encrypt = symmetricEncryption.encrypt(key, input.getBytes());

        byte[] decrypt = symmetricEncryption.decrypt(key, encrypt);

        Assert.isTrue(new String(decrypt).equals(input), "crypto logic failed ");

        System.out.println("验证成功:" + Base64Utils.encodeToString(encrypt));
    }

}

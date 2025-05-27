/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.apoollo.commons.util.crypto.key.Key;
import com.apoollo.commons.util.crypto.key.SecretKeySpec;

/**
 * @author liuyulong
 */
public class DefaultSymmetricEncryption implements SymmetricEncryption {

	private String provider;
	private String algorithm;
	private String transformation;

	public DefaultSymmetricEncryption(String provider, String algorithm, String transformation) {
		super();
		this.provider = provider;
		this.algorithm = algorithm;
		this.transformation = transformation;
	}

	@Override
	public byte[] encrypt(Key key, AlgorithmParameterSpec params, byte[] input) {
		try {
			SecretKey secretKey = key.getSecretKey();
			Cipher cipher = Cipher.getInstance(transformation, provider);
			if (null != params) {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			}
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] decrypt(Key key, AlgorithmParameterSpec params, byte[] input) {
		try {
			SecretKey secretKey = key.getSecretKey();
			Cipher cipher = Cipher.getInstance(transformation, provider);
			if (null != params) {
				cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
			}
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] input) {
		return encrypt(new SecretKeySpec(key, algorithm), null, input);
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] input) {
		return decrypt(new SecretKeySpec(key, algorithm), null, input);
	}

}

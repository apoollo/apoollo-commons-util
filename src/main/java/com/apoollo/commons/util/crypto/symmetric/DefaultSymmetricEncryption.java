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
import javax.crypto.spec.SecretKeySpec;

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
	public byte[] encrypt(byte[] key, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(transformation, provider);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(transformation, provider);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] encrypt(byte[] key, AlgorithmParameterSpec params, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(transformation, provider);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] decrypt(byte[] key, AlgorithmParameterSpec params, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, algorithm);
			Cipher cipher = Cipher.getInstance(transformation, provider);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

}

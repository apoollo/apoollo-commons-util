/**
 * 
 */
package com.apoollo.commons.util.crypto.key;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class PebKey implements Key {

	private int iterationCount;
	private int keyLength;
	private String pbeAlgorithm;
	private char[] password;
	private byte[] salt;

	public PebKey(int iterationCount, int keyLength, String pbeAlgorithm, char[] password, byte[] salt) {
		super();
		this.iterationCount = iterationCount;
		this.keyLength = keyLength;
		this.pbeAlgorithm = pbeAlgorithm;
		this.password = password;
		this.salt = salt;
	}

	public PebKey(char[] password, byte[] salt) {
		this(1000, 256, "PBKDF2WithHmacSHA256", password, salt);
	}

	@Override
	public SecretKey getSecretKey() {
		try {
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(pbeAlgorithm);
			return secretKeyFactory.generateSecret(pbeKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

}

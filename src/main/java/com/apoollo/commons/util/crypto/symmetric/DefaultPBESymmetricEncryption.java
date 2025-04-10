/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author liuyulong
 */
public class DefaultPBESymmetricEncryption implements PBESymmetricEncryption {

	private SymmetricEncryption symmetricEncryption;
	private int iterationCount;
	private int keyLength;
	private String pbeAlgorithm;

	public DefaultPBESymmetricEncryption(SymmetricEncryption symmetricEncryption, int iterationCount, int keyLength,
			String pbeAlgorithm) {
		this.symmetricEncryption = symmetricEncryption;
		this.iterationCount = iterationCount;
		this.keyLength = keyLength;
		this.pbeAlgorithm = pbeAlgorithm;
	}

	public SecretKey getSecretKey(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(pbeAlgorithm);
		return secretKeyFactory.generateSecret(pbeKeySpec);
	}

	public byte[] getSecretBytes(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return getSecretKey(password, salt).getEncoded();
	}

	@Override
	public byte[] encrypt(char[] password, byte[] salt, byte[] input) {
		try {
			return symmetricEncryption.encrypt(getSecretBytes(password, salt), input);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] decrypt(char[] password, byte[] salt, byte[] input) {
		try {
			return symmetricEncryption.decrypt(getSecretBytes(password, salt), input);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

}

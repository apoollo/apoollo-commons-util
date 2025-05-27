/**
 * 
 */
package com.apoollo.commons.util.crypto.hash;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

import com.apoollo.commons.util.crypto.key.Key;
import com.apoollo.commons.util.crypto.key.SecretKeySpec;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class DefaultMacHash implements MacHash {

	private String provider;
	private String algorithm;

	public DefaultMacHash(String provider, String algorithm) {
		super();
		this.provider = provider;
		this.algorithm = algorithm;
	}

	public byte[] hash(Key key, AlgorithmParameterSpec params, byte[] input) {
		try {
			Mac hmac = Mac.getInstance(algorithm, provider);
			SecretKey secretKey = key.getSecretKey();
			if (null != params) {
				hmac.init(secretKey, params);
			} else {
				hmac.init(secretKey);
			}
			return hmac.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException
				| InvalidAlgorithmParameterException | IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] hash(byte[] key, AlgorithmParameterSpec params, byte[] input) {
		return hash(new SecretKeySpec(key, algorithm), params, input);
	}

}

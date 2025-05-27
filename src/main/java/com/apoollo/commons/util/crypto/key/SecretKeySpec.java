/**
 * 
 */
package com.apoollo.commons.util.crypto.key;

import javax.crypto.SecretKey;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class SecretKeySpec implements Key {

	private byte[] key;
	private String algorithm;

	public SecretKeySpec(byte[] key, String algorithm) {
		super();
		this.key = key;
		this.algorithm = algorithm;
	}

	@Override
	public SecretKey getSecretKey() {
		return new javax.crypto.spec.SecretKeySpec(key, algorithm);
	}

}

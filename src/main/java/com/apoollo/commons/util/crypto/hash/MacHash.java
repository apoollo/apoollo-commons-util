/**
 * 
 */
package com.apoollo.commons.util.crypto.hash;

import java.security.spec.AlgorithmParameterSpec;

import com.apoollo.commons.util.crypto.key.Key;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public interface MacHash {

	public byte[] hash(Key key, AlgorithmParameterSpec params, byte[] input);

	public byte[] hash(byte[] key, AlgorithmParameterSpec params, byte[] input);

	public default byte[] hash(byte[] key, byte[] input) {
		return hash(key, null, input);

	}
}

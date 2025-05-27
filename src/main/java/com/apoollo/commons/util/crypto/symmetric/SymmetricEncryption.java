/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

import java.security.spec.AlgorithmParameterSpec;

import com.apoollo.commons.util.crypto.key.Key;

/**
 * @author liuyulong
 */
public interface SymmetricEncryption {

	public byte[] encrypt(Key key, AlgorithmParameterSpec params, byte[] input);

	public byte[] decrypt(Key key, AlgorithmParameterSpec params, byte[] input);

	public byte[] encrypt(byte[] key, byte[] input);

	public byte[] decrypt(byte[] key, byte[] input);
}

/**
 * 
 */
package com.apoollo.commons.util.crypto.symmetric;

import java.security.spec.AlgorithmParameterSpec;

/**
 * @author liuyulong
 */
public interface SymmetricEncryption {

	public byte[] encrypt(byte[] key, byte[] input);

	public byte[] decrypt(byte[] key, byte[] input);

	public byte[] encrypt(byte[] key, AlgorithmParameterSpec params, byte[] input);

	public byte[] decrypt(byte[] key, AlgorithmParameterSpec params, byte[] input);
}

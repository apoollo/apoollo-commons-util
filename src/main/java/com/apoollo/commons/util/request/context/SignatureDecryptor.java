/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 * @since 2025-05-21
 */
public interface SignatureDecryptor {

	public byte[] decrypt(String signature, String secret);
}

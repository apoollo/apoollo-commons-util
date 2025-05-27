/**
 * 
 */
package com.apoollo.commons.util.crypto.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class Md5 implements Hash {

	@Override
	public byte[] hash(byte[] bytes) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("Md5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] digest = messageDigest.digest(bytes);
		return digest;
	}

}

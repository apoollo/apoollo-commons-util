/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.function.Function;

import org.bouncycastle.util.encoders.Hex;

/**
 * @author liuyulong
 * @since 2023年9月22日
 */
public class Md5Utils {

	public static <T> T digest(byte[] data, Function<byte[], T> map) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("Md5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] digest = messageDigest.digest(data);
		return map.apply(digest);
	}

	public static byte[] digest(byte[] data) {
		return digest(data, Function.identity());
	}

	public static String digestToHexString(byte[] data) {
		return digest(data, Hex::toHexString);
	}

	public static String digestToBase64String(byte[] data, Charset charset) {
		return digest(data, digest -> Base64Utils.encodeToString(digest, charset));
	}

	public static boolean compare(byte[] digest, byte[] data) {
		return Arrays.equals(digest, digest(data));
	}
}

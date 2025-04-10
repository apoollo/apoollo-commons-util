/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * @author liuyulong
 * @since 2023年9月22日
 */
public class Md5Utils {

	public static String digest(byte[] data) {
		String hash = null;
		if (null != data) {
			MD5Digest digest = new MD5Digest();
			digest.update(data, 0, data.length);
			byte[] hashValue = new byte[digest.getDigestSize()];
			digest.doFinal(hashValue, 0);
			hash = Hex.toHexString(hashValue);
		}
		return hash;
	}

	public static String digest(String message) {
		String hash = null;
		if (null != message) {
			byte[] data = message.getBytes(StandardCharsets.UTF_8);
			hash = digest(data);
		}
		return hash;
	}

	public static boolean compare(String digest, String message) {
		return digest.equals(digest(message));
	}

	public static boolean compare(String digest, byte[] data) {
		return digest.equals(digest(data));
	}

}

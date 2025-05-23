/**
 * 
 */
package com.apoollo.commons.util;

import java.util.Objects;
import java.util.stream.Stream;

import org.bouncycastle.util.encoders.Hex;

/**
 * @author liuyulong
 * @since 2025-04-09
 */
public class ByteArrayUtils {

	public static byte[] subbytesBefore(byte[] bytes, int filter, int direction) {

		if (-1 == direction) {
			// 从前向后查找
			int i = 0;
			for (; i < bytes.length; i++) {
				if (bytes[i] == filter) {
					break;
				}
			}
			if (i != bytes.length) {
				byte[] subbytesed = new byte[i];
				System.arraycopy(bytes, 0, subbytesed, 0, subbytesed.length);
				bytes = subbytesed;
			}
		} else if (1 == direction) {
			// 从后向前查找
			int i = bytes.length - 1;
			for (; i >= 0; i--) {
				if (bytes[i] == filter) {
					break;
				}
			}
			if (i > -1) {
				byte[] subbytesed = new byte[i];
				System.arraycopy(bytes, 0, subbytesed, 0, subbytesed.length);
				bytes = subbytesed;
			}
		}
		return bytes;
	}

	public static byte[] rstrip(byte[] bytes, int filter) {
		// right strip 0
		int i = bytes.length - 1;
		for (; i >= 0; i--) {
			if (bytes[i] != filter) {
				break;
			}
		}
		if (i > -1) {
			byte[] lstriped = new byte[i + 1];
			System.arraycopy(bytes, 0, lstriped, 0, lstriped.length);
			bytes = lstriped;
		}

		return bytes;
	}

	public static byte[] joins(byte[]... arrays) {
		int length = Stream.of(arrays).filter(Objects::nonNull).mapToInt(array -> array.length).sum();
		byte[] result = new byte[length];

		int currentLength = 0;
		for (int i = 0; i < arrays.length; i++) {
			byte[] array = arrays[i];
			if (null != array && array.length > 0) {
				System.arraycopy(array, 0, result, currentLength, array.length);
				currentLength += array.length;
			}
		}
		return result;
	}

	public static long unsignedLongSumToLong(byte[] bytes) {
		long sum = 0;
		for (int i = 0; i < bytes.length; i++) {
			sum += Byte.toUnsignedLong(bytes[i]);
		}
		return sum;
	}

	public static byte unsignedLongSumToByte(byte[] bytes) {
		return (byte) (unsignedLongSumToLong(bytes) & 0xFF);
	}

	public static byte[] hexStringTo(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}

	public static String toHexString(byte[] bs) {
		return Hex.toHexString(bs);
	}

}

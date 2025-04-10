/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuyulong
 * @since 2023年8月11日
 */
public class Base64Utils {

	public static String encodeToString(String content) {
		if (StringUtils.isNotBlank(content)) {
			return new String(encode(content), StandardCharsets.UTF_8);
		} else {
			return null;
		}
	}

	public static byte[] encode(String content) {
		if (StringUtils.isNotBlank(content)) {
			return Base64.getEncoder().encode(content.getBytes(StandardCharsets.UTF_8));
		} else {
			return null;
		}
	}

	public static String encodeToString(byte[] bytes) {
		if (null != bytes && bytes.length > 0) {
			return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
		} else {
			return null;
		}
	}

	public static String decodeToString(String content) {
		if (StringUtils.isNotBlank(content)) {
			return new String(decode(content), StandardCharsets.UTF_8);
		} else {
			return null;
		}
	}

	public static byte[] decode(String content) {
		if (StringUtils.isNotBlank(content)) {
			return Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
		} else {
			return null;
		}
	}

	public static int decodeLength(String content) {
		if (StringUtils.isNotBlank(content)) {
			return decode(content).length;
		} else {
			return 0;
		}
	}
}

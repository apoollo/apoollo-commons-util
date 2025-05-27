/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuyulong
 * @since 2023年8月11日
 */
public class Base64Utils {

	public static String encodeToString(String content, Charset charset) {
		if (StringUtils.isNotBlank(content)) {
			return new String(encode(content, charset), charset);
		} else {
			return null;
		}
	}

	public static byte[] encode(String content, Charset charset) {
		if (StringUtils.isNotBlank(content)) {
			return Base64.getEncoder().encode(content.getBytes(charset));
		} else {
			return null;
		}
	}

	public static String encodeToString(byte[] bytes, Charset charset) {
		if (null != bytes && bytes.length > 0) {
			return new String(Base64.getEncoder().encode(bytes), charset);
		} else {
			return null;
		}
	}

	public static String decodeToString(String content, Charset charset) {
		if (StringUtils.isNotBlank(content)) {
			return new String(decode(content, charset), charset);
		} else {
			return null;
		}
	}

	public static byte[] decode(String content, Charset charset) {
		if (StringUtils.isNotBlank(content)) {
			return Base64.getDecoder().decode(content.getBytes(charset));
		} else {
			return null;
		}
	}

	public static int decodeLength(String content, Charset charset) {
		if (StringUtils.isNotBlank(content)) {
			return decode(content, charset).length;
		} else {
			return 0;
		}
	}
}

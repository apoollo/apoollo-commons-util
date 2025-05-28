/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.Charset;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import com.apoollo.commons.util.crypto.hash.MacHash;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class HttpContentUtils {

	public static String getHttpContentSignature(MacHash macHash, String key, Charset httpCharset, String method,
			String requestPath, String queryString, TreeMap<String, String> headers, byte[] body) {
		String content = getHttpContent(httpCharset, method, requestPath, queryString, headers, body);
		return getHttpContentSignature(macHash, key, httpCharset, content);
	}

	public static String getHttpContentSignature(MacHash macHash, String key, Charset httpCharset, String content) {
		byte[] digest = macHash.hash(Hex.decodeStrict(key), content.getBytes(httpCharset));
		return Base64Utils.encodeToString(digest, httpCharset);
	}

	public static String getHttpContent(Charset httpCharset, String method, String requestPath, String queryString,
			TreeMap<String, String> headers, byte[] body) {
		String newLine = "\r\n";
		String newSpace = " ";
		String colon = ": ";

		String requestLine = method.toUpperCase() + newSpace + requestPath;
		if (StringUtils.isNotBlank(queryString)) {
			requestLine += "?" + queryString;
		}
		String header = null != headers
				? headers.entrySet().stream().map(entry -> entry.getKey().toLowerCase() + colon + entry.getValue())
						.collect(Collectors.joining(newLine))
				: null;

		StringBuilder stringBuilder = new StringBuilder();
		// request line
		stringBuilder.append(requestLine);
		// header
		if (StringUtils.isNoneBlank(header)) {
			stringBuilder.append(newLine);
			stringBuilder.append(header);
		}
		// empty line
		stringBuilder.append(newLine);
		// body
		if (null != body) {
			stringBuilder.append(Md5Utils.digestToBase64String(body, httpCharset));
		}
		return stringBuilder.toString();
	}
}

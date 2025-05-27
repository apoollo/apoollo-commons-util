/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.Charset;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.crypto.hash.MacHash;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class HttpContentUtils {

	public static String getHttpContentSignature(MacHash macHash, byte[] key, Charset httpCharset, String method,
			String uri, String queryString, TreeMap<String, String> headers, byte[] body) {
		String content = getHttpContent(httpCharset, method, uri, queryString, null, headers, body);
		byte[] digest = macHash.hash(key, content.getBytes(httpCharset));
		return Base64Utils.encodeToString(digest, httpCharset);
	}

	public static String getHttpContent(Charset httpCharset, String method, String uri, String queryString,
			String protocol, TreeMap<String, String> headers, byte[] body) {
		String newLine = "\r\n";
		String newSpace = " ";
		String colon = ": ";

		String requestLine = method.toUpperCase() + newSpace + uri;
		if (StringUtils.isNotBlank(queryString)) {
			requestLine += "?" + queryString;
		}
		if (StringUtils.isNotBlank(protocol)) {
			requestLine += newSpace + protocol.toUpperCase();
		}

		String header = null != headers ? headers.entrySet().stream()
				.map(entry -> entry.getKey() + colon + entry.getValue()).collect(Collectors.joining(newLine)) : null;

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

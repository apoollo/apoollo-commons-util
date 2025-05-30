/**
 * 
 */
package com.apoollo.commons.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.TreeMap;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.apoollo.commons.util.crypto.hash.HmacSHA256;
import com.apoollo.commons.util.crypto.hash.MacHash;

/**
 * @author liuyulong
 * @since 2025-05-27
 */
public class HttpContentUtilsTest {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String[] args) {
		Charset httpCharset = StandardCharsets.UTF_8;
		MacHash macHash = new HmacSHA256();
		String key = "08f4129e78219ebf19a4971667de589d";

		String body = "{\"name\":\"a\",\"password\":\"<abc><890>\",\"accessKey\":\"accessKey1\",\"secretKey\":\"secretKey1\"}";

		byte[] bodyBytes = body.getBytes(httpCharset);

		String httpContent = HttpContentUtils.getHttpContent(httpCharset, "GET", "/demo8", "c=d&1=3&a=b",
				new TreeMap<>() {

					private static final long serialVersionUID = 5810947227127465856L;
					{
						put("cache-control", "no-cache");
						put("content-type", "application/json");
						put("host", "127.0.0.1:8080");
						put("content-length", bodyBytes.length + "");
						put("x-timestamp", System.currentTimeMillis() + "");
						put("x-nonce",LangUtils.getUppercaseUUID());
					}

				}, bodyBytes);

		String signature = HttpContentUtils.getHttpContentSignature(macHash, key, httpCharset, httpContent);
		System.out.println(httpContent);
		System.out.println(signature);
	}
}

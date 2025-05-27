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
		byte[] key = "123".getBytes(httpCharset);
	

		String body = "{\"name\":\"a\",\"password\":\"<abc><890>\",\"accessKey\":\"accessKey1\",\"secretKey\":\"secretKey1\"}";


		String signature = HttpContentUtils.getHttpContentSignature(macHash, key, httpCharset, "post", "/demo8",
				"c=d&1=3&a=b", new TreeMap<>() {

					private static final long serialVersionUID = 5810947227127465856L;
					{
						put("Content-Type", "application/json");
					}

				}, body.getBytes(httpCharset));
		System.out.println(signature);
	}
}

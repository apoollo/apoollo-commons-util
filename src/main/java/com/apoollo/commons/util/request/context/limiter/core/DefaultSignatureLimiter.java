/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.HttpContentUtils;
import com.apoollo.commons.util.crypto.hash.HmacSHA256;
import com.apoollo.commons.util.crypto.hash.MacHash;
import com.apoollo.commons.util.exception.AppSignatureLimiterSignatureIllegalException;
import com.apoollo.commons.util.request.context.limiter.SignatureLimiter;
import com.apoollo.commons.util.request.context.model.RequestConstants;
import com.apoollo.commons.util.request.context.model.ServletInputStreamHelper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultSignatureLimiter implements SignatureLimiter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSignatureLimiter.class);

	private static final MacHash HMAC_SHA256 = new HmacSHA256();

	@Override
	public void limit(HttpServletRequest request, String secret, List<String> signatureLimiterExcludeHeaderNames,
			List<String> signatureLimiterIncludeHeaderNames, Supplier<byte[]> body) {

		String requestSignature = request.getHeader(RequestConstants.REQUEST_HEADER_SIGNATURE);
		if (StringUtils.isBlank(requestSignature)) {
			throw new AppSignatureLimiterSignatureIllegalException(
					"header [" + RequestConstants.REQUEST_HEADER_SIGNATURE + "] must not be null");
		}
		if (StringUtils.isBlank(secret)) {
			throw new RuntimeException("secret must not be blank");
		}
		String requestMehtod = request.getMethod();
		String requestUri = request.getRequestURI();
		String queryString = request.getQueryString();
		TreeMap<String, String> headers = getHeaders(request, signatureLimiterExcludeHeaderNames,
				signatureLimiterIncludeHeaderNames);
		Charset charset = ServletInputStreamHelper.getCharset(request);
		String httpContent = HttpContentUtils.getHttpContent(charset, requestMehtod, requestUri, queryString, headers,
				body.get());
		String signature = HttpContentUtils.getHttpContentSignature(HMAC_SHA256, secret, charset, httpContent);

		if (!StringUtils.equals(requestSignature, signature)) {
			LOGGER.error("secret:" + secret);
			LOGGER.error("signature:" + signature);
			LOGGER.error("httpContent:\n" + httpContent);
			throw new AppSignatureLimiterSignatureIllegalException("signature compared false");
		}
	}

	public TreeMap<String, String> getNameValues(Function<String, String> valueGetter, Iterator<String> names,
			List<String> excludes, List<String> includes) {
		TreeMap<String, String> treeMap = new TreeMap<>();
		names.forEachRemaining(name -> {
			name = name.toLowerCase();
			if ((null == excludes || excludes.isEmpty() || !excludes.contains(name))
					&& (null == includes || includes.isEmpty() || includes.contains(name))) {
				String value = valueGetter.apply(name);
				treeMap.put(name, value);
			}
		});
		return treeMap;
	}

	public TreeMap<String, String> getHeaders(HttpServletRequest request, List<String> excludes,
			List<String> includes) {
		if (CollectionUtils.isEmpty(excludes)) {
			excludes = List.of(RequestConstants.REQUEST_HEADER_SIGNATURE);
		}
		return getNameValues(headerName -> request.getHeader(headerName), request.getHeaderNames().asIterator(),
				excludes, includes);
	}

}

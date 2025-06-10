/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import java.util.List;
import java.util.function.Supplier;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface SignatureLimiter {

	public void limit(HttpServletRequest request, String secret, List<String> signatureLimiterExcludeHeaderNames,
			List<String> signatureLimiterIncludeHeaderNames, Supplier<byte[]> body);

}

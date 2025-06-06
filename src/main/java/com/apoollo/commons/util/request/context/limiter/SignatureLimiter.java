/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import java.util.function.Supplier;

import com.apoollo.commons.util.request.context.limiter.support.SignatureLimiterSupport;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface SignatureLimiter {

	public void limit(HttpServletRequest request, SignatureLimiterSupport signatureLimterSupport, Supplier<byte[]> body);

}

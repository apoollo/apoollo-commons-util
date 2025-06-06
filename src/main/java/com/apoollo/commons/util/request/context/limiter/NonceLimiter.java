/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import com.apoollo.commons.util.request.context.limiter.support.NonceLimiterSupport;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface NonceLimiter {

	public void limit(HttpServletRequest request, NonceLimiterSupport nonceLimiterSupport);
}

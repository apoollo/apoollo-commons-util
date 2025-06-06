/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import com.apoollo.commons.util.request.context.limiter.support.RefererLimiterSupport;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface RefererLimiter {

	public void limit(HttpServletRequest request, RefererLimiterSupport refererLimiterSupport);
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import com.apoollo.commons.util.request.context.limiter.support.CorsLimiterSupport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface CorsLimiter {

	public void limit(HttpServletRequest request, HttpServletResponse response, CorsLimiterSupport corsLimiterSupport);
}

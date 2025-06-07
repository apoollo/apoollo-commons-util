/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface RefererLimiter {

	public void limit(HttpServletRequest request, List<String> refererLimiterIncludeReferers);
}

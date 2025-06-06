/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

import org.springframework.web.cors.CorsConfiguration;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface CorsLimiterSupport {

	Boolean getEnableCorsLimiter();

	CorsConfiguration getCorsLimiterConfiguration();

}

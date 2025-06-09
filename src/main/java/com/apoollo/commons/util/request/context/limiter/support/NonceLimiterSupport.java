/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

import com.apoollo.commons.util.request.context.limiter.NonceValidator;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface NonceLimiterSupport {

	Boolean getEnableNonceLimiter();

	NonceValidator getNonceLimiterValidator();

	Long getNonceLimiterDuration();
}

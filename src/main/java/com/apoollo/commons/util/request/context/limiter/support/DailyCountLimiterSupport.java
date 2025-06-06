/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface DailyCountLimiterSupport extends PrincipalSupport {

	Boolean getEnableDailyCountLimiter();

	Long getDailyCountLimiterLimitCount();
}

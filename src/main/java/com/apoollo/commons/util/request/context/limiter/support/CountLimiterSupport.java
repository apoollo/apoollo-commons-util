/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

import com.apoollo.commons.util.redis.service.RedisNameSpaceKey.TimeUnitPattern;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface CountLimiterSupport extends PrincipalSupport {
	
	Boolean getEnableCountLimiter();
	
	TimeUnitPattern getCountLimiterTimeUnitPattern();

	Long getCountLimiterLimitCount();
}

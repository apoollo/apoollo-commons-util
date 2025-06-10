/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import com.apoollo.commons.util.redis.service.RedisNameSpaceKey.TimeUnitPattern;
import com.apoollo.commons.util.redis.service.impl.CommonsCountLimiter.Incremented;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface TimeUnitPatternCountLimiter {

	public default Incremented limit(String resourcePin, TimeUnitPattern timeUnitPattern, Long limitCount) {
		return limit(null, resourcePin, timeUnitPattern, limitCount);
	}

	public Incremented limit(String accessKey, String resourcePin, TimeUnitPattern timeUnitPattern, Long limitCount);
}

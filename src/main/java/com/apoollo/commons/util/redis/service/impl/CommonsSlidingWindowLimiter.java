/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import org.springframework.data.redis.core.RedisTemplate;

import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.redis.service.SlidingWindowLimiter;
import com.apoollo.commons.util.redis.service.model.SlidingWindowCount;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
public class CommonsSlidingWindowLimiter extends CommonsSlidingWindowCounter implements SlidingWindowLimiter {

	public CommonsSlidingWindowLimiter(RedisTemplate<String, String> redisTemplate,
			RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate, redisNameSpaceKey);
	}

	@Override
	public boolean access(String key, long windowTimeMillis, long limitCount) {
		SlidingWindowCount slidingWindowCount = count(key, windowTimeMillis);
		return limitCount >= slidingWindowCount.getCount();
	}

}

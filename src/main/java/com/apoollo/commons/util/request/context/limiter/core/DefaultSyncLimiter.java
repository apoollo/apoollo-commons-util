/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;

import com.apoollo.commons.util.exception.refactor.AppSyncLimiterRefusedException;
import com.apoollo.commons.util.redis.RedisUtils;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.limiter.SyncLimiter;

/**
 * @author liuyulong
 * @since 2024-11-29
 */
public class DefaultSyncLimiter implements SyncLimiter {

	private RedisTemplate<String, String> redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;

	public DefaultSyncLimiter(RedisTemplate<String, String> redisTemplate, RedisNameSpaceKey redisNameSpaceKey) {
		super();
		this.redisTemplate = redisTemplate;
		this.redisNameSpaceKey = redisNameSpaceKey;
	}

	@Override
	public void limit(String accessKey, String resourcePin, Duration duration) {
		String key = redisNameSpaceKey.getKey(RedisNameSpaceKey.SLIDING_WINDOW_COUNTER, accessKey, resourcePin);
		if (!RedisUtils.fight(redisTemplate, key, duration)) {
			throw new AppSyncLimiterRefusedException("sync limit refused");
		}
	}

	@Override
	public void unlimit(String accessKey, String resourcePin) {
		String key = redisNameSpaceKey.getKey(RedisNameSpaceKey.SLIDING_WINDOW_COUNTER, accessKey, resourcePin);
		redisTemplate.delete(key);
	}

}

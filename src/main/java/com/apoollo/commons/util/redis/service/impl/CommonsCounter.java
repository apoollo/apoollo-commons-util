/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.apoollo.commons.util.redis.service.AbstractNamespaceRedisEvalLua;
import com.apoollo.commons.util.redis.service.Counter;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey.TimeUnitPattern;

/**
 * @author liuyulong
 */
public class CommonsCounter extends AbstractNamespaceRedisEvalLua implements Counter {

	private static final RedisScript<Long> REDIS_SCRIPT = getRedisScript("Increment.lua", Long.class);

	public CommonsCounter(RedisTemplate<String, String> redisTemplate, RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate, redisNameSpaceKey);
	}

	@Override
	public Long increment(String key, TimeUnitPattern timeUnitPattern, long currentTimeMillis, long timeout,
			TimeUnit timeoutUnit) {
		String keyPrefix = null == timeUnitPattern ? null
				: TimeUnitPattern.getTimeUnitPattern(currentTimeMillis, timeUnitPattern);
		return increment(key, keyPrefix, currentTimeMillis, timeout, timeoutUnit);
	}

	public Long increment(String key, String keySuffix, long currentTimeMillis, long timeout, TimeUnit timeoutUnit) {
		String targetKey = getKey(RedisNameSpaceKey.COUNTER, key, keySuffix);
		Date expireAt = DateUtils.addSeconds(//
				new Date(currentTimeMillis), // 当前时间
				Long.valueOf(timeoutUnit.toSeconds(timeout)).intValue()// 超时时间
		);
		Long result = execute(REDIS_SCRIPT, targetKey, getRedisExpireAt(expireAt));
		return result;
	}

}

/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.apoollo.commons.util.redis.service.AbstractRedisEvalLua;
import com.apoollo.commons.util.redis.service.Counter;

/**
 * @author liuyulong
 */
public class CommonsCounter extends AbstractRedisEvalLua implements Counter {

	private static final RedisScript<Long> REDIS_SCRIPT = getRedisScript("Increment.lua", Long.class);

	public CommonsCounter(RedisTemplate<String, String> redisTemplate) {
		super(redisTemplate);
	}

	@Override
	public Long increment(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit) {
		Date expireAt = DateUtils.addSeconds(//
				new Date(currentTimeMillis), // 当前时间
				Long.valueOf(timeoutUnit.toSeconds(timeout)).intValue()// 超时时间
		);
		Long result = execute(REDIS_SCRIPT, key, getRedisExpireAt(expireAt));
		return result;
	}

	@Override
	public Long incrementDate(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit) {
		String finalKey = getDailyKey(key, currentTimeMillis);
		return increment(finalKey, currentTimeMillis, timeout, timeoutUnit);
	}

}

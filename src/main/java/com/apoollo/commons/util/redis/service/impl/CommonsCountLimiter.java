/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.alibaba.fastjson2.JSON;
import com.apoollo.commons.util.redis.service.AbstractNamespaceRedisEvalLua;
import com.apoollo.commons.util.redis.service.CountLimiter;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
public class CommonsCountLimiter extends AbstractNamespaceRedisEvalLua implements CountLimiter {

	private static final RedisScript<String> REDIS_SCRIPT = getRedisScript("IncrementLimiter.lua", String.class);

	public CommonsCountLimiter(RedisTemplate<String, String> redisTemplate, RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate, redisNameSpaceKey);
	}

	@Override
	public Incremented increment(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit,
			long limitCount) {
		return increment(key, null, currentTimeMillis, timeout, timeoutUnit, limitCount);
	}

	public Incremented increment(String key, String keyPrefix, long currentTimeMillis, long timeout,
			TimeUnit timeoutUnit, long limitCount) {
		String targetKey = getKey(RedisNameSpaceKey.COUNT_LIMITER, key, keyPrefix);
		Date expireAt = DateUtils.addSeconds(//
				new Date(currentTimeMillis), // 当前时间
				Long.valueOf(timeoutUnit.toSeconds(timeout)).intValue()// 超时时间
		);
		String result = execute(REDIS_SCRIPT, targetKey, getRedisExpireAt(expireAt), limitCount);
		Incremented incremented = JSON.parseObject(result, Incremented.class);
		incremented.setKey(targetKey);
		return incremented;
	}

	@Override
	public Incremented incrementDate(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit,
			long limitCount) {
		if (timeout < 1) {
			throw new IllegalArgumentException("timeoutDays must great than 0");
		}
		return increment(key, RedisNameSpaceKey.getDaily(currentTimeMillis), currentTimeMillis, timeout, timeoutUnit,
				limitCount);

	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Incremented {
		private Boolean accessed;// 是否通过
		private Long currentCount;// 当前计数，如果accessed=false ，真实数量为currentCount-1
		private String key;// key
	}

}

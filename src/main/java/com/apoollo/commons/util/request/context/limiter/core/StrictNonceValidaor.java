package com.apoollo.commons.util.request.context.limiter.core;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.apoollo.commons.util.redis.RedisUtils;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.limiter.NonceValidator;

public class StrictNonceValidaor implements NonceValidator {

	private StringRedisTemplate redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;

	public StrictNonceValidaor(StringRedisTemplate redisTemplate, RedisNameSpaceKey redisNameSpaceKey) {
		super();
		this.redisNameSpaceKey = redisNameSpaceKey;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean isValid(String nonce, long duration) {
		return RedisUtils.fight(redisTemplate, redisNameSpaceKey.getKey(RedisNameSpaceKey.NONCE, nonce),
				Duration.ofMillis(duration));
	}

}

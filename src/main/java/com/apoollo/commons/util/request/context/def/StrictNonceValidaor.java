package com.apoollo.commons.util.request.context.def;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.apoollo.commons.util.redis.RedisUtils;
import com.apoollo.commons.util.redis.service.CommonUtilRedisKey;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.NonceValidator;

public class StrictNonceValidaor implements NonceValidator {

	private CommonUtilRedisKey commonUtilRedisKey;
	private StringRedisTemplate redisTemplate;

	public StrictNonceValidaor(RedisNameSpaceKey redisNameSpaceKey, StringRedisTemplate redisTemplate) {
		super();
		this.commonUtilRedisKey = () -> redisNameSpaceKey;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean isValid(String nonce, long duration) {

		return RedisUtils.fight(redisTemplate, commonUtilRedisKey.getNonceKey(nonce), Duration.ofMillis(duration));
	}

}

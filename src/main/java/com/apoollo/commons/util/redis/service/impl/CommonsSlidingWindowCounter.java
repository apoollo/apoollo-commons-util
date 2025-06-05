/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.redis.service.AbstractNamespaceRedisEvalLua;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.redis.service.SlidingWindowCounter;
import com.apoollo.commons.util.redis.service.model.SlidingWindowCount;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
public class CommonsSlidingWindowCounter extends AbstractNamespaceRedisEvalLua implements SlidingWindowCounter {

	@SuppressWarnings("rawtypes")
	private static final RedisScript<List> REDIS_SCRIPT = getRedisScript("SlidingWindowCounter.lua", List.class);

	public CommonsSlidingWindowCounter(RedisTemplate<String, String> redisTemplate,
			RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate, redisNameSpaceKey);
	}

	@Override
	public SlidingWindowCount count(String key, long windowTimeMillis) {

		String targetKey = getCommonUtilRedisKey().getCommonsUtilKey(key);
		List<?> list = execute(REDIS_SCRIPT, targetKey, System.currentTimeMillis(), windowTimeMillis);
		SlidingWindowCount slidingWindowCount = new SlidingWindowCount(LangUtils.toLong(list.get(0)),
				LangUtils.toLong(list.get(1)));
		return slidingWindowCount;
	}

}

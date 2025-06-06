/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
public class AbstractNamespaceRedisEvalLua extends AbstractRedisEvalLua {

	private RedisNameSpaceKey redisNameSpaceKey;

	public AbstractNamespaceRedisEvalLua(RedisTemplate<String, String> redisTemplate,
			RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate);
		this.redisNameSpaceKey = redisNameSpaceKey;
	}

	public String getKey(String prefix, String key, String suffix) {
		return redisNameSpaceKey.getKey(prefix, key, suffix);
	}
}

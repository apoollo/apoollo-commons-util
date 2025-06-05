/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
public class AbstractNamespaceRedisEvalLua extends AbstractRedisEvalLua {

	private CommonUtilRedisKey commonUtilRedisKey;

	public AbstractNamespaceRedisEvalLua(RedisTemplate<String, String> redisTemplate,
			RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate);
		this.commonUtilRedisKey = () -> redisNameSpaceKey;
	}

	public CommonUtilRedisKey getCommonUtilRedisKey() {
		return commonUtilRedisKey;
	}

	public String getKey(String key, Supplier<String> keyAppender) {
		String targetKey = getCommonUtilRedisKey().getCommonsUtilKey(key);
		if (null != keyAppender) {
			targetKey = StringUtils.joinWith(":", key, keyAppender.get());
		}
		return targetKey;
	}
}

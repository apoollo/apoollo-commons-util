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

	private CommonUtilRedisKey commonUtilRedisKey;

	/**
	 * @param redisTemplate
	 */
	public AbstractNamespaceRedisEvalLua(RedisTemplate<String, String> redisTemplate,
			RedisNameSpaceKey redisNameSpaceKey) {
		super(redisTemplate);
		this.commonUtilRedisKey = () -> redisNameSpaceKey;
	}

	public CommonUtilRedisKey getCommonUtilRedisKey() {
		return commonUtilRedisKey;
	}

}

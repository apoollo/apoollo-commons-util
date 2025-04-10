/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson2.JSON;
import com.apoollo.commons.util.redis.service.HashObject;

/**
 * @author liuyulong
 */
public class CommonsHashObject implements HashObject {

	private RedisTemplate<String, String> redisTemplate;

	/**
	 * @param redisTemplate
	 */
	public CommonsHashObject(RedisTemplate<String, String> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	@Override
	public Object getValue(String key, String field) {
		return redisTemplate.opsForHash().get(key, field);
	}

	@Override
	public void setValue(String key, String field, Object value) {
		if (null != value) {
			String targetValue = null;
			if (value instanceof String) {
				targetValue = (String) value;
			} else {
				targetValue = JSON.toJSONString(value);
			}
			redisTemplate.opsForHash().put(key, field, targetValue);
		}
	}

	@Override
	public void delete(String key, String field) {
		redisTemplate.opsForHash().delete(key, field);
	}

	@Override
	public void delete(String key) {
		redisTemplate.delete(key);

	}

}

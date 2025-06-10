/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.concurrent.TimeUnit;

import com.apoollo.commons.util.redis.service.RedisNameSpaceKey.TimeUnitPattern;

/**
 * @author liuyulong
 */
public interface Counter {

	/**
	 * 
	 * @param key               redisKey
	 * @param timeUnitPattern   时间单位模式，用于时间维度自动切换Key
	 * @param currentTimeMillis 当前时间
	 * @param timeout           超时时间，只有第一次设置值的时候才会设置过期时间
	 * @param timeoutUnit       超时时间单位
	 * 
	 *                          只有第一次设置值的时候才会设置过期时间
	 * @return 增长后的值
	 */
	public Long increment(String key, TimeUnitPattern timeUnitPattern, long currentTimeMillis, long timeout,
			TimeUnit timeoutUnit);

	/**
	 * 
	 * @param key               redisKey
	 * @param currentTimeMillis 当前时间
	 * @param timeout           超时时间,只有第一次设置值的时候才会设置过期时间
	 * @param timeoutUnit       超时时间单位
	 * 
	 * 
	 * @return 增长后的值
	 */
	public default Long incrementDate(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit) {
		return increment(key, null, currentTimeMillis, timeout, timeoutUnit);
	}
}

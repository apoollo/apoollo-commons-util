/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * @author liuyulong
 */
public interface Counter extends RedisKey {

	/**
	 * 
	 * @param key               redisKey
	 * @param currentTimeMillis 当前时间
	 * @param timeout           超时时间
	 * @param timeoutUnit       超时时间单位
	 * 
	 *                          只有第一次设置值的时候才会设置过期时间
	 * @return 增长后的值
	 */
	public Long increment(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit);

	/**
	 * 
	 * @param key               redisKey
	 * @param currentTimeMillis 当前时间
	 * @param timeout           超时时间
	 * @param timeoutUnit       超时时间单位
	 * 
	 *                          只有第一次设置值的时候才会设置过期时间
	 * @return 增长后的值
	 */
	public Long incrementDate(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit);
}

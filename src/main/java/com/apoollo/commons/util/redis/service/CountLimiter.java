/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.concurrent.TimeUnit;

import com.apoollo.commons.util.redis.service.impl.CommonsCountLimiter.Incremented;

/**
 * @author liuyulong
 */
public interface CountLimiter {

	/**
	 * 一定时间内限制数量增长
	 * 
	 * @param key               唯一标识符
	 * @param currentTimeMillis 当前时间
	 * @param timeout           过期时间
	 * @param timeoutUnit       过期时间单位
	 * @param limitCount        限制增长的数量
	 * @return
	 */
	public Incremented increment(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit,
			long limitCount);

	/**
	 * 按照天数限制
	 * 
	 * @param key               唯一标识符
	 * @param currentTimeMillis 当前时间
	 * @param timeout           过期时间
	 * @param timeoutUnit       过期时间单位
	 * @param limitCount        限制增长的数量
	 * @return
	 */
	public Incremented incrementDate(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit,
			long limitCount);

}

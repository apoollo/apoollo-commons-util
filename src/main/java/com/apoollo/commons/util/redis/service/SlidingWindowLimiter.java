/**
 * 
 */
package com.apoollo.commons.util.redis.service;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
public interface SlidingWindowLimiter {

	public boolean access(String key, long windowTimeMillis, long limitCount);
}

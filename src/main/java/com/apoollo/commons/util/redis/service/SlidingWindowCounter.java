/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import com.apoollo.commons.util.redis.service.model.SlidingWindowCount;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
public interface SlidingWindowCounter {

	public SlidingWindowCount count(String key, long windowTimeMillis);

}

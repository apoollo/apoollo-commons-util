/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.concurrent.TimeUnit;

import com.apoollo.commons.util.redis.service.impl.CommonsCountLimiter.Incremented;

/**
 * @author liuyulong
 */
public interface CountLimiter extends RedisKey {

    /**
     * 一定时间内限制数量增长
     * 
     * @param key
     * @param currentTimeMillis 当前时间
     * @param timeout           过期时间
     * @param timeoutUnit       过期单位
     * @param limitCount        限制增长的数量
     * @return
     */
    public Incremented increment(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit,
            long limitCount);

    /**
     * 按照天数限制
     * 
     * @param key
     * @param timeoutDays 限制天数
     * @param limitCount  限制增长的数量
     * @return
     */
    public Incremented incrementDate(String key, int timeoutDays, long limitCount);

    /**
     * 用于冲正，减一
     * 
     * @param key
     */

    public void decrement(String key);

}

/**
 * 
 */
package com.apoollo.commons.util.redis.service.impl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.alibaba.fastjson2.JSON;
import com.apoollo.commons.util.redis.service.AbstractRedisEvalLua;
import com.apoollo.commons.util.redis.service.CountLimiter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
public class CommonsCountLimiter extends AbstractRedisEvalLua implements CountLimiter {

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(CommonsInvocationTimesLimiter.class);

    private static final RedisScript<String> REDIS_SCRIPT = getRedisScript("InvocationTimesLimiter.lua", String.class);

    public CommonsCountLimiter(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Incremented increment(String key, long currentTimeMillis, long timeout, TimeUnit timeoutUnit,
            long limitCount) {
        // if (limitCount < 1) {
        // throw new IllegalArgumentException("limitCount must great than 0");
        // }
        Date expireAt = DateUtils.addSeconds(//
                new Date(currentTimeMillis), // 当前时间
                Long.valueOf(timeoutUnit.toSeconds(timeout)).intValue()// 超时时间
        );
        String result = execute(REDIS_SCRIPT, key, getRedisExpireAt(expireAt), limitCount);
        Incremented incremented = JSON.parseObject(result, Incremented.class);
        incremented.setKey(key);
        return incremented;
    }

    @Override
    public Incremented incrementDate(String key, int timeoutDays, long limitCount) {
        if (timeoutDays < 1) {
            throw new IllegalArgumentException("timeoutDays must great than 0");
        }
        long currentTimeMillis = System.currentTimeMillis();
        String finalKey = getDailyKey(key, currentTimeMillis);
        return increment(finalKey, currentTimeMillis, timeoutDays, TimeUnit.DAYS, limitCount);

    }

   

    @Override
    public void decrement(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Incremented {
        private Boolean accessed;// 是否通过
        private Long currentCount;// 当前计数，如果accessed=false ，真实数量为currentCount-1
        private String key;// key
    }

}

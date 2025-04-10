/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */
public abstract class AbstractRedisEvalLua {

    protected RedisTemplate<String, String> redisTemplate;

    public AbstractRedisEvalLua(RedisTemplate<String, String> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    public static <T> RedisScript<T> getRedisScript(String luaName, Class<T> clazz) {
        return RedisScript.of(new ClassPathResource("com/apoollo/commons/util/redis/service/resources/" + luaName),
                clazz);
    }

    public Date getExpireDate(Long timeMillis, Integer addDays) {
        Date expireAt = DateUtils.addDays(new Date(timeMillis), addDays);
        return DateUtils.truncate(expireAt, Calendar.SECOND);
    }

    public Long getRedisExpireAt(Date date) {
        return date.getTime() / 1000;
    }

    public <T> T execute(RedisScript<T> redisScript, String key, Object... args) {
        for (int i = 0; null != args && i < args.length; i++) {
            if (null == args[i]) {
                args[i] = "";
            } else if (!(args[i] instanceof String)) {
                args[i] = LangUtils.toString(args[i]);
            }
        }
        T result = redisTemplate.execute(redisScript, Collections.singletonList(key), args);
        return result;
    }
}

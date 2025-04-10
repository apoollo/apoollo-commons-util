/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */
public interface CommonUtilRedisKey {

    public RedisNameSpaceKey getRedisNameSpaceKey();

    public default String getCommonsUtilKey(String... services) {
        return getRedisNameSpaceKey().getKey("commons-util", LangUtils.getStream(services));
    }

    public default String getCaptchaKey(String token) {
        return getCommonsUtilKey("captcha", token);
    }
}

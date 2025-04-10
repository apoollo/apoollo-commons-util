/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @author liuyulong
 */
public interface RedisKey {

    public default String getDateKey(String key, long currentTimeMillis, String dateFormatPattern) {
        return StringUtils.joinWith(":", key, DateFormatUtils.format(currentTimeMillis, dateFormatPattern));
    }
    
    public default String getDailyKey(String key, long currentTimeMillis) {
        return getDateKey(key, currentTimeMillis, "yyyyMMdd");
    }
}

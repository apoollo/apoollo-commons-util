/**
 * 
 */
package com.apoollo.commons.util.redis;

import java.time.Duration;
import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson2.util.DateUtils;

/**
 * @author liuyulong
 * @since 2023年8月28日
 */
public class RedisUtils {

	/**
	 * <pre>
	 * 服务器多个节点同时执行一个任务时，只希望有一个服务器触发该任务，这个时候需要知道其他节点有没有已经执行过该任务
	 * 如果已经执行返回false，未执行，返回true
	 * </pre>
	 * 
	 * @param redisTemplate
	 * @param key           本次竞争的业务key，确保唯一，不予竞争字段中其他字段重复，否则会影响判断结果
	 * @param duration      锁定时长
	 * @return 判断结果
	 */
	public static boolean fight(RedisTemplate<String, String> redisTemplate, String key, Duration duration) {
		Boolean bool = redisTemplate.opsForValue().setIfAbsent(key, DateUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"),
				duration);
		return BooleanUtils.isTrue(bool);
	}
}

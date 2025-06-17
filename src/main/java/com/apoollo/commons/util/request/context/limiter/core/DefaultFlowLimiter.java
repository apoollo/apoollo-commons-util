/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.exception.AppFlowLimiterRefusedException;
import com.apoollo.commons.util.redis.service.SlidingWindowLimiter;
import com.apoollo.commons.util.request.context.limiter.FlowLimiter;

/**
 * @author liuyulong
 */
public class DefaultFlowLimiter implements FlowLimiter {

	private SlidingWindowLimiter slidingWindowLimiter;

	public DefaultFlowLimiter(SlidingWindowLimiter slidingWindowLimiter) {
		this.slidingWindowLimiter = slidingWindowLimiter;
	}

	@Override
	public void limit(String accessKey, String resourcePin, Long limitCount) {

		if (null == limitCount || limitCount < 1) {
			throw new RuntimeException("limitCount must ge 1");
		}

		if (limitCount % 2 != 0) {
			throw new IllegalArgumentException("limitCount illegal: " + limitCount);
		}

		String key = Stream.of(accessKey, resourcePin).filter(StringUtils::isNotBlank).collect(Collectors.joining(":"));

		// 500毫秒内可以调用的次数
		if (!slidingWindowLimiter.access(key, 500, limitCount / 2)) {
			throw new AppFlowLimiterRefusedException("flow limiter refused");
		}
	}

}

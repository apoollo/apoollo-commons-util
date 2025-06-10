/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.exception.AppExceedingTimeUnitMaximumUseTimesLimitException;
import com.apoollo.commons.util.redis.service.CountLimiter;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey.TimeUnitPattern;
import com.apoollo.commons.util.redis.service.impl.CommonsCountLimiter.Incremented;
import com.apoollo.commons.util.request.context.limiter.TimeUnitPatternCountLimiter;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultTimeUnitPatternCountLimiter implements TimeUnitPatternCountLimiter {

	private CountLimiter countLimiter;

	public DefaultTimeUnitPatternCountLimiter(CountLimiter countLimiter) {
		super();
		this.countLimiter = countLimiter;
	}

	@Override
	public Incremented limit(String accessKey, String resourcePin, TimeUnitPattern timeUnitPattern, Long limitCount) {

		if (null == timeUnitPattern) {
			throw new RuntimeException("timeUnitPattern must not be null");
		}

		if (null == limitCount || limitCount < 1) {
			throw new RuntimeException("limitCount must ge 1");
		}

		TimeUnit timeUnit = null;
		long timeout = 0;
		switch (timeUnitPattern) {
		case YEAR:
			timeout = 367;
			timeUnit = TimeUnit.DAYS;
			break;
		case MONTH:
			timeout = 32;
			timeUnit = TimeUnit.DAYS;
			break;
		case DAY:
			timeout = 2;
			timeUnit = TimeUnit.DAYS;
			break;
		case HOUR:
			timeout = 2;
			timeUnit = TimeUnit.HOURS;
			break;
		case MINUTE:
			timeout = 2;
			timeUnit = TimeUnit.MINUTES;
			break;
		case SECONDS:
			timeout = 2;
			timeUnit = TimeUnit.SECONDS;
			break;
		}

		String key = Stream.of(accessKey, resourcePin).filter(StringUtils::isNotBlank).collect(Collectors.joining(":"));

		Incremented incremented = countLimiter.increment(key, timeUnitPattern, System.currentTimeMillis(), timeout,
				timeUnit, limitCount);
		if (BooleanUtils.isTrue(incremented.getAccessed())) {
			return incremented;
		} else {
			throw new AppExceedingTimeUnitMaximumUseTimesLimitException(
					timeUnitPattern + " limit times: " + limitCount);
		}
	}
}

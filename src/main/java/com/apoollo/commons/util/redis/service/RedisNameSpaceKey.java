/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */
public interface RedisNameSpaceKey {

	public String getNamespace();

	public default String getKey(String serviceNamespace, List<String> services) {
		return getKey(serviceNamespace, services.stream());
	}

	public default String getKey(String serviceNamespace, Stream<String> services) {
		return getKey(Stream.concat(LangUtils.getStream(serviceNamespace), services));
	}

	public default String getKey(String... services) {
		return getKey(LangUtils.getStream(services));
	}

	public default String getKey(Stream<String> services) {
		return new StringBuilder(getNamespace())//
				.append(getSplitor())//
				.append(services.filter(StringUtils::isNotBlank).collect(Collectors.joining(getSplitor())))//
				.toString();
	}

	public default String getSplitor() {
		return ":";
	}

	public static enum TimeUnitPattern {
		
		YEAR("yyyy"), //
		MONTH("yyyyMM"), //
		DAY("yyyyMMdd"), //
		HOUR("yyyyMMddHH"), //
		MINUTE("yyyyMMddHHmm"), //
		SECONDS("yyyyMMddHHmmss");

		private String pattern;

		private TimeUnitPattern(String pattern) {
			this.pattern = pattern;
		}

		public String getPattern() {
			return pattern;
		}
		
		public static String getTimeUnitPattern(long currentTimeMillis, TimeUnitPattern timeUnitPattern) {
			return DateFormatUtils.format(currentTimeMillis, timeUnitPattern.getPattern());
		}
	}



	public static final String SYNC_LIMITER = "sync-limiter";
	public static final String COUNT_LIMITER = "count-limiter";
	public static final String SLIDING_WINDOW_COUNTER = "sliding-window-counter";
	public static final String COUNTER = "counter";
	public static final String NONCE = "nonce";
	public static final String CAPTCHA = "captcha";
	public static final String AUTHORIZATION = "authorization";
	public static final String REQUEST_RESOURCE = "request-resource";
	public static final String USER = "user";
}

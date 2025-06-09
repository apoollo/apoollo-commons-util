/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;

import com.apoollo.commons.util.request.context.limiter.NonceValidator;
import com.apoollo.commons.util.request.context.limiter.support.LimitersSupport;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
@Getter
@Setter
public class DefaultLimitersSupport implements LimitersSupport {
	
	private String resourcePin;

	private String accessKey;

	private Boolean enableNonceLimiter;

	private NonceValidator nonceLimiterValidator;

	private Long nonceLimiterDuration;

	private Boolean enableSignatureLimiter;

	private String signatureLimiterSecret;

	private List<String> signatureLimiterExcludeHeaderNames;

	private List<String> signatureLimiterIncludeHeaderNames;

	private Boolean enableCorsLimiter;

	private CorsConfiguration corsLimiterConfiguration;

	private Boolean enableIpLimiter;

	private List<String> ipLimiterExcludes;

	private List<String> ipLimiterIncludes;

	private Boolean enableRefererLimiter;

	private List<String> refererLimiterIncludeReferers;

	private Boolean enableSyncLimiter;

	private Boolean enableFlowLimiter;

	private Long flowLimiterLimitCount;

	private Boolean enableDailyCountLimiter;

	private Long dailyCountLimiterLimitCount;
}

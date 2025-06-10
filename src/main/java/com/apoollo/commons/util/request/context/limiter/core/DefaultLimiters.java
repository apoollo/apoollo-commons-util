/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.time.Duration;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.limiter.CorsLimiter;
import com.apoollo.commons.util.request.context.limiter.TimeUnitPatternCountLimiter;
import com.apoollo.commons.util.request.context.limiter.FlowLimiter;
import com.apoollo.commons.util.request.context.limiter.IpLimiter;
import com.apoollo.commons.util.request.context.limiter.Limiters;
import com.apoollo.commons.util.request.context.limiter.NonceLimiter;
import com.apoollo.commons.util.request.context.limiter.RefererLimiter;
import com.apoollo.commons.util.request.context.limiter.SignatureLimiter;
import com.apoollo.commons.util.request.context.limiter.SyncLimiter;
import com.apoollo.commons.util.request.context.limiter.support.LimitersSupport;
import com.apoollo.commons.util.request.context.model.ServletInputStreamHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultLimiters<T extends LimitersSupport> implements Limiters<T> {

	private NonceLimiter nonceLimiter;
	private SignatureLimiter signatureLimter;
	private CorsLimiter corsLimiter;
	private IpLimiter ipLimiter;
	private RefererLimiter refererLimiter;
	private SyncLimiter syncLimiter;
	private FlowLimiter flowLimiter;
	private TimeUnitPatternCountLimiter timeUnitPatternCountLimiter;

	public DefaultLimiters(NonceLimiter nonceLimiter, SignatureLimiter signatureLimter, CorsLimiter corsLimiter,
			IpLimiter ipLimiter, RefererLimiter refererLimiter, SyncLimiter syncLimiter, FlowLimiter flowLimiter,
			TimeUnitPatternCountLimiter timeUnitPatternCountLimiter) {
		super();
		this.nonceLimiter = nonceLimiter;
		this.signatureLimter = signatureLimter;
		this.corsLimiter = corsLimiter;
		this.ipLimiter = ipLimiter;
		this.refererLimiter = refererLimiter;
		this.syncLimiter = syncLimiter;
		this.flowLimiter = flowLimiter;
		this.timeUnitPatternCountLimiter = timeUnitPatternCountLimiter;
	}

	@Override
	public void limit(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext,
			T support) {

		// TODO list
		// 一个请求中，同一个limiter 最多只能请求一次，防止配置混乱
		// Limiters 中的异常需要重新规划，返回的Code码

		if (BooleanUtils.isTrue(support.getEnableNonceLimiter())) {
			nonceLimiter.limit(request, support.getNonceLimiterValidator(), support.getNonceLimiterDuration());
		}
		if (BooleanUtils.isTrue(support.getEnableSignatureLimiter())) {
			signatureLimter.limit(request, support.getSignatureLimiterSecret(),
					support.getSignatureLimiterExcludeHeaderNames(), support.getSignatureLimiterIncludeHeaderNames(),
					() -> ServletInputStreamHelper.getCachingBodyByteArray(requestContext, request));
		}
		if (BooleanUtils.isTrue(support.getEnableCorsLimiter())) {
			corsLimiter.limit(request, response, support.getCorsLimiterConfiguration());
		}
		if (BooleanUtils.isTrue(support.getEnableIpLimiter())) {
			ipLimiter.limit(support.getIpLimiterExcludes(), support.getIpLimiterIncludes(),
					requestContext.getRequestIp());
		}
		if (BooleanUtils.isTrue(support.getEnableRefererLimiter())) {
			refererLimiter.limit(request, support.getRefererLimiterIncludeReferers());
		}
		if (BooleanUtils.isTrue(support.getEnableSyncLimiter())) {
			syncLimiter.limit(support.getAccessKey(), support.getResourcePin(), Duration.ofSeconds(30));
		}
		if (BooleanUtils.isTrue(support.getEnableFlowLimiter())) {
			flowLimiter.limit(support.getAccessKey(), support.getResourcePin(), support.getFlowLimiterLimitCount());
		}
		if (BooleanUtils.isTrue(support.getEnableCountLimiter())) {
			timeUnitPatternCountLimiter.limit(support.getAccessKey(), support.getResourcePin(),
					support.getCountLimiterTimeUnitPattern(), support.getCountLimiterLimitCount());
		}
	}

	@Override
	public void unlimit(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext,
			T support) {
		if (BooleanUtils.isTrue(support.getEnableSyncLimiter())) {
			syncLimiter.unlimit(support.getAccessKey(), support.getResourcePin());
		}
	}

}

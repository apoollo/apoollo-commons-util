/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.time.Duration;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.limiter.CorsLimiter;
import com.apoollo.commons.util.request.context.limiter.DailyCountLimiter;
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
	private DailyCountLimiter dailyCountLimiter;

	public DefaultLimiters(NonceLimiter nonceLimiter, SignatureLimiter signatureLimter, CorsLimiter corsLimiter,
			IpLimiter ipLimiter, RefererLimiter refererLimiter, SyncLimiter syncLimiter, FlowLimiter flowLimiter,
			DailyCountLimiter dailyCountLimiter) {
		super();
		this.nonceLimiter = nonceLimiter;
		this.signatureLimter = signatureLimter;
		this.corsLimiter = corsLimiter;
		this.ipLimiter = ipLimiter;
		this.refererLimiter = refererLimiter;
		this.syncLimiter = syncLimiter;
		this.flowLimiter = flowLimiter;
		this.dailyCountLimiter = dailyCountLimiter;
	}

	@Override
	public void limit(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext,
			T support) {
		if (BooleanUtils.isTrue(support.getEnableNonceLimiter())) {
			nonceLimiter.limit(request, support);
		}
		if (BooleanUtils.isTrue(support.getEnableSignatureLimiter())) {
			signatureLimter.limit(request, support,
					() -> ServletInputStreamHelper.getCachingBodyByteArray(requestContext, request));
		}
		if (BooleanUtils.isTrue(support.getEnableCorsLimiter())) {
			corsLimiter.limit(request, response, support);
		}
		if (BooleanUtils.isTrue(support.getEnableIpLimiter())) {
			ipLimiter.limit(support, requestContext.getRequestIp());
		}
		if (BooleanUtils.isTrue(support.getEnableRefererLimiter())) {
			refererLimiter.limit(request, support);
		}
		if (BooleanUtils.isTrue(support.getEnableSyncLimiter())) {
			syncLimiter.limit(support.getAccessKey(), support.getResourcePin(), Duration.ofSeconds(30));
		}
		if (BooleanUtils.isTrue(support.getEnableFlowLimiter())) {
			flowLimiter.limit(support.getAccessKey(), support.getResourcePin(), support.getFlowLimiterLimitCount());
		}
		if (BooleanUtils.isTrue(support.getEnableDailyCountLimiter())) {
			dailyCountLimiter.limit(support.getAccessKey(), support.getResourcePin(),
					support.getDailyCountLimiterLimitCount());
		}
	}

	@Override
	public void unlimit(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext,
			T support) {
		if (null != support && BooleanUtils.isTrue(support.getEnableSyncLimiter())) {
			syncLimiter.unlimit(support.getAccessKey(), support.getResourcePin());
		}
	}

}

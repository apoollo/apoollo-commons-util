/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserRequestResourceMatcher;
import com.apoollo.commons.util.request.context.access.UserRequestResourceMatcher.UserMatchesRequestResourceCondition;
import com.apoollo.commons.util.request.context.limiter.CorsLimiter;
import com.apoollo.commons.util.request.context.limiter.FlowLimiter;
import com.apoollo.commons.util.request.context.limiter.IpLimiter;
import com.apoollo.commons.util.request.context.limiter.NonceLimiter;
import com.apoollo.commons.util.request.context.limiter.RefererLimiter;
import com.apoollo.commons.util.request.context.limiter.SignatureLimiter;
import com.apoollo.commons.util.request.context.limiter.SyncLimiter;
import com.apoollo.commons.util.request.context.limiter.TimeUnitPatternCountLimiter;

/**
 * @author liuyulong
 * @since 2025-06-12
 */
public class UseLimiters extends DefaultLimiters<User> {

	private UserRequestResourceMatcher requestResourceMatcher;

	public UseLimiters(NonceLimiter nonceLimiter, SignatureLimiter signatureLimter, CorsLimiter corsLimiter,
			IpLimiter ipLimiter, RefererLimiter refererLimiter, SyncLimiter syncLimiter, FlowLimiter flowLimiter,
			TimeUnitPatternCountLimiter timeUnitPatternCountLimiter,
			UserRequestResourceMatcher requestResourceMatcher) {
		super(nonceLimiter, signatureLimter, corsLimiter, ipLimiter, refererLimiter, syncLimiter, flowLimiter,
				timeUnitPatternCountLimiter);
		this.requestResourceMatcher = requestResourceMatcher;
	}

	@Override
	protected boolean enableEnableNonceLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableEnableNonceLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getNonceLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableSignatureLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableSignatureLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getSignatureLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableCorsLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableCorsLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getCorsLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableIpLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableIpLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getIpLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableRefererLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableRefererLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getRefererLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableSyncLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableSyncLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getSyncLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableFlowLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableFlowLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getFlowLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

	@Override
	protected boolean enableCountLimiter(RequestContext requestContext, User support) {
		boolean matches = false;
		if (matches = super.enableCountLimiter(requestContext, support)) {
			UserMatchesRequestResourceCondition condition = support.getCountLimiterCondition();
			if (null != condition) {
				matches = requestResourceMatcher.maches(condition, requestContext.getRequestResource());
			}
		}
		return matches;
	}

}

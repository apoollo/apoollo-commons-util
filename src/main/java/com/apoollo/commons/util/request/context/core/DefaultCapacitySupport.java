/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;

import com.apoollo.commons.util.redis.service.RedisNameSpaceKey.TimeUnitPattern;
import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.Instances;
import com.apoollo.commons.util.request.context.limiter.NonceValidator;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;
import com.apoollo.commons.util.request.context.limiter.core.DefaultLimitersSupport;
import com.apoollo.commons.util.request.context.limiter.support.CapacitySupport;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
@Getter
@Setter
public class DefaultCapacitySupport extends DefaultLimitersSupport implements CapacitySupport {

	private Boolean enableCapacity;
	private Boolean enableContentEscape;
	private EscapeMethod contentEscapeMethod;
	private Boolean enableResponseWrapper;
	private WrapResponseHandler wrapResponseHandler;

	@Getter
	@Setter
	public static class SerializebleCapacitySupport {
		private String resourcePin;
		private String accessKey;
		private Boolean enableNonceLimiter;
		private Long nonceLimiterDuration;
		private Boolean enableSignatureLimiter;
		private String signatureLimiterSecret;
		private List<String> signatureLimiterExcludeHeaderNames;
		private List<String> signatureLimiterIncludeHeaderNames;
		private Boolean enableCorsLimiter;
		private Boolean enableIpLimiter;
		private List<String> ipLimiterExcludes;
		private List<String> ipLimiterIncludes;
		private Boolean enableRefererLimiter;
		private List<String> refererLimiterIncludeReferers;
		private Boolean enableSyncLimiter;
		private Boolean enableFlowLimiter;
		private Long flowLimiterLimitCount;
		private Boolean enableCountLimiter;
		private TimeUnitPattern countLimiterTimeUnitPattern;
		private Long countLimiterLimitCount;
		private Boolean enableCapacity;
		private Boolean enableContentEscape;
		private Boolean enableResponseWrapper;

		//
		private Class<? extends EscapeMethod> contentEscapeMethodClass;
		private Class<? extends CorsConfiguration> corsLimiterConfigurationClass;
		private Class<? extends NonceValidator> nonceLimiterValidatorClass;
		private Class<? extends WrapResponseHandler> wrapResponseHandlerClass;

	}

	public static CapacitySupport toCapacitySupport(Instances instances, SerializebleCapacitySupport source) {
		DefaultCapacitySupport capacitySupport = new DefaultCapacitySupport();
		evlaute(instances, source, capacitySupport);
		return capacitySupport;
	}

	public static void evlaute(Instances instances, SerializebleCapacitySupport source, DefaultCapacitySupport target) {
		target.setResourcePin(source.getResourcePin());
		target.setAccessKey(source.getAccessKey());
		target.setEnableNonceLimiter(source.getEnableNonceLimiter());
		target.setNonceLimiterDuration(source.getNonceLimiterDuration());
		target.setEnableSignatureLimiter(source.getEnableSignatureLimiter());
		target.setSignatureLimiterSecret(source.getSignatureLimiterSecret());
		target.setSignatureLimiterExcludeHeaderNames(source.getSignatureLimiterExcludeHeaderNames());
		target.setSignatureLimiterIncludeHeaderNames(source.getSignatureLimiterIncludeHeaderNames());
		target.setEnableCorsLimiter(source.getEnableCorsLimiter());
		target.setEnableIpLimiter(source.getEnableIpLimiter());
		target.setIpLimiterExcludes(source.getIpLimiterExcludes());
		target.setIpLimiterIncludes(source.getIpLimiterIncludes());
		target.setEnableRefererLimiter(source.getEnableRefererLimiter());
		target.setRefererLimiterIncludeReferers(source.getRefererLimiterIncludeReferers());
		target.setEnableSyncLimiter(source.getEnableSyncLimiter());
		target.setEnableFlowLimiter(source.getEnableFlowLimiter());
		target.setFlowLimiterLimitCount(source.getFlowLimiterLimitCount());
		target.setEnableCountLimiter(source.getEnableCountLimiter());
		target.setCountLimiterTimeUnitPattern(source.getCountLimiterTimeUnitPattern());
		target.setCountLimiterLimitCount(source.getCountLimiterLimitCount());
		target.setEnableCapacity(source.getEnableCapacity());
		target.setEnableContentEscape(source.getEnableContentEscape());
		target.setEnableResponseWrapper(source.getEnableResponseWrapper());

		if (null != source.getContentEscapeMethodClass()) {
			target.setContentEscapeMethod(instances.getEscapeMethod(source.getContentEscapeMethodClass()));
		}
		if (null != source.getCorsLimiterConfigurationClass()) {
			target.setCorsLimiterConfiguration(
					instances.getCorsConfiguration(source.getCorsLimiterConfigurationClass()));
		}
		if (null != source.getNonceLimiterValidatorClass()) {
			target.setNonceLimiterValidator(instances.getNonceValidator(source.getNonceLimiterValidatorClass()));
		}
		if (null != source.getWrapResponseHandlerClass()) {
			target.setWrapResponseHandler(instances.getWrapResponseHandler(source.getWrapResponseHandlerClass()));
		}
	}

	public static void evlaute(CapacitySupport source, SerializebleCapacitySupport target) {
		target.setResourcePin(source.getResourcePin());
		target.setAccessKey(source.getAccessKey());
		target.setEnableNonceLimiter(source.getEnableNonceLimiter());
		target.setNonceLimiterDuration(source.getNonceLimiterDuration());
		target.setEnableSignatureLimiter(source.getEnableSignatureLimiter());
		target.setSignatureLimiterSecret(source.getSignatureLimiterSecret());
		target.setSignatureLimiterExcludeHeaderNames(source.getSignatureLimiterExcludeHeaderNames());
		target.setSignatureLimiterIncludeHeaderNames(source.getSignatureLimiterIncludeHeaderNames());
		target.setEnableCorsLimiter(source.getEnableCorsLimiter());
		target.setEnableIpLimiter(source.getEnableIpLimiter());
		target.setIpLimiterExcludes(source.getIpLimiterExcludes());
		target.setIpLimiterIncludes(source.getIpLimiterIncludes());
		target.setEnableRefererLimiter(source.getEnableRefererLimiter());
		target.setRefererLimiterIncludeReferers(source.getRefererLimiterIncludeReferers());
		target.setEnableSyncLimiter(source.getEnableSyncLimiter());
		target.setEnableFlowLimiter(source.getEnableFlowLimiter());
		target.setFlowLimiterLimitCount(source.getFlowLimiterLimitCount());
		target.setEnableCountLimiter(source.getEnableCountLimiter());
		target.setCountLimiterTimeUnitPattern(source.getCountLimiterTimeUnitPattern());
		target.setCountLimiterLimitCount(source.getCountLimiterLimitCount());
		target.setEnableCapacity(source.getEnableCapacity());
		target.setEnableContentEscape(source.getEnableContentEscape());
		target.setEnableResponseWrapper(source.getEnableResponseWrapper());
		if (null != source.getContentEscapeMethod()) {
			target.setContentEscapeMethodClass(source.getContentEscapeMethod().getClass());
		}
		if (null != source.getCorsLimiterConfiguration()) {
			target.setCorsLimiterConfigurationClass(source.getCorsLimiterConfiguration().getClass());
		}
		if (null != source.getNonceLimiterValidator()) {
			target.setNonceLimiterValidatorClass(source.getNonceLimiterValidator().getClass());
		}
		if (null != source.getWrapResponseHandler()) {
			target.setWrapResponseHandlerClass(source.getWrapResponseHandler().getClass());
		}
	}
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.request.context.CapacitySupport;
import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.WrapResponseHandler;
import com.apoollo.commons.util.request.context.limiter.core.DefaultLimitersSupport;
import com.apoollo.commons.util.web.spring.Instance;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
@Getter
@Setter
public class DefaultCapacitySupport extends DefaultLimitersSupport implements CapacitySupport {

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
		private Boolean enableDailyCountLimiter;
		private Long dailyCountLimiterLimitCount;
		private Boolean enableContentEscape;
		private Boolean enableResponseWrapper;

		private String contentEscapeMethodClass;
		private String corsLimiterConfigurationClass;
		private String nonceLimiterValidatorClass;
		private String wrapResponseHandlerClass;

	}

	public static void evlaute(Instance instance, SerializebleCapacitySupport source, DefaultCapacitySupport target) {
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
		target.setEnableDailyCountLimiter(source.getEnableDailyCountLimiter());
		target.setDailyCountLimiterLimitCount(source.getDailyCountLimiterLimitCount());
		target.setEnableContentEscape(source.getEnableContentEscape());
		target.setEnableResponseWrapper(source.getEnableResponseWrapper());

		if (StringUtils.isNotBlank(source.getContentEscapeMethodClass())) {
			target.setContentEscapeMethod(instance.getInstance(source.getContentEscapeMethodClass()));
		}
		if (StringUtils.isNotBlank(source.getCorsLimiterConfigurationClass())) {
			target.setCorsLimiterConfiguration(instance.getInstance(source.getCorsLimiterConfigurationClass()));
		}
		if (StringUtils.isNotBlank(source.getNonceLimiterValidatorClass())) {
			target.setNonceLimiterValidator(instance.getInstance(source.getNonceLimiterValidatorClass()));
		}
		if (StringUtils.isNotBlank(source.getWrapResponseHandlerClass())) {
			target.setWrapResponseHandler(instance.getInstance(source.getWrapResponseHandlerClass()));
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
		target.setEnableDailyCountLimiter(source.getEnableDailyCountLimiter());
		target.setDailyCountLimiterLimitCount(source.getDailyCountLimiterLimitCount());
		target.setEnableContentEscape(source.getEnableContentEscape());
		target.setEnableResponseWrapper(source.getEnableResponseWrapper());
		if (null != source.getContentEscapeMethod()) {
			target.setContentEscapeMethodClass(source.getContentEscapeMethod().getClass().getName());
		}
		if (null != source.getCorsLimiterConfiguration()) {
			target.setCorsLimiterConfigurationClass(source.getCorsLimiterConfiguration().getClass().getName());
		}
		if (null != source.getNonceLimiterValidator()) {
			target.setNonceLimiterValidatorClass(source.getNonceLimiterValidator().getClass().getName());
		}
		if (null != source.getWrapResponseHandler()) {
			target.setWrapResponseHandlerClass(source.getWrapResponseHandler().getClass().getName());
		}
	}
}

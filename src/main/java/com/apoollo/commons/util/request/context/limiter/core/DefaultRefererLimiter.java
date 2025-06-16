/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.apoollo.commons.util.UrlUtils;
import com.apoollo.commons.util.UrlUtils.Url;
import com.apoollo.commons.util.exception.refactor.AppRefererLimiterRefusedException;
import com.apoollo.commons.util.request.context.limiter.RefererLimiter;
import com.apoollo.commons.util.request.context.model.RequestConstants;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultRefererLimiter implements RefererLimiter {

	@Override
	public void limit(HttpServletRequest request, List<String> refererLimiterIncludeReferers) {
		if (CollectionUtils.isNotEmpty(refererLimiterIncludeReferers)) {
			String referer = request.getHeader(RequestConstants.REQUEST_HEADER_REFERER);
			Url url = UrlUtils.parse(referer);
			if (null != url) {
				String domain = url.getDomain();
				refererLimiterIncludeReferers.stream().forEach(includeReferer -> {
					boolean matches = false;
					if (includeReferer.startsWith("*")) {
						matches = domain.endsWith(includeReferer.substring(1));
					} else {
						matches = domain.equals(includeReferer);
					}
					if (!matches) {
						throw new AppRefererLimiterRefusedException(
								"header [" + RequestConstants.REQUEST_HEADER_REFERER + "]  not allowed");
					}
				});
			}
		}
	}

}

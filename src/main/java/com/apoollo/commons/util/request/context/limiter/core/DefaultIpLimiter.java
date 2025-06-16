/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.exception.refactor.AppIpLimiterExcludeListRefusedException;
import com.apoollo.commons.util.exception.refactor.AppIpLimiterIncludeListRefusedException;
import com.apoollo.commons.util.request.context.limiter.IpLimiter;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultIpLimiter implements IpLimiter {

	public void limit(List<String> ipLimiterExcludes, List<String> ipLimiterIncludes, String requestIp) {

		if (maches(ipLimiterExcludes, requestIp)) {
			throw new AppIpLimiterExcludeListRefusedException("request ip disabled by exclude ip list : " + requestIp);
		}
		if (CollectionUtils.isNotEmpty(ipLimiterIncludes) && !maches(ipLimiterIncludes, requestIp)) {
			throw new AppIpLimiterIncludeListRefusedException("request ip disabled by include ip list : " + requestIp);
		}
	}

	private boolean maches(List<String> list, String input) {
		return LangUtils.getStream(list).filter(whiteIp -> {
			boolean accessed = true;
			if (whiteIp.endsWith("*")) {
				String startWith = whiteIp.substring(0, whiteIp.length() - 1);
				accessed = input.startsWith(startWith);
			} else {
				accessed = whiteIp.equals(input);
			}
			return accessed;

		}).findFirst().isPresent();
	}
}

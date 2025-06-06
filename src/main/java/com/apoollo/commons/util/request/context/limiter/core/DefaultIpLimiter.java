/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.exception.detailed.IpLimterException;
import com.apoollo.commons.util.request.context.limiter.IpLimiter;
import com.apoollo.commons.util.request.context.limiter.support.IpLimiterSupport;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultIpLimiter implements IpLimiter {

	public void limit(IpLimiterSupport ipLimterSupport, String requestIp) {

		if (maches(ipLimterSupport.getIpLimiterExcludes(), requestIp)) {
			throw new IpLimterException("request ip disabled by  black ip list : " + requestIp);
		}
		if (CollectionUtils.isNotEmpty(ipLimterSupport.getIpLimiterIncludes())
				&& !maches(ipLimterSupport.getIpLimiterIncludes(), requestIp)) {
			throw new IpLimterException("request ip disabled by white ip list : " + requestIp);
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

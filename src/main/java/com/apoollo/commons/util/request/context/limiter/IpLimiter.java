/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import java.util.List;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface IpLimiter {

	public void limit(List<String> ipLimiterExcludes,List<String> ipLimiterIncludes, String requestIp);
}

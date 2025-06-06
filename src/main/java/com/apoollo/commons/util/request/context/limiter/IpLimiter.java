/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import com.apoollo.commons.util.request.context.limiter.support.IpLimiterSupport;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public interface IpLimiter {

	public void limit(IpLimiterSupport ipLimterSupport, String requestIp);
}

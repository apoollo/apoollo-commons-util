/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
public interface LimitersSupport extends NonceLimiterSupport, SignatureLimiterSupport, CorsLimiterSupport,
		IpLimiterSupport, RefererLimiterSupport, SyncLimiterSupport, FlowLimiterSupport, CountLimiterSupport {

	
}

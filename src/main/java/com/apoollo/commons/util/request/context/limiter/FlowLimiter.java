/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

/**
 * @author liuyulong
 */
public interface FlowLimiter {
	
	public default void limit(String resourcePin, Long limitCount) {
		limit(null, resourcePin, limitCount);
	}

	public void limit(String accessKey, String resourcePin, Long limitCount);
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

import com.apoollo.commons.util.request.context.ContentEscapeMethod;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
public interface CapacitySupport extends LimitersSupport {

	public Boolean getEnableCapacity();

	public Boolean getEnableContentEscape();

	public ContentEscapeMethod getContentEscapeMethod();

	public Boolean getEnableResponseWrapper();

	public WrapResponseHandler getWrapResponseHandler();

	
}

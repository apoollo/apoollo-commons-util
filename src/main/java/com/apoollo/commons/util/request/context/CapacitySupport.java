/**
 * 
 */
package com.apoollo.commons.util.request.context;

import com.apoollo.commons.util.request.context.limiter.support.LimitersSupport;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
public interface CapacitySupport extends LimitersSupport {

	public Boolean getEnableContentEscape();

	public EscapeMethod getContentEscapeMethod();

	public Boolean getEnableResponseWrapper();

	public WrapResponseHandler getWrapResponseHandler();
}

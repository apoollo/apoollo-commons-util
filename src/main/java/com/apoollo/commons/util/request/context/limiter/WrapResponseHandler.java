/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import com.apoollo.commons.util.request.context.RequestContext;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public interface WrapResponseHandler {

	public Object writeAndGetExceptionResponse(HttpServletResponse response, RequestContext requestContext, Exception ex);

	public Object getNormallyResponse(RequestContext requestContext, Object object);

}

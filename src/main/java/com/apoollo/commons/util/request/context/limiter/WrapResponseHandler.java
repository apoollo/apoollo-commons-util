/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

import java.util.Map;

import com.apoollo.commons.util.request.context.RequestContext;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public interface WrapResponseHandler {

	public void writeExceptionResponse(HttpServletResponse response, RequestContext requestContext, Exception ex);

	public Map<String, Object> getNormallyResponse(RequestContext requestContext, Object object);

}

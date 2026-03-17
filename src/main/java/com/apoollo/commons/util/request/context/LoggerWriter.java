/**
 * 
 */
package com.apoollo.commons.util.request.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface LoggerWriter {

	public void write(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext);

}

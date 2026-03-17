/**
 * 
 */
package com.apoollo.commons.util.request.context;

import org.springframework.core.Ordered;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContextDataBus extends Ordered {

	public void transport(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext);

	public boolean asyncSupport();

}

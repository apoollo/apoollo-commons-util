/**
 * 
 */
package com.apoollo.commons.util.request.context;

import org.springframework.core.Ordered;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContextDataBus extends Ordered {

	public void transport(HttpServletRequest request, RequestContext requestContext);
	
	public boolean asyncSupport();

}

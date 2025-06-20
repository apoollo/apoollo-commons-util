/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.exception.AppRequestResourceDisabledException;
import com.apoollo.commons.util.exception.AppRequestResourceNotExistsException;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.RequestResourceManager;
import com.apoollo.commons.util.request.context.access.SecurePrincipal;
import com.apoollo.commons.util.request.context.limiter.Limiters;
import com.apoollo.commons.util.request.context.limiter.support.LimitersSupport;
import com.apoollo.commons.util.request.context.model.RequestContextCapacitySupport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * liuyulong
 */
public class SecureRequestResource implements SecurePrincipal<RequestResource> {

	private RequestResourceManager requestResourceManager;
	private Limiters<LimitersSupport> limiters;

	public SecureRequestResource(RequestResourceManager requestResourceManager, Limiters<LimitersSupport> limiters) {
		super();
		this.requestResourceManager = requestResourceManager;
		this.limiters = limiters;
	}

	@Override
	public RequestResource init(HttpServletRequest request, HttpServletResponse response,
			RequestContext requestContext) {
		String requestMappingPath = requestContext.getRequestMappingPath();
		RequestResource requestResource = requestResourceManager.getRequestResource(requestMappingPath);
		if (null == requestResource) {
			throw new AppRequestResourceNotExistsException("can't find requestResource - " + requestMappingPath);
		}
		requestContext.setRequestResource(requestResource);
		if (BooleanUtils.isNotTrue(requestResource.getEnable())) {
			throw new AppRequestResourceDisabledException("requestResource disabled - " + requestMappingPath);
		}
		RequestContextCapacitySupport.doSupport(List.of(requestResource), capacitySupport -> {
			limiters.limit(request, response, requestContext, capacitySupport);
		});
		return requestResource;
	}

}

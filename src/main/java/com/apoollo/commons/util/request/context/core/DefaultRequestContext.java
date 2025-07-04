/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.HashMap;
import java.util.Map;

import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.User;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public class DefaultRequestContext implements RequestContext {

	private String clientRequestId;

	private String clientRealIp;

	private String requestId;

	private Long requestTime;

	private String contextPath;

	private String requestUri;

	private String requestMappingPath;

	private RequestResource requestResource;

	private User requestUser;

	private byte[] requestBody;

	private Long responseTime;

	private Object responseBody;

	private Map<String, Object> hint = new HashMap<>();

	@Override
	public String getClientRequestId() {
		return clientRequestId;
	}

	@Override
	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}

	@Override
	public String getClientRealIp() {
		return clientRealIp;
	}

	@Override
	public void setClientRealIp(String clientRealIp) {
		this.clientRealIp = clientRealIp;
	}

	@Override
	public Long getRequestTime() {
		return requestTime;
	}

	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public void set(String requestId, Long requestTime, String contextPath, String requestUri) {
		this.requestId = requestId;
		this.requestTime = requestTime;
		this.contextPath = contextPath;
		this.requestUri = requestUri;
		requestMappingPath = RequestContext.getRequestMappingPath(contextPath, requestUri);
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	@Override
	public String getRequestUri() {
		return requestUri;
	}

	@Override
	public String getRequestMappingPath() {
		return requestMappingPath;
	}

	@Override
	public RequestResource getRequestResource() {
		return requestResource;
	}

	@Override
	public void setRequestResource(RequestResource requestResource) {
		this.requestResource = requestResource;
	}

	@Override
	public User getRequestUser() {
		return requestUser;
	}

	@Override
	public void setRequestUser(User user) {
		this.requestUser = user;

	}

	@Override
	public byte[] getRequestBody() {
		return requestBody;
	}

	@Override
	public void setRequestBody(byte[] requestBody) {
		this.requestBody = requestBody;
	}

	@Override
	public Long getResponseTime() {
		return responseTime;
	}

	@Override
	public void setResponseTime(Long endTime) {
		this.responseTime = endTime;
	}

	@Override
	public Object getResponseBody() {
		return responseBody;
	}

	@Override
	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public Long getElapsedTime() {
		if (null == responseTime) {
			throw new RuntimeException("endTime not init");
		}
		return responseTime - requestTime;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getHint(String key) {
		return (T) hint.get(key);
	}

	@Override
	public void addHint(String key, Object value) {
		if (null != key && null != value) {
			hint.put(key, value);
		}
	}

}

/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.RequestResource;
import com.apoollo.commons.util.request.context.Response;
import com.apoollo.commons.util.request.context.User;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public class DefaultRequestContext implements RequestContext {

	private Long requestTime;

	private String requestId;

	private String requestIp;

	private String clientRequestId;

	private Long clientTimeout;

	private Long requestTimeout;

	private String contextPath;

	private String requestUri;

	private String requestMappingPath;

	private User user;

	private RequestResource requestResource;

	private Long responseTime;

	private Response<?> response;

	private ThreadPoolExecutor threadPoolExecutor;

	private String requestServerName;

	private Map<String, Object> hint = new HashMap<>();

	private byte[] requestBody;
	
	/**
	 * @return the startTime
	 */
	@Override
	public Long getRequestTime() {
		return requestTime;
	}

	/**
	 * @return the requestId
	 */
	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @return the requestUri
	 */
	@Override
	public String getRequestUri() {
		return requestUri;
	}

	@Override
	public String getRequestMappingPath() {
		return requestMappingPath;
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
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;

	}

	/**
	 * @return the requestResource
	 */
	public RequestResource getRequestResource() {
		return requestResource;
	}

	/**
	 * @param requestResource the requestResource to set
	 */
	public void setRequestResource(RequestResource requestResource) {
		this.requestResource = requestResource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T getCastUser() {
		return (T) getUser();
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
	public void setResponse(Response<?> response) {
		this.response = response;
	}

	@Override
	public Long getElapsedTime() {
		if (null == responseTime) {
			throw new RuntimeException("endTime not init");
		}
		return responseTime - requestTime;
	}

	@Override
	public Response<?> getResponse() {
		return response;
	}

	@Override
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	@Override
	public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;

	}

	@Override
	public String getClientRequestId() {
		return clientRequestId;
	}

	@Override
	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}

	@Override
	public Long getClientTimeout() {
		return clientTimeout;
	}

	@Override
	public Long getRequestTimeout() {
		return requestTimeout;
	}

	@Override
	public void setClientTimeout(Long clientTimeout) {
		this.clientTimeout = clientTimeout;

	}

	@Override
	public void setRequestTimeout(Long requestTimeout) {
		this.requestTimeout = requestTimeout;

	}

	@Override
	public String getRequestIp() {
		return requestIp;
	}

	@Override
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	@Override
	public String getRequestServerName() {
		return requestServerName;
	}

	@Override
	public void setRequestServerName(String requestServerName) {
		this.requestServerName = requestServerName;
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

	public byte[] getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(byte[] requestBody) {
		this.requestBody = requestBody;
	}

}

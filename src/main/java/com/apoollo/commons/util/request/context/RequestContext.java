/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.MdcUtils;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.core.DefaultRequestContext;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContext {

	public static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

	public static RequestContext reset(String requestId, String contextPath, String requestUri) {
		return reset(requestId, contextPath, requestUri, DefaultRequestContext::new);
	}

	public static <T extends RequestContext> T reset(T context) {
		MdcUtils.addAll(context.getRequestId());
		CONTEXT.set(context);
		return context;
	}

	public static <T extends RequestContext> T reset(String requestId, String contextPath, String requestUri,
			Supplier<T> supplier) {
		T requestContext = supplier.get();
		requestContext.set(requestId, System.currentTimeMillis(), contextPath, requestUri);
		MdcUtils.addAll(requestId);
		CONTEXT.set(requestContext);
		return requestContext;
	}

	public static RequestContext release() {
		RequestContext requestContext = CONTEXT.get();
		CONTEXT.remove();
		MdcUtils.releaseAll();
		return requestContext;
	}

	public static RequestContext getRequired() {
		return getRequired(DefaultRequestContext.class);
	}

	public static <T extends RequestContext> T getRequired(Class<T> clazz) {
		T requestContext = get(clazz);
		if (null == requestContext) {
			throw new RuntimeException("request context must not be null");
		}
		return requestContext;
	}

	public static RequestContext get() {
		return get(DefaultRequestContext.class);
	}

	@SuppressWarnings("unchecked")
	public static <T extends RequestContext> T get(Class<T> clazz) {
		RequestContext requestContext = CONTEXT.get();
		return null == requestContext ? null : (T) requestContext;
	}

	public static String getRequestMappingPath(String contextPath, String requestUri) {
		String requestMappingPath = null;
		if (StringUtils.isBlank(contextPath) || "/".equals(contextPath)) {
			requestMappingPath = requestUri;
		} else {
			requestMappingPath = StringUtils.substringAfter(requestUri, contextPath);
		}
		return requestMappingPath;
	}

	public default <T> T getHintOfExceptionCatchedData() {
		return getHint("exception.catched.data");
	}

	public default void setHintOfExceptionCatcheData(Object object) {
		addHint("exception.catched.data", object);
	}

	// getters setters

	public String getClientRequestId();

	public void setClientRequestId(String clientRequestId);

	public String getClientRealIp();

	public void setClientRealIp(String clientRealIp);

	public String getRequestId();

	public Long getRequestTime();

	public void set(String requestId, Long requestTime, String contextPath, String requestUri);

	public String getContextPath();

	public String getRequestUri();

	public String getRequestMappingPath();

	public RequestResource getRequestResource();

	public void setRequestResource(RequestResource requestResource);

	public User getRequestUser();

	public void setRequestUser(User user);

	public byte[] getRequestBody();

	public void setRequestBody(byte[] requestBody);

	public Long getResponseTime();

	public void setResponseTime(Long endTime);

	public Object getResponseBody();

	public void setResponseBody(Object response);

	public Long getElapsedTime();

	public <T> T getHint(String key);

	public void addHint(String key, Object value);

}

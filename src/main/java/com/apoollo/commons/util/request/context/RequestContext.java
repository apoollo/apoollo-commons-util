/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.concurrent.ThreadPoolExecutor;
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

	public String getClientRequestId();

	public String getRequestId();

	public String getRequestIp();

	public Long getRequestTime();

	public String getContextPath();

	public String getRequestUri();

	public String getRequestMappingPath();

	public User getUser();

	public RequestResource getRequestResource();

	public Long getResponseTime();

	public Long getElapsedTime();

	public Response<?> getResponse();

	public <T extends User> T getCastUser();

	public ThreadPoolExecutor getThreadPoolExecutor();

	public Long getClientTimeout();

	public Long getRequestTimeout();

	public String getRequestServerName();

	public <T> T getHint(String key);

	public byte[] getRequestBody();

	public void setRequestIp(String requestIp);

	public void setClientRequestId(String clientRequestId);

	public void setUser(User user);

	public void setRequestResource(RequestResource requestResource);

	public void setResponseTime(Long endTime);

	public void set(String requestId, Long requestTime, String contextPath, String requestUri);

	public void setResponse(Response<?> response);

	public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor);

	public void setClientTimeout(Long clientTimeout);

	public void setRequestTimeout(Long requestTimeout);

	public void setRequestServerName(String requestServerName);

	public void addHint(String key, Object value);

	public void setRequestBody(byte[] requestBody);

}

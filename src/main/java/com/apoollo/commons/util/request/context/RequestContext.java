/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.apoollo.commons.util.MdcUtils;
import com.apoollo.commons.util.exception.AppForbbidenException;
import com.apoollo.commons.util.exception.AppHttpCodeMessageException;
import com.apoollo.commons.util.request.context.def.AccessStrategy;
import com.apoollo.commons.util.request.context.def.DefaultHttpCodeNameHandler;
import com.apoollo.commons.util.request.context.def.DefaultRequestContext;
import com.apoollo.commons.util.request.context.def.PrivateRequestResourceAccessStrategy;
import com.apoollo.commons.util.request.context.def.PublicRequestResourceAccessStrategy;
import com.apoollo.commons.util.request.context.model.Authorized;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContext {

	public static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

	public static final HttpCodeNameHandler DEFAULT_HTTP_CODE_NAME_HANDLER = new DefaultHttpCodeNameHandler();

	public static final Map<String, RequestResourceAccessStrategy> REQUEST_RESOUCE_ACCESS_STRATEGY = new ConcurrentHashMap<String, RequestResourceAccessStrategy>() {
		private static final long serialVersionUID = -5404055407185446469L;

		{
			put(AccessStrategy.PRIVATE_REQUEST.getAccessStrategyPin(), new PrivateRequestResourceAccessStrategy());
			put(AccessStrategy.PUBLIC_REQUEST.getAccessStrategyPin(), new PublicRequestResourceAccessStrategy());
		}
	};

	public default RequestResourceAccessStrategy getRequestResourceAccessStrategy() {
		RequestResourceAccessStrategy requestResourceAccessStrategy = null;
		RequestResource requestResource = getRequestResource();
		if (null != requestResource) {
			String accessStrategyKey = requestResource.getAccessStrategy();
			requestResourceAccessStrategy = REQUEST_RESOUCE_ACCESS_STRATEGY.get(accessStrategyKey);
			if (null == requestResourceAccessStrategy
					&& AccessStrategy.CUSTOMIZE.getAccessStrategyPin().equals(accessStrategyKey)) {
				Class<? extends RequestResourceAccessStrategy> clazz = requestResource
						.getCustomizeAccessStrategyClass();
				if (null == clazz || PrivateRequestResourceAccessStrategy.class == clazz
						|| PublicRequestResourceAccessStrategy.class == clazz) {
					throw new RuntimeException("customAccessStrategyClass must set customize:" + accessStrategyKey);
				}
				try {
					synchronized (REQUEST_RESOUCE_ACCESS_STRATEGY) {
						requestResourceAccessStrategy = REQUEST_RESOUCE_ACCESS_STRATEGY.get(accessStrategyKey);
						if (null == requestResourceAccessStrategy) {
							requestResourceAccessStrategy = clazz.getDeclaredConstructor().newInstance();
							REQUEST_RESOUCE_ACCESS_STRATEGY.put(accessStrategyKey, requestResourceAccessStrategy);
						}
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return requestResourceAccessStrategy;
	}

	public default RequestResourceAccessStrategy getDefaultResourceAccessStrategy() {
		RequestResourceAccessStrategy requestResourceAccessStrategy = getRequestResourceAccessStrategy();
		if (null == requestResourceAccessStrategy) {
			requestResourceAccessStrategy = new PrivateRequestResourceAccessStrategy();
		}
		return requestResourceAccessStrategy;
	}

	public default RequestResourceAccessStrategy getResourceAccessStrategyRequired() {
		RequestResourceAccessStrategy requestResourceAccessStrategy = getRequestResourceAccessStrategy();
		Validate.notNull(requestResourceAccessStrategy, "resourceAccessStrategy must not be null");
		return requestResourceAccessStrategy;
	}

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
			throw new AppForbbidenException("request context must not be null");
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
		String requestPath = null;
		if (StringUtils.isBlank(contextPath) || contextPath.equals("/")) {
			requestPath = requestUri;
		} else {
			requestPath = StringUtils.substringAfter(requestUri, contextPath);
		}
		return requestPath;
	}

	public static void validateDataPermission(String loginAccessKey, Supplier<String> message) {
		RequestContext requestContext = RequestContext.getRequired();
		String accessKey = RequestContext.getRequired().getUser().getAccessKey();
		if (!StringUtils.equals(loginAccessKey, accessKey)) {
			throw new AppHttpCodeMessageException(
					requestContext.getResourceAccessStrategyRequired().getHttpCodeNameHandler().getForbbiden(),
					new String[] { message.get() });
		}
	}

	public static void validateDataPermission(String loginAccessKey) {
		validateDataPermission(loginAccessKey, () -> "数据权限越界");
	}

	@SuppressWarnings("unchecked")
	public default <T> T getAuthorizedValue(Class<T> clazz) {
		T result = null;
		Authorized<?> authorized = getAuthorized();
		if (null != authorized) {
			Object authorizedValue = authorized.getAuthorizedValue();
			if (null != authorizedValue && clazz.isAssignableFrom(authorizedValue.getClass())) {
				result = (T) authorizedValue;
			}
		}
		return result;
	}

	public default <T> T getAuthorizedValueRequired(Class<T> clazz) {
		T result = getAuthorizedValue(clazz);
		if (null == result) {
			throw new ClassCastException("authorizedValue cast faild");
		}
		return result;
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

	public Authorized<?> getAuthorized();

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

	public String getDailyMaximumUseTimesLimitKey();

	public Boolean getResponseIsChargeForUseTimesLimit();

	public Object getRequestBody();

	public void setRequestIp(String requestIp);

	public void setClientRequestId(String clientRequestId);

	public void setUser(User user);

	public void setAuthorized(Authorized<?> authorized);

	public void setRequestResource(RequestResource requestResource);

	public void setResponseTime(Long endTime);

	public void set(String requestId, Long requestTime, String contextPath, String requestUri);

	public void beforeBodyWrite(Response<?> response);

	public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor);

	public void setClientTimeout(Long clientTimeout);

	public void setRequestTimeout(Long requestTimeout);

	public void setRequestServerName(String requestServerName);

	public void addHint(String key, Object value);

	public void setDailyMaximumUseTimesLimitKey(String dailyMaximumUseTimesLimitKey);

	public void setResponseIsChargeForUseTimesLimit(Boolean responseIsChargeForUseTimesLimit);

	public void setRequestBody(Object requestBody);

}

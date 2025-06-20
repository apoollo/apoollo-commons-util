/**
 * 
 */
package com.apoollo.commons.util.request.context.model;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;
import com.apoollo.commons.util.request.context.limiter.support.CapacitySupport;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2025-06-10
 */
public class RequestContextCapacitySupport {

	private EscapeMethod escapeMethod;
	private WrapResponseHandler wrapResponseHandler;
	private CapacitySupport capacitySupport;

	public RequestContextCapacitySupport(EscapeMethod escapeMethod, WrapResponseHandler wrapResponseHandler,
			CapacitySupport capacitySupport) {
		super();
		this.escapeMethod = escapeMethod;
		this.wrapResponseHandler = wrapResponseHandler;
		this.capacitySupport = capacitySupport;
		if (BooleanUtils.isTrue(capacitySupport.getEnableCapacity())) {
			if (StringUtils.isBlank(capacitySupport.getResourcePin())) {
				throw new RuntimeException("platform capacitySupport resourcePin must not be blank");
			}
		}
	}

	public EscapeMethod getEscapeMethod(RequestContext requestContext) {
		EscapeMethod escapeMethod = getAbility(requestContext, capacitySupport,
				CapacitySupport::getContentEscapeMethod);
		if (null == escapeMethod) {
			escapeMethod = this.escapeMethod;
		}
		return escapeMethod;
	}

	public WrapResponseHandler getWrapResponseHandler(RequestContext requestContext) {
		WrapResponseHandler wrapResponseHandler = getAbility(requestContext, capacitySupport,
				CapacitySupport::getWrapResponseHandler);
		if (null == wrapResponseHandler) {
			wrapResponseHandler = this.wrapResponseHandler;
		}
		return wrapResponseHandler;
	}

	public Object getNormallyResponse(RequestContext requestContext, Object object) {
		Object result = null;
		if (null != requestContext
				&& supportAbility(requestContext, capacitySupport, CapacitySupport::getEnableResponseWrapper)) {
			requestContext.setResponseTime(System.currentTimeMillis());
			result = getWrapResponseHandler(requestContext).getNormallyResponse(requestContext, object);
		}
		return result;
	}

	public <T> T writeExceptionResponse(HttpServletResponse response, RequestContext requestContext, Exception ex,
			Supplier<T> supplier) {
		T result = null;
		if (!response.isCommitted() && null != requestContext
				&& supportAbility(requestContext, capacitySupport, CapacitySupport::getEnableResponseWrapper)) {
			requestContext.setResponseTime(System.currentTimeMillis());
			getWrapResponseHandler(requestContext).writeExceptionResponse(response, requestContext, ex);
			result = supplier.get();
		}
		return result;
	}

	public boolean supportAbility(RequestContext requestContext, Function<CapacitySupport, Boolean> function) {
		return supportAbility(requestContext, capacitySupport, function);
	}

	public void doSupport(Consumer<CapacitySupport> consumer) {
		doSupport(List.of(capacitySupport), consumer);
	}

	public void doSupport(RequestContext requestContext, Consumer<CapacitySupport> consumer) {
		doSupport(LangUtils.getStream(capacitySupport, requestContext.getUser(), requestContext.getRequestResource())
				.toList(), consumer);
	}

	public static <T extends CapacitySupport> void doSupport(List<T> capacitySupports, Consumer<T> consumer) {
		capacitySupports//
				.stream()//
				.filter(Objects::nonNull)//
				.filter(support -> BooleanUtils.isTrue(support.getEnableCapacity()))//
				.forEach(consumer);
	}

	public static boolean supportAbility(RequestContext requestContext, CapacitySupport capacitySupport,
			Function<CapacitySupport, Boolean> function) {
		return getAbilityStream(LangUtils
				.getStream(requestContext.getUser(), requestContext.getRequestResource(), capacitySupport).toList(),
				function)//
				.filter(BooleanUtils::isTrue)//
				.findAny()//
				.orElse(false)//
		;
	}

	public static <T> T getAbility(RequestContext requestContext, CapacitySupport capacitySupport,
			Function<CapacitySupport, T> function) {
		return getAbility(LangUtils
				.getStream(requestContext.getUser(), requestContext.getRequestResource(), capacitySupport).toList(),
				function);
	}

	public static <T> T getAbility(List<CapacitySupport> capacitySupports, Function<CapacitySupport, T> function) {
		return getAbilityStream(capacitySupports, function)//
				.filter(Objects::nonNull)//
				.findAny()//
				.orElse(null)//
		;
	}

	public static <T> Stream<T> getAbilityStream(List<CapacitySupport> capacitySupports,
			Function<CapacitySupport, T> function) {
		return capacitySupports//
				.stream()//
				.filter(Objects::nonNull)//
				.filter(support -> BooleanUtils.isTrue(support.getEnableCapacity()))//
				.map(support -> function.apply(support))//
		;
	}

}

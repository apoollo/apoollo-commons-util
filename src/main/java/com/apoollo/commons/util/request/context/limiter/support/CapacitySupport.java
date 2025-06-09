/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
public interface CapacitySupport extends LimitersSupport {

	public Boolean getEnableCapacity();

	public Boolean getEnableContentEscape();

	public EscapeMethod getContentEscapeMethod();

	public Boolean getEnableResponseWrapper();

	public WrapResponseHandler getWrapResponseHandler();

	public static boolean support(RequestContext requestContext, CapacitySupport capacitySupport,
			Function<CapacitySupport, Boolean> function) {
		return support(List.of(capacitySupport, requestContext.getRequestResource(), requestContext.getUser()),
				function);
	}

	public static boolean support(List<CapacitySupport> capacitySupports, Function<CapacitySupport, Boolean> function) {
		return capacitySupports//
				.stream()//
				.filter(Objects::nonNull)//
				.filter(support -> BooleanUtils.isNotFalse(support.getEnableCapacity()))//
				.filter(support -> BooleanUtils.isTrue(function.apply(support)))//
				.findAny()//
				.isPresent();
	}
}

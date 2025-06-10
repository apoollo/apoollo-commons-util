/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.support;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.LangUtils;
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

	public static boolean supportAbility(RequestContext requestContext, CapacitySupport capacitySupport,
			Function<CapacitySupport, Boolean> function) {
		Boolean support = getAbility(LangUtils
				.getStream(capacitySupport, requestContext.getRequestResource(), requestContext.getUser()).toList(),
				function);
		return BooleanUtils.isTrue(support);
	}

	public static <T> T getAbility(RequestContext requestContext, CapacitySupport capacitySupport,
			Function<CapacitySupport, T> function) {
		return getAbility(LangUtils
				.getStream(capacitySupport, requestContext.getRequestResource(), requestContext.getUser()).toList(),
				function);
	}

	public static <T> T getAbility(List<CapacitySupport> capacitySupports, Function<CapacitySupport, T> function) {
		return capacitySupports//
				.stream()//
				.filter(Objects::nonNull)//
				.filter(support -> BooleanUtils.isNotFalse(support.getEnableCapacity()))//
				.map(support -> function.apply(support))//
				.filter(Objects::nonNull)//
				.findAny()//
				.orElse(null)//
		;
	}
}

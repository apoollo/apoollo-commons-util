/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.Comparator;
import java.util.List;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.LoggerWriter;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.RequestContextDataBus;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public class DefaultLoggerWriter implements LoggerWriter {

	private List<RequestContextDataBus> requestContextDataBuses;

	public DefaultLoggerWriter(List<RequestContextDataBus> requestContextDataBuses) {
		super();
		this.requestContextDataBuses = LangUtils.getStream(requestContextDataBuses)
				.sorted(Comparator.comparingInt(RequestContextDataBus::getOrder)).toList();
	}

	@Override
	public void write(RequestContext requestContext) {
		this.requestContextDataBuses.stream()
				.forEach(requestContextDataBus -> requestContextDataBus.transport(requestContext));

	}
}

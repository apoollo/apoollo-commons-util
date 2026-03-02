/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.MdcUtils;
import com.apoollo.commons.util.request.context.LoggerWriter;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.RequestContextDataBus;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public class DefaultLoggerWriter implements LoggerWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultLoggerWriter.class);

	private List<RequestContextDataBus> requestContextDataBuses;

	public DefaultLoggerWriter(List<RequestContextDataBus> requestContextDataBuses) {
		super();
		this.requestContextDataBuses = LangUtils.getStream(requestContextDataBuses)
				.sorted(Comparator.comparingInt(RequestContextDataBus::getOrder)).toList();
	}

	@Override
	public void write(HttpServletRequest request, RequestContext requestContext) {
		this.requestContextDataBuses.stream().forEach(requestContextDataBus -> {
			if (requestContextDataBus.asyncSupport()) {

				CompletableFuture.runAsync(() -> {
					MdcUtils.addAll(requestContext.getRequestId());
					requestContextDataBus.transport(request, requestContext);
				}).whenComplete((response, throwable) -> {
					MdcUtils.releaseAll();
					if (null != throwable) {
						LOGGER.error("AsyncRequestContextDataBus transport error:", throwable);
					}
				});
			} else {
				requestContextDataBus.transport(request, requestContext);
			}
		});

	}
}

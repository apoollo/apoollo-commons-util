/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.exception.AppException;
import com.apoollo.commons.util.exception.AppRequestTimeoutLimitException;
import com.apoollo.commons.util.exception.detailed.TimeoutIllegalArgumentException;
import com.apoollo.commons.util.model.MinMax;
import com.apoollo.commons.util.request.context.RequestAccessParameter;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.RequestId;
import com.apoollo.commons.util.request.context.Timeout;

/**
 * @author liuyulong
 */
public class TimeoutContextInvocation<I, O> extends LoggingInvocation<I, O> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeoutContextInvocation.class);

	public TimeoutContextInvocation(String loggingName, String[] inputMaskPropertyNames,
			String[] outputMaskPropertyNames) {
		super(loggingName, inputMaskPropertyNames, outputMaskPropertyNames);
	}

	@Override
	public O invoke(Function<I, O> function, I input) {

		RequestContext requestContext = RequestContext.getRequired();
		if (input instanceof RequestId) {
			RequestId requestId = ((RequestId) input);
			if (StringUtils.isNotBlank(requestId.getRequestId())) {
				requestContext.setClientRequestId(requestId.getRequestId());
			}
			LOGGER.info("clientRequestId：" + requestContext.getClientRequestId());
		}
		RequestAccessParameter requestAccessParameter = requestContext.getAuthorizedValue(RequestAccessParameter.class);
		if (null != requestAccessParameter && null != requestAccessParameter.getRequestDefaultTimeoutMillis()
				&& null != requestAccessParameter.getRequestTimeoutRangeMillis()) {
			return timeoutExecute(function, input, requestContext,
					requestAccessParameter.getRequestDefaultTimeoutMillis(),
					requestAccessParameter.getRequestTimeoutRangeMillis());
		} else {
			return super.invoke(function, input);
		}
	}

	protected O timeoutExecute(Function<I, O> function, I input, RequestContext requestContext,
			Long requestDefaultTimeoutMillis, MinMax<Long> requestTimeoutRangeMillis) {

		Future<O> future = requestContext.getThreadPoolExecutor().submit(() -> {
			try {
				RequestContext.reset(requestContext);
				O result = super.invoke(function, input);
				return result;
			} catch (Exception e) {
				LOGGER.error("service execute error：", e);
				throw e;
			} finally {
				RequestContext.release();
			}
		});
		try {
			// 客户端请求超时时间
			Long clientTimeout = null;
			if (input instanceof Timeout) {
				clientTimeout = ((Timeout) input).getTimeout();
				requestContext.setClientTimeout(clientTimeout);
			} else if (null != requestContext.getClientTimeout()) {
				clientTimeout = requestContext.getClientTimeout();
			}

			// 服务端的请求超时时间
			Long requestTimeout = null;
			if (null != clientTimeout) {
				if (clientTimeout.longValue() <= 0) {
					throw new TimeoutIllegalArgumentException("client timeout must positive integer");
				}
				requestTimeout = LangUtils.getInRange(requestTimeoutRangeMillis, clientTimeout);
			} else {
				requestTimeout = requestDefaultTimeoutMillis;
			}
			requestContext.setRequestTimeout(requestTimeout);

			// 本次调用真实请求超时时间
			long elapsedTime = System.currentTimeMillis() - requestContext.getRequestTime();
			long terminalTimeout = requestTimeout - elapsedTime;

			return future.get(terminalTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			throw new AppRequestTimeoutLimitException("AppTimeout");
		} catch (ExecutionException e) {
			Throwable throwable = e.getCause();
			if (throwable instanceof AppException) {
				throw (AppException) throwable;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}

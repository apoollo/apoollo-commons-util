/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.Invocation;
import com.apoollo.commons.util.request.context.model.LoggingInvocationConfig;

/**
 * @author liuyulong
 */
public class LoggingInvocation<I, O> implements Invocation<I, O> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInvocation.class);

	private String loggingName;
	private LoggingInvocationConfig inputLoggingConfig;
	private LoggingInvocationConfig outputLoggingConfig;

	@Deprecated
	public LoggingInvocation(LoggingInvocationConfig inputLoggingConfig, LoggingInvocationConfig outputLoggingConfig) {
		super();
		this.inputLoggingConfig = inputLoggingConfig;
		this.outputLoggingConfig = outputLoggingConfig;
		if (null != inputLoggingConfig) {
			loggingName = inputLoggingConfig.getLoggingName();
		}
		if (StringUtils.isBlank(loggingName)) {
			if (null != outputLoggingConfig) {
				loggingName = outputLoggingConfig.getLoggingName();
			}
		}
		if (StringUtils.isNotBlank(loggingName)) {
			if (loggingName.endsWith("入参") || loggingName.endsWith("出参")) {
				loggingName = loggingName.substring(0, loggingName.length() - 2);
			}
		}
	}

	public LoggingInvocation(String loggingName, String[] inputMaskPropertyNames, String[] outputMaskPropertyNames) {
		super();
		this.loggingName = loggingName;
		if (StringUtils.isNotBlank(loggingName) || null != inputMaskPropertyNames) {
			this.inputLoggingConfig = new LoggingInvocationConfig(StringUtils.join(loggingName, "入参"),
					inputMaskPropertyNames);
		}
		if (StringUtils.isNotBlank(loggingName) || null != outputMaskPropertyNames) {
			this.outputLoggingConfig = new LoggingInvocationConfig(StringUtils.join(loggingName, "出参"),
					outputMaskPropertyNames);
		}
	}

	@Override
	public O invoke(Function<I, O> function, I input) {
		long requestTime = System.currentTimeMillis();
		if (null != inputLoggingConfig) {
			infoParameterLogging(inputLoggingConfig, input);
		}
		O output = function.apply(input);
		long responseTime = System.currentTimeMillis();
		if (null != outputLoggingConfig) {
			infoParameterLogging(outputLoggingConfig, output);
		}
		infoElapsedTime(responseTime - requestTime);
		return output;
	}

	protected void infoParameterLogging(LoggingInvocationConfig loggingInvocationConfig, Object target) {

		if (null != loggingInvocationConfig.getLoggingName()) {

			StringBuilder message = new StringBuilder();
			message.append(loggingInvocationConfig.getLoggingName()).append("：");
			if (null != target) {
				message.append(JSON.toJSONString(target,
						LangUtils.getMaskValueFilter(loggingInvocationConfig.getMaskPropertyNames()),
						JSONWriter.Feature.WriteMapNullValue));
			} else {
				message.append("无");
			}
			LOGGER.info(message.toString());

		}

	}

	public void infoElapsedTime(Long elapsedTime) {
		if (null != elapsedTime) {
			if (StringUtils.isNotBlank(loggingName)) {
				LOGGER.info("[" + loggingName + "] elapsedTime：" + elapsedTime + "ms");
			} else {
				LOGGER.info("elapsedTime：" + elapsedTime + "ms");
			}
		}
	}

}

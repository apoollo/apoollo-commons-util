package com.apoollo.commons.util.request.context.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class LoggingInvocationConfig {
	private String loggingName;
	private String[] maskPropertyNames;

	public LoggingInvocationConfig() {
	}

	/**
	 * @param loggingName
	 */
	public LoggingInvocationConfig(String loggingName) {
		super();
		this.loggingName = loggingName;
	}

	/**
	 * @param loggingName
	 * @param maskPropertyNames
	 */
	public LoggingInvocationConfig(String loggingName, String[] maskPropertyNames) {
		super();
		this.loggingName = loggingName;
		this.maskPropertyNames = maskPropertyNames;
	}

}
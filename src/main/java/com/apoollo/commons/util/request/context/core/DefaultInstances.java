/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.cors.CorsConfiguration;

import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.Instances;
import com.apoollo.commons.util.request.context.limiter.NonceValidator;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;
import com.apoollo.commons.util.web.spring.Instance;
import com.apoollo.commons.util.web.spring.core.DefaultInstance;

/**
 * @author liuyulong
 * @since 2025-06-20
 */
public class DefaultInstances implements Instances, ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private Instance<NonceValidator> nonceValidatorInstance = new DefaultInstance<>(NonceValidator.class);
	private Instance<WrapResponseHandler> wrapResponseHandlerInstance = new DefaultInstance<>(
			WrapResponseHandler.class);
	private Instance<EscapeMethod> escapeMethodInstance = new DefaultInstance<>(EscapeMethod.class);
	private Instance<CorsConfiguration> corsConfigurationInstance = new DefaultInstance<>(CorsConfiguration.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		nonceValidatorInstance.initail(applicationContext);
		wrapResponseHandlerInstance.initail(applicationContext);
		escapeMethodInstance.initail(applicationContext);
		corsConfigurationInstance.initail(applicationContext);
	}

	@Override
	public NonceValidator getNonceValidator(Class<? extends NonceValidator> clazz) {
		NonceValidator nonceValidator = nonceValidatorInstance.getInstance(clazz);
		if (null == nonceValidator) {
			throw new RuntimeException("can't find nonceValidator by:" + clazz);
		}
		return nonceValidator;
	}

	@Override
	public NonceValidator getNonceValidator(String clazz) {
		NonceValidator nonceValidator = nonceValidatorInstance.getInstance(clazz);
		if (null == nonceValidator) {
			throw new RuntimeException("can't find nonceValidator by:" + clazz);
		}
		return nonceValidator;
	}

	@Override
	public WrapResponseHandler getWrapResponseHandler(Class<? extends WrapResponseHandler> clazz) {
		WrapResponseHandler wrapResponseHandler = wrapResponseHandlerInstance.getInstance(clazz);
		if (null == wrapResponseHandler) {
			throw new RuntimeException("can't find wrapResponseHandler by:" + clazz);
		}
		return wrapResponseHandler;
	}

	@Override
	public WrapResponseHandler getWrapResponseHandler(String clazz) {
		WrapResponseHandler wrapResponseHandler = wrapResponseHandlerInstance.getInstance(clazz);
		if (null == wrapResponseHandler) {
			throw new RuntimeException("can't find wrapResponseHandler by:" + clazz);
		}
		return wrapResponseHandler;
	}

	@Override
	public EscapeMethod getEscapeMethod(Class<? extends EscapeMethod> clazz) {
		EscapeMethod escapeMethod = escapeMethodInstance.getInstance(clazz);
		if (null == escapeMethod) {
			throw new RuntimeException("can't find escapeMethod by:" + clazz);
		}
		return escapeMethod;
	}

	@Override
	public EscapeMethod getEscapeMethod(String clazz) {
		EscapeMethod escapeMethod = escapeMethodInstance.getInstance(clazz);
		if (null == escapeMethod) {
			throw new RuntimeException("can't find escapeMethod by:" + clazz);
		}
		return escapeMethod;
	}

	@Override
	public CorsConfiguration getCorsConfiguration(Class<? extends CorsConfiguration> clazz) {
		CorsConfiguration corsConfiguration = corsConfigurationInstance.getInstance(clazz);
		if (null == corsConfiguration) {
			throw new RuntimeException("can't find corsConfiguration by:" + clazz);
		}
		return corsConfiguration;
	}

	@Override
	public CorsConfiguration getCorsConfiguration(String clazz) {
		CorsConfiguration corsConfiguration = corsConfigurationInstance.getInstance(clazz);
		if (null == corsConfiguration) {
			throw new RuntimeException("can't find corsConfiguration by:" + clazz);
		}
		return corsConfiguration;
	}

}

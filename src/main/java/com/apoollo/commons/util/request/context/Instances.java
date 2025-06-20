/**
 * 
 */
package com.apoollo.commons.util.request.context;

import org.springframework.context.ApplicationContext;
import org.springframework.web.cors.CorsConfiguration;

import com.apoollo.commons.util.request.context.limiter.NonceValidator;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;

/**
 * @author liuyulong
 * @since 2025-06-20
 */
public interface Instances {

	public ApplicationContext getApplicationContext();

	public NonceValidator getNonceValidator(Class<? extends NonceValidator> clazz);

	public NonceValidator getNonceValidator(String clazz);

	public WrapResponseHandler getWrapResponseHandler(Class<? extends WrapResponseHandler> clazz);

	public WrapResponseHandler getWrapResponseHandler(String clazz);

	public EscapeMethod getEscapeMethod(Class<? extends EscapeMethod> clazz);

	public EscapeMethod getEscapeMethod(String clazz);

	public CorsConfiguration getCorsConfiguration(Class<? extends CorsConfiguration> clazz);

	public CorsConfiguration getCorsConfiguration(String clazz);
}

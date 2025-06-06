/**
 * 
 */
package com.apoollo.commons.util.web.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author liuyulong
 * @since 2025-05-29
 */
public interface Instance {

	public ApplicationContext getApplicationContext();

	public <T> T getInstance(Class<T> clazz);

	public <T> T getInstance(String clazz);

}

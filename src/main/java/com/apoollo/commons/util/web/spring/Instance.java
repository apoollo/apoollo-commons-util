/**
 * 
 */
package com.apoollo.commons.util.web.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author liuyulong
 * @since 2025-05-29
 */
public interface Instance<T> {

	public void initail(ApplicationContext applicationContext);

	public T getInstance(Class<? extends T> clazz);

	public T getInstance(String clazz);

}

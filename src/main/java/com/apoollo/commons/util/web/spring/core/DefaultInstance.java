/**
 * 
 */
package com.apoollo.commons.util.web.spring.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.apoollo.commons.util.web.spring.Instance;

/**
 * @author liuyulong
 * @since 2025-05-29
 */
public class DefaultInstance<T> implements Instance<T> {

	private Map<String, T> instance;

	private Class<T> clazz;

	public DefaultInstance(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T getInstance(Class<? extends T> clazz) {
		return getInstance(clazz.getName());
	}

	@Override
	public T getInstance(String clazz) {
		return instance.get(clazz);
	}

	@Override
	public void initail(ApplicationContext applicationContext) {
		if (null == instance) {
			instance = new HashMap<>();
			Map<String, T> beans = applicationContext.getBeansOfType(clazz);
			if (null != beans && !beans.isEmpty()) {
				beans.values().stream().forEach(bean -> {
					instance.put(bean.getClass().getName().toString(), bean);
				});
			}
		}
	}

}

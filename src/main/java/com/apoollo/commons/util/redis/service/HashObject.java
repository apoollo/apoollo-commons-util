/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */
public interface HashObject {

	public Object getValue(String key, String field);

	public default <T> T getObject(String key, String field, Class<T> clazz) {
		Object value = getValue(key, field);
		return null == value ? null : LangUtils.parseObject(value, clazz);
	}

	public void setValue(String key, String field, Object value);

	public void delete(String key, String field);
	
	public void delete(String key);
}

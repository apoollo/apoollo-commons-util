/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.List;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */
public interface DataBus<T> {

	public default void transport(T data) {
		if (null != data) {
			transport(LangUtils.toList(data));
		}
	}

	public void transport(List<T> datas);
	
}

/**
 * 
 */
package com.apoollo.commons.util.request.context;

import com.apoollo.commons.util.request.context.core.DefaultRequestContext;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContextInitail {

	public RequestContext newInstance();

	public static RequestContextInitail DefaultRequestContext() {
		return DefaultRequestContext::new;
	}
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import com.apoollo.commons.util.request.context.HttpCodeNameMessage;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class DefaultHttpCodeNameMessage<C, N, M> extends DefaultHttpCodeName<C, N>
		implements HttpCodeNameMessage<C, N, M> {

	private M message;

	public DefaultHttpCodeNameMessage(C code, N name, Integer httpCode, M message) {
		super(code, name, httpCode);
		this.message = message;
	}

	public M getMessage() {
		return message;
	}

}

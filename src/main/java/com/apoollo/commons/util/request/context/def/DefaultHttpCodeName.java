/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import com.apoollo.commons.util.request.context.HttpCodeName;

/**
 * @author liuyulong
 */
public class DefaultHttpCodeName<C, N> extends DefaultCodeName<C, N> implements HttpCodeName<C, N> {

	private Integer httpCode;

	public DefaultHttpCodeName() {
	}

	public DefaultHttpCodeName(C code, N name, Integer httpCode) {
		super(code, name);
		this.httpCode = httpCode;
	}

	@Override
	public Integer getHttpCode() {
		return httpCode;
	}

}

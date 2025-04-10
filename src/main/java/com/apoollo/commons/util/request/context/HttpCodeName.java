/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 */
public interface HttpCodeName<C, N> extends CodeName<C, N> {

	public Integer getHttpCode();
}

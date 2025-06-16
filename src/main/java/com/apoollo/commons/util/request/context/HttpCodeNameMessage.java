/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 */
public interface HttpCodeNameMessage<C, N, M> extends CodeName<C, N> {

	public Integer getHttpCode();

	public M getMessage();
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

/**
 * liuyulong
 */
public interface TokenPair<T> {

	public String getAccessKey();

	public T getToken();
}

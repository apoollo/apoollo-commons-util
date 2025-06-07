/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

/**
 * liuyulong
 */
public interface Authentication<T> {

	public Authority authenticate(String userPin, T token);
	
}

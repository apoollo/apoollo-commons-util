/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import com.apoollo.commons.util.request.context.access.core.AbstractAuthentication.Authority;
import com.apoollo.commons.util.request.context.core.AccessStrategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * liuyulong
 */
public interface Authentication<T> {
	
	public boolean support(AccessStrategy accessStrategy);
	
	public Authority<T> authenticate(HttpServletRequest request);
	
	
}

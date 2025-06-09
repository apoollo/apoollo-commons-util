/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import com.apoollo.commons.util.request.context.RequestResource;
import com.apoollo.commons.util.request.context.User;

/**
 * liuyulong
 */
public interface Authorization {

	public void authorize(User user, RequestResource requestResource);
	
	public void setDynamic(String accessKey, String resourcePin);

	public void deleteDynamic(String accessKey, String resourcePin);
}

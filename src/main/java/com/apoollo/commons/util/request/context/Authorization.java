/**
 * 
 */
package com.apoollo.commons.util.request.context;

import com.apoollo.commons.util.request.context.model.Authorized;

/**
 * @author liuyulong
 * @since 2023年9月27日
 */
public interface Authorization<T> {

	public Authorized<T> getAuthorized(User user, RequestResource requestResource);

	public void setAuthorized(String accessKey, String resourcePin, Authorized<?> authorized);

	public void deleteAuthorization(String accessKey, String resourcePin);

}

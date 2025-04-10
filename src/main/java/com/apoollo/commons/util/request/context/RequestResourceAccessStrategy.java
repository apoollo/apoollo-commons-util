/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 */
public interface RequestResourceAccessStrategy {

	public HttpCodeNameHandler getHttpCodeNameHandler();

	public boolean crossRequestHeaderJwtTokenAccessInterceptor();

	public boolean crossRequestHeaderSecretKeyTokenAccessInterceptor();

	public boolean crossRequestParameterSecretKeyTokenAccessInterceptor();

	public boolean crossRequestBodyDigestValidateAdvice();

	public boolean crossRequestBodySecretKeyTokenAccessAdvice();

	public default boolean crossRequestBodyJwtTokenAccessAdvice() {
		return false;
	}

	public default String getAuthenticationTokenFromRequestBody(Object requestBody) {
		return null;
	}
}

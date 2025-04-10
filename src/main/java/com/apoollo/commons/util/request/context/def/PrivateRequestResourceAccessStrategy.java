/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import com.apoollo.commons.util.request.context.HttpCodeNameHandler;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.RequestResourceAccessStrategy;

/**
 * @author liuyulong
 */
public class PrivateRequestResourceAccessStrategy implements RequestResourceAccessStrategy {

	@Override
	public HttpCodeNameHandler getHttpCodeNameHandler() {
		return RequestContext.DEFAULT_HTTP_CODE_NAME_HANDLER;
	}

	@Override
	public boolean crossRequestHeaderJwtTokenAccessInterceptor() {
		return true;
	}

	@Override
	public boolean crossRequestBodyDigestValidateAdvice() {
		return false;
	}

	@Override
	public boolean crossRequestBodySecretKeyTokenAccessAdvice() {
		return false;
	}

	@Override
	public boolean crossRequestHeaderSecretKeyTokenAccessInterceptor() {
		return false;
	}

	@Override
	public boolean crossRequestParameterSecretKeyTokenAccessInterceptor() {
		return false;
	}

}

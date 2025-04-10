/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

/**
 * liuyulong
 */
public class PublicRequestResourceAccessStrategy extends PrivateRequestResourceAccessStrategy {

	@Override
	public boolean crossRequestHeaderJwtTokenAccessInterceptor() {
		return false;
	}
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.TokenPair;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.core.AccessStrategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-09
 */
public class JSONBodyKeyPairAuthentication extends AbstractKeyPairAuthentication {

	/**
	 * @param userManager
	 * @param accessKeyProperty
	 * @param secretKeyProperty
	 */
	public JSONBodyKeyPairAuthentication(UserManager userManager, String accessKeyProperty, String secretKeyProperty) {
		super(userManager, accessKeyProperty, secretKeyProperty);
	}

	@Override
	public boolean support(AccessStrategy accessStrategy) {
		return AccessStrategy.PRIVATE_JSON_BODY_KEY_PAIR == accessStrategy;
	}

	@Override
	public TokenPair<String> getTokenPair(HttpServletRequest request, RequestContext requestContext) {
		return getTokenPair(request, requestContext, json -> {
			return new KeyTokenPair(json.getString(accessKeyProperty), json.getString(secretKeyProperty));
		});
	}

}

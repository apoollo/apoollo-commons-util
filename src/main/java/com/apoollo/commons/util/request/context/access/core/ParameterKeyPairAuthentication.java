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
 * liuyulong
 */
public class ParameterKeyPairAuthentication extends AbstractKeyPairAuthentication {

	public ParameterKeyPairAuthentication(UserManager userManager, String accessKeyProperty, String secretKeyProperty) {
		super(userManager, accessKeyProperty, secretKeyProperty);
	}

	@Override
	public boolean support(AccessStrategy accessStrategy) {
		return AccessStrategy.PRIVATE_PARAMETER_KEY_PAIR == accessStrategy;
	}

	@Override
	public TokenPair<String> getTokenPair(HttpServletRequest request, RequestContext requestContext) {
		return new KeyTokenPair(request.getParameter(accessKeyProperty), request.getParameter(secretKeyProperty));
	}

}

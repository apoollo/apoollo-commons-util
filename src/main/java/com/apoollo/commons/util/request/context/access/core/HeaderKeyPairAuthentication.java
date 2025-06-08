/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.request.context.access.TokenPair;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.core.AccessStrategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * liuyulong
 */
public class HeaderKeyPairAuthentication extends AbstractKeyPairAuthentication {

	public HeaderKeyPairAuthentication(UserManager userManager, String accessKeyProperty, String secretKeyProperty) {
		super(userManager, accessKeyProperty, secretKeyProperty);
	}

	@Override
	public boolean support(AccessStrategy accessStrategy) {
		return AccessStrategy.PRIVATE_HEADER_KEY_PAIR == accessStrategy;
	}

	@Override
	public TokenPair<String> getTokenPair(HttpServletRequest request) {
		return new KeyTokenPair(request.getHeader(accessKeyProperty), request.getHeader(secretKeyProperty));
	}

}

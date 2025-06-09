/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.AuthorizationJwtTokenDecoder;
import com.apoollo.commons.util.request.context.access.TokenPair;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.core.AccessStrategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * liuyulong
 */
public class JSONBodyJwtAuthentication extends AbstractJwtTokenAuthentication {

	private String jwtTokenProperty;

	public JSONBodyJwtAuthentication(UserManager userManager, AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder,
			String jwtTokenProperty) {
		super(userManager, authorizationJwtTokenDecoder);
		this.jwtTokenProperty = LangUtils.defaultString(jwtTokenProperty, "jwtToken");
	}

	@Override
	public boolean support(AccessStrategy accessStrategy) {
		return AccessStrategy.PRIVATE_JSON_BODY_JWT_TOKEN == accessStrategy;
	}

	@Override
	public TokenPair<JwtToken> getTokenPair(HttpServletRequest request, RequestContext requestContext) {

		return getTokenPair(request, requestContext, json -> {
			JwtToken jwtToken = authorizationJwtTokenDecoder.decode(json.getString(jwtTokenProperty));
			return new JwtTokenPair(jwtToken.getAccessKey(), jwtToken);
		});

	}

}

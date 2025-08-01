/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.JwtUtils;
import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.AuthorizationJwtTokenDecoder;
import com.apoollo.commons.util.request.context.access.TokenPair;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.core.AccessStrategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * liuyulong
 */
public class HeaderJwtAuthentication extends AbstractJwtTokenAuthentication{
	
	public HeaderJwtAuthentication(UserManager userManager, AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder) {
		super(userManager, authorizationJwtTokenDecoder);
	}


	@Override
	public boolean support(AccessStrategy accessStrategy) {
		return AccessStrategy.PRIVATE_HEADER_JWT_TOKEN == accessStrategy;
	}

	@Override
	public TokenPair<JwtToken> getTokenPair(HttpServletRequest request, RequestContext requestContext) {
		JwtToken jwtToken = authorizationJwtTokenDecoder.decode(request.getHeader(JwtUtils.HEADER_AUTHORIZATIONE));
		return new JwtTokenPair(jwtToken.getAccessKey(), jwtToken);
	}
	

}

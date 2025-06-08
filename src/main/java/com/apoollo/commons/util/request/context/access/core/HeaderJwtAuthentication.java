/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.JwtUtils;
import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.exception.AppForbbidenException;
import com.apoollo.commons.util.request.context.User;
import com.apoollo.commons.util.request.context.access.AuthorizationJwtTokenDecoder;
import com.apoollo.commons.util.request.context.access.TokenPair;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.core.AccessStrategy;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * liuyulong
 */
public class HeaderJwtAuthentication extends AbstractAuthentication<JwtToken> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeaderJwtAuthentication.class);

	private AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder;

	public HeaderJwtAuthentication(UserManager userManager, AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder) {
		super(userManager);
		this.authorizationJwtTokenDecoder = authorizationJwtTokenDecoder;
	}

	@Override
	public boolean support(AccessStrategy accessStrategy) {
		return AccessStrategy.PRIVATE_HEADER_JWT_TOKEN == accessStrategy;
	}

	@Override
	public TokenPair<JwtToken> getTokenPair(HttpServletRequest request) {
		JwtToken jwtToken = authorizationJwtTokenDecoder.decode(request.getHeader(JwtUtils.HEADER_AUTHORIZATIONE));
		return new JwtTokenPair(jwtToken.getAccessKey(), jwtToken);
	}

	@Override
	public void authenticate(User user, JwtToken token) {
		try {
			JwtUtils.jwtVerify(token.getJwtTokenDecoded(), user.getSecretKey(), user.getSecretKeySaltValue());
			LOGGER.info("jwt token verify success");
		} catch (TokenExpiredException e) {
			LOGGER.error("signature expired:", e);
			throw new AppForbbidenException("signature expired");
		} catch (SignatureVerificationException e) {
			LOGGER.error("signature verify failed:", e);
			throw new AppForbbidenException("signature verify failed");
		} catch (Exception e) {
			LOGGER.error("signature verify error:", e);
			throw new AppForbbidenException("signature verify error");
		}
	}

}

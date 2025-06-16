/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.JwtUtils;
import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.exception.refactor.AppAuthenticationJwtTokenExpiredException;
import com.apoollo.commons.util.exception.refactor.AppAuthenticationJwtTokenForbiddenException;
import com.apoollo.commons.util.exception.refactor.AppAuthenticationJwtTokenIllegalException;
import com.apoollo.commons.util.request.context.access.AuthorizationJwtTokenDecoder;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

/**
 * @author liuyulong
 * @since 2025-06-09
 */
public abstract class AbstractJwtTokenAuthentication extends AbstractAuthentication<JwtToken> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJwtTokenAuthentication.class);

	protected AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder;

	public AbstractJwtTokenAuthentication(UserManager userManager,
			AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder) {
		super(userManager);
		this.authorizationJwtTokenDecoder = authorizationJwtTokenDecoder;
	}

	@Override
	public void authenticate(User user, JwtToken token) {
		try {
			JwtUtils.jwtVerify(token.getJwtTokenDecoded(), user.getSecretKey(), user.getSecretKeySaltValue());
			LOGGER.info("jwt token verify success");
		} catch (TokenExpiredException e) {
			LOGGER.error("signature expired:", e);
			throw new AppAuthenticationJwtTokenExpiredException("signature expired");
		} catch (SignatureVerificationException e) {
			LOGGER.error("signature verify failed:", e);
			throw new AppAuthenticationJwtTokenForbiddenException("signature forbbiden");
		} catch (Exception e) {
			LOGGER.error("signature verify error:", e);
			throw new AppAuthenticationJwtTokenIllegalException("signature verify error");
		}

	}

}

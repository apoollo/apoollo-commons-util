/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.JwtUtils;
import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenExpiredException;
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenForbiddenException;
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenIllegalException;
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

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(AbstractJwtTokenAuthentication.class);

	protected AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder;

	public AbstractJwtTokenAuthentication(UserManager userManager,
			AuthorizationJwtTokenDecoder authorizationJwtTokenDecoder) {
		super(userManager);
		this.authorizationJwtTokenDecoder = authorizationJwtTokenDecoder;
	}

	@Override
	public void authenticate(User user, JwtToken token) {
		try {
			JwtUtils.jwtVerify(token.getJwtTokenDecoded(), user.getSecretKey(), user.getSecretKeySsoSalt());
		} catch (TokenExpiredException e) {
			throw new AppAuthenticationJwtTokenExpiredException("jwt token expired", e);
		} catch (SignatureVerificationException e) {
			throw new AppAuthenticationJwtTokenForbiddenException("jwt forbbiden", e);
		} catch (Exception e) {
			throw new AppAuthenticationJwtTokenIllegalException("jwt verify error", e);
		}

	}

}

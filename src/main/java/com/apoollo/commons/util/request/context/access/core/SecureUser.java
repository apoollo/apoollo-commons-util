/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.exception.AppAuthenticationUserPasswordExpiredException;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.Authentication;
import com.apoollo.commons.util.request.context.access.Authorization;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.SecurePrincipal;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.core.AbstractAuthentication.Authority;
import com.apoollo.commons.util.request.context.limiter.Limiters;
import com.apoollo.commons.util.request.context.model.RequestConstants;
import com.apoollo.commons.util.request.context.model.RequestContextCapacitySupport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * liuyulong
 */
public class SecureUser implements SecurePrincipal<User> {

	private List<Authentication<?>> authentications;
	private Authorization authorization;
	private Limiters<User> limiters;
	private JwtAuthorizationRenewal authorizationRenewal;

	public SecureUser(List<Authentication<?>> authentications, Authorization authorization, Limiters<User> limiters,
			JwtAuthorizationRenewal authorizationRenewal) {
		super();
		this.authentications = authentications;
		this.authorization = authorization;
		this.limiters = limiters;
		this.authorizationRenewal = authorizationRenewal;
	}

	@Override
	public User init(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext) {
		RequestResource requestResource = requestContext.getRequestResource();
		return authentications.stream()
				.filter(authentication -> authentication.support(requestResource.getAccessStrategy()))
				.map(authentication -> {
					// 身份判定
					Authority<?> authority = authentication.authenticate(request, requestContext);
					User user = authority.getUser();
					requestContext.setRequestUser(user);

					if (null != user.getPasswordLastUpdateTimestamp() && null != user.getPasswordValidMillis()) {
						Long userPasswordExpireRemainDuration = user.getPasswordLastUpdateTimestamp()
								+ user.getPasswordValidMillis() - System.currentTimeMillis();

						if (BooleanUtils.isTrue(user.getEnableForceChangePassword())
								&& userPasswordExpireRemainDuration <= 0) {
							throw new AppAuthenticationUserPasswordExpiredException("user password already expired");
						}

						response.setHeader(RequestConstants.RESPONSE_HEADER_USER_PASSWORD_EXPIRE_REMAIN_DURATION,
								String.valueOf(userPasswordExpireRemainDuration));
					}

					// 资源判定
					authorization.authorize(user, requestResource);

					// limiters
					RequestContextCapacitySupport.doSupport(List.of(user), capacitySupport -> {
						limiters.limit(request, response, requestContext, capacitySupport);
					});

					// token 续期
					Object token = authority.getToken();
					if (token instanceof JwtToken jwtToken) {
						authorizationRenewal.renewal(user, jwtToken, (renewal) -> {
							response.setHeader(RequestConstants.RESPONSE_HEADER_RENEWAL_AUTHORIZATION,
									renewal.getRenewalAuthorizationJwtToken());
						});
					}

					return user;
				})//
				.findAny()//
				.orElse(null);
	}

}

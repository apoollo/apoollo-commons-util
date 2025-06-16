/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.exception.refactor.AppAuthenticationKeyPairSecretKeyForbiddenException;
import com.apoollo.commons.util.exception.refactor.AppAuthenticationKeyPairTokenIllegalException;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserManager;

/**
 * liuyulong
 */
public abstract class AbstractKeyPairAuthentication extends AbstractAuthentication<String> {

	protected String accessKeyProperty;
	protected String secretKeyProperty;

	public AbstractKeyPairAuthentication(UserManager userManager, String accessKeyProperty, String secretKeyProperty) {
		super(userManager);
		this.accessKeyProperty = LangUtils.defaultString(accessKeyProperty, "accessKey");
		this.secretKeyProperty = LangUtils.defaultString(secretKeyProperty, "secretKey");
	}

	@Override
	public void authenticate(User user, String token) {
		if (StringUtils.isBlank(token)) {
			throw new AppAuthenticationKeyPairTokenIllegalException(this.accessKeyProperty + " must not be blank");
		}
		if (!StringUtils.equals(user.getSecretKey(), token)) {
			throw new AppAuthenticationKeyPairSecretKeyForbiddenException(
					this.secretKeyProperty + "secretKey verify failed");
		}
	}

}

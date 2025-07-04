/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.apoollo.commons.util.JwtUtils;
import com.apoollo.commons.util.JwtUtils.Renewal;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.core.DefaultUser.SerializableUser;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
public interface UserManager {

	public User getUser(String accessKey);

	public void setUser(SerializableUser user, Long timeout, TimeUnit timeUnit);

	public void renewal(String accessKey, Renewal renewal);

	public default String login(SerializableUser user, Long timeout, TimeUnit timeUnit) {
		int keepSeconds = ((Long) timeUnit.toSeconds(timeout)).intValue();
		String jwtToken = JwtUtils.generateJwtToken(user.getAccessKey(), user.getSecretKey(),
				user.getSecretKeySsoSalt(), new Date(), keepSeconds);
		setUser(user, timeout, timeUnit);
		return JwtUtils.authorizationConcatJwtToken(jwtToken);
	}

	public void refresh(String accessKey, Boolean enable, String secretKey);

	public void refresh(String accessKey, Boolean enable);

	public void refresh(String accessKey, String secretKey);

	public void delete(String accessKey);

	public default void logout() {
		delete(RequestContext.getRequired().getRequestUser().getAccessKey());
	}

}

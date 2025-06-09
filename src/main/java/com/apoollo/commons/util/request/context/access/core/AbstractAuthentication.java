/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.apoollo.commons.util.exception.AppForbbidenException;
import com.apoollo.commons.util.exception.detailed.AccessKeyEmptyException;
import com.apoollo.commons.util.exception.detailed.TokenEmptyExcetion;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.access.Authentication;
import com.apoollo.commons.util.request.context.access.TokenPair;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.model.ServletInputStreamHelper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * liuyulong
 */
public abstract class AbstractAuthentication<T> implements Authentication<T> {

	protected UserManager userManager;

	public AbstractAuthentication(UserManager userManager) {
		super();
		this.userManager = userManager;
	}

	@Override
	public Authority<T> authenticate(HttpServletRequest request, RequestContext requestContext) {
		TokenPair<T> tokenPair = getTokenPair(request, requestContext);
		String accessKey = tokenPair.getAccessKey();
		if (StringUtils.isBlank(accessKey)) {
			throw new AccessKeyEmptyException("[accessKey] must not be blank");
		}
		T token = tokenPair.getToken();
		if (null == token) {
			throw new TokenEmptyExcetion("[token] must not be null");
		}
		User user = userManager.getUser(accessKey);
		if (null == user) {
			throw new AppForbbidenException("Not Logged In : " + accessKey);
		}
		if (!BooleanUtils.isTrue(user.getEnable())) {
			throw new AppForbbidenException("user disabled : " + accessKey);
		}
		String secretKey = user.getSecretKey();
		if (StringUtils.isBlank(secretKey)) {
			throw new RuntimeException("user [secretKey] must not be blank");
		}
		authenticate(user, token);
		return new Authority<>(user, token);
	}

	public TokenPair<T> getTokenPair(HttpServletRequest request, RequestContext requestContext,
			Function<JSONObject, TokenPair<T>> map) {
		byte[] bytes = ServletInputStreamHelper.getCachingBodyByteArray(requestContext, request);
		if (ArrayUtils.isNotEmpty(bytes)) {
			JSONObject jsonObject = JSON.parseObject(bytes, 0, bytes.length,
					ServletInputStreamHelper.getCharset(request));
			return map.apply(jsonObject);
		} else {
			throw new RuntimeException("body must not be null");
		}
	}

	public abstract TokenPair<T> getTokenPair(HttpServletRequest request, RequestContext requestContext);

	public abstract void authenticate(User user, T token);

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Authority<T> {
		private User user;
		private T token;

	}
}

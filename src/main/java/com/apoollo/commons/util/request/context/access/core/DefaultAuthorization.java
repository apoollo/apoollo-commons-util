/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.apoollo.commons.util.exception.refactor.AppAuthorizationForbiddenException;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.access.Authorization;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserRequestResourceMatcher;

/**
 * liuyulong
 */
public class DefaultAuthorization implements Authorization {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthorization.class);

	private StringRedisTemplate redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;
	private UserRequestResourceMatcher requestResourceMatcher;
	private Map<String, List<String>> accessKeyRequestResourcePinsMapping;

	public DefaultAuthorization(StringRedisTemplate redisTemplate, RedisNameSpaceKey redisNameSpaceKey,
			UserRequestResourceMatcher requestResourceMatcher,
			Map<String, List<String>> accessKeyRequestResourcePinsMapping) {
		super();
		this.redisTemplate = redisTemplate;
		this.redisNameSpaceKey = redisNameSpaceKey;
		this.requestResourceMatcher = requestResourceMatcher;
		this.accessKeyRequestResourcePinsMapping = accessKeyRequestResourcePinsMapping;
	}

	public String getAuthorizedRedisKey(String accessKey) {
		return redisNameSpaceKey.getKey(RedisNameSpaceKey.AUTHORIZATION, accessKey);
	}

	@Override
	public void authorize(User user, RequestResource requestResource) {
		String accessKey = user.getAccessKey();
		String resourcePin = requestResource.getResourcePin();
		long startTimestamp = System.currentTimeMillis();
		boolean authorized = false;

		// 本地配置文件设置权限判定授权
		if (MapUtils.isNotEmpty(accessKeyRequestResourcePinsMapping)) {
			List<String> resourcePins = accessKeyRequestResourcePinsMapping.get(accessKey);
			authorized = CollectionUtils.isNotEmpty(resourcePins) && resourcePins.contains(resourcePin);
		}
		if (!authorized) {
			// 通过用户设置的授权条件匹配判定授权
			authorized = requestResourceMatcher.maches(user.getAuthorizationCondition(), requestResource);
			if (!authorized) {
				// 通过动态权限判定授权
				String key = getAuthorizedRedisKey(accessKey);
				Object permission = redisTemplate.opsForHash().get(key, resourcePin);
				if (permission instanceof Boolean result) {
					authorized = result;
				}
				if (!authorized) {
					throw new AppAuthorizationForbiddenException("unauthorized");
				}
			}
		}
		long endTimestamp = System.currentTimeMillis();
		LOGGER.info("authorized elapsedTime：" + (endTimestamp - startTimestamp) + "ms");
	}

	@Override
	public void setDynamic(String accessKey, String resourcePin) {
		String key = getAuthorizedRedisKey(accessKey);
		redisTemplate.opsForHash().put(key, resourcePin, true);

	}

	@Override
	public void deleteDynamic(String accessKey, String resourcePin) {
		String key = getAuthorizedRedisKey(accessKey);
		redisTemplate.opsForHash().delete(key, resourcePin);
	}

}

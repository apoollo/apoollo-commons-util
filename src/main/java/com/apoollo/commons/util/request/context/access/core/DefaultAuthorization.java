/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.AntPathMatcher;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.exception.AppForbbidenException;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.access.Authorization;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.User;

/**
 * liuyulong
 */
public class DefaultAuthorization implements Authorization {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthorization.class);

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
	private StringRedisTemplate redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;
	private Map<String, List<String>> accessKeyRequestResourcePinsMapping;
	
	
	public DefaultAuthorization(StringRedisTemplate redisTemplate, RedisNameSpaceKey redisNameSpaceKey,
			Map<String, List<String>> accessKeyRequestResourcePinsMapping) {
		super();
		this.redisTemplate = redisTemplate;
		this.redisNameSpaceKey = redisNameSpaceKey;
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

		// 通过角色判定授权
		if (!authorized && CollectionUtils.isNotEmpty(requestResource.getRoles())
				&& CollectionUtils.isNotEmpty(user.getRoles())) {
			Optional<String> roleOptional = requestResource.getRoles().stream()
					.filter(role -> user.getRoles().contains(role)).findAny();
			authorized = roleOptional.isPresent();
		}

		// 通过USER URL匹配判定授权
		if (!authorized && CollectionUtils.isNotEmpty(user.getAllowRequestAntPathPatterns())) {
			authorized = matches(requestResource.getRequestMappingPath(), user.getAllowRequestAntPathPatterns());
		}

		// 通过动态权限判定授权
		if (!authorized) {
			String key = getAuthorizedRedisKey(accessKey);
			Object permission = redisTemplate.opsForHash().get(key, resourcePin);
			authorized = null != permission;
		}

		if (!authorized) {
			throw new AppForbbidenException("unauthorized");
		}

		long endTimestamp = System.currentTimeMillis();
		LOGGER.info("authorized elapsedTime：" + (endTimestamp - startTimestamp) + "ms");
	}

	private boolean matches(String requestMappingPath, List<String> allowRequestAntPathPatterns) {
		Optional<String> allowRequestAntPathPatternOptional = LangUtils.getStream(allowRequestAntPathPatterns)
				.filter(patter -> ANT_PATH_MATCHER.match(patter, requestMappingPath)).findAny();
		return allowRequestAntPathPatternOptional.isPresent();
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

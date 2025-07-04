/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.apoollo.commons.util.JwtUtils.Renewal;
import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.Instances;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserManager;
import com.apoollo.commons.util.request.context.access.core.DefaultUser.SerializableUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
public class DefaultUserManager implements UserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserManager.class);

	private ObjectMapper objectMapper;
	private Instances instances;
	private StringRedisTemplate redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;

	private Map<String, User> users;

	public DefaultUserManager(ObjectMapper objectMapper, Instances instances, StringRedisTemplate redisTemplate,
			RedisNameSpaceKey redisNameSpaceKey, List<SerializableUser> users) {
		super();
		this.objectMapper = objectMapper;
		this.instances = instances;
		this.redisTemplate = redisTemplate;
		this.redisNameSpaceKey = redisNameSpaceKey;
		this.users = new HashMap<>();
		LangUtils.getStream(users).forEach(serializableUser -> {
			this.users.put(serializableUser.getAccessKey(), DefaultUser.toUser(instances, serializableUser));
		});
	}

	protected SerializableUser getUserFromRedis(String key) {
		String user = redisTemplate.opsForValue().get(key);
		try {
			return null == user ? null : objectMapper.readValue(user, SerializableUser.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	protected User getUserFromConfig(String accessKey) {
		return users.get(accessKey);
	}

	protected String getUserRedisKey(String accessKey) {
		return redisNameSpaceKey.getKey(RedisNameSpaceKey.USER, accessKey);
	}

	@Override
	// @Cacheable(value = UserManager.CACHE_NAME, sync = true)
	public User getUser(String accessKey) {
		long startTimestamp = System.currentTimeMillis();
		User user = getUserFromConfig(accessKey);
		if (null == user) {
			SerializableUser serializableUser = getUserFromRedis(getUserRedisKey(accessKey));
			if (null != serializableUser) {
				user = DefaultUser.toUser(instances, serializableUser);
			}
		}
		long endTimestamp = System.currentTimeMillis();
		LOGGER.info("getUser elapsedTime：" + (endTimestamp - startTimestamp) + "ms");
		return user;
	}

	@Override
	public void setUser(SerializableUser user, Long timeout, TimeUnit timeUnit) {
		String key = getUserRedisKey(user.getAccessKey());
		String value;
		try {
			value = objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		if (null != timeout && null != timeUnit && 0 != timeout) {
			redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
		} else {
			redisTemplate.opsForValue().set(key, value);
		}
	}

	@Override
	public void delete(String accessKey) {
		String key = getUserRedisKey(accessKey);
		redisTemplate.delete(key);
	}

	@Override
	public void renewal(String accessKey, Renewal renewal) {
		String key = getUserRedisKey(accessKey);
		redisTemplate.expireAt(key, renewal.getRenewalExpiresAt());
	}

	public void refresh(String accessKey, Boolean enable, String secretKey) {
		Validate.isTrue(null != enable || null != secretKey, "enable or secret must not be null of one");
		String key = getUserRedisKey(accessKey);
		SerializableUser user = getUserFromRedis(key);
		if (null != user) {
			if (null != enable) {
				user.setEnable(enable);
			}
			if (StringUtils.isNotBlank(secretKey)) {
				user.setSecretKey(secretKey);
			}
			setUser(user, redisTemplate.getExpire(key, TimeUnit.SECONDS), TimeUnit.SECONDS);
		}
	}

	@Override
	public void refresh(String accessKey, Boolean enable) {
		refresh(accessKey, enable, null);

	}

	@Override
	public void refresh(String accessKey, String secretKey) {
		refresh(accessKey, null, secretKey);
	}

}

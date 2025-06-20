/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;
import com.apoollo.commons.util.request.context.Instances;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.RequestResourceManager;
import com.apoollo.commons.util.request.context.access.core.DefaultRequestResource.SerializableRequestResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author liuyulong
 * @since 2023年9月28日
 */
public class DefaultRequestResourceManager implements RequestResourceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestResourceManager.class);

	private ObjectMapper objectMapper;
	private Instances instances;
	private StringRedisTemplate redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;
	private List<RequestResource> requestResources;

	public DefaultRequestResourceManager(ObjectMapper objectMapper, Instances instances,
			StringRedisTemplate redisTemplate, RedisNameSpaceKey redisNameSpaceKey,
			List<RequestResource> requestResources) {
		super();
		this.objectMapper = objectMapper;
		this.instances = instances;
		this.redisTemplate = redisTemplate;
		this.redisNameSpaceKey = redisNameSpaceKey;
		this.requestResources = requestResources;

	}

	@Override
	// @Cacheable(value = RequestResourceManager.CACHE_NAME, key =
	// "#requestMappingPath", sync = true)
	public RequestResource getRequestResource(String requestMappingPath) {
		long startTimestamp = System.currentTimeMillis();
		RequestResource requestResource = getRequestResourceFromConfig(requestMappingPath);
		if (null == requestResource) {
			requestResource = DefaultRequestResource.toRequestResource(instances,
					getRequestResourceFromRedis(requestMappingPath));
		}
		long endTimestamp = System.currentTimeMillis();
		LOGGER.info("getRequestResource elapsedTime：" + (endTimestamp - startTimestamp) + "ms");
		return requestResource;
	}

	public RequestResource getRequestResourceFromConfig(String requestMappingPath) {
		return LangUtils.getStream(requestResources)//
				.filter(resource -> StringUtils.equals(resource.getRequestMappingPath(), requestMappingPath))//
				.findFirst()//
				.orElse(null);
	}

	public SerializableRequestResource getRequestResourceFromRedis(String requestMappingPath) {
		String key = getRequestResourceRedisKey();
		Object object = redisTemplate.opsForHash().get(key, requestMappingPath);
		try {
			return objectMapper.readValue((String) object, SerializableRequestResource.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getRequestResourceRedisKey() {
		return redisNameSpaceKey.getKey(RedisNameSpaceKey.REQUEST_RESOURCE);
	}

	@Override
	public void setRequestResource(SerializableRequestResource requestResource) {
		try {
			redisTemplate.opsForHash().put(getRequestResourceRedisKey(), requestResource.getRequestMappingPath(),
					objectMapper.writeValueAsString(requestResource));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteRequestResource(String requestMappingPath) {
		redisTemplate.opsForHash().delete(getRequestResourceRedisKey(), requestMappingPath);
	}

}

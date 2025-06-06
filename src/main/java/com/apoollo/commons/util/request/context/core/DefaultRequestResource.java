/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.List;

import com.apoollo.commons.util.request.context.RequestResource;
import com.apoollo.commons.util.web.spring.Instance;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
@Getter
@Setter
public class DefaultRequestResource extends DefaultCapacitySupport implements RequestResource {

	private Boolean enable;
	private String name;
	private String requestMappingPath;
	private AccessStrategy accessStrategy;
	private List<String> roles;

	@Getter
	@Setter
	public static class SerializableRequestResource extends SerializebleCapacitySupport {
		private Boolean enable;
		private String name;
		private String requestMappingPath;
		private AccessStrategy accessStrategy;
		private List<String> roles;
	}

	public static RequestResource toRequestResource(Instance instance,
			SerializableRequestResource serializableRequestResource) {
		DefaultRequestResource requestResource = new DefaultRequestResource();
		requestResource.setEnable(serializableRequestResource.getEnable());
		requestResource.setName(serializableRequestResource.getName());
		requestResource.setRequestMappingPath(serializableRequestResource.getRequestMappingPath());
		requestResource.setAccessStrategy(serializableRequestResource.getAccessStrategy());
		requestResource.setRoles(serializableRequestResource.getRoles());
		DefaultCapacitySupport.evlaute(instance, serializableRequestResource, requestResource);
		return requestResource;
	}

	public static SerializableRequestResource toSerializableRequestResource(RequestResource requestResource) {
		SerializableRequestResource serializableRequestResource = new SerializableRequestResource();
		serializableRequestResource.setEnable(requestResource.getEnable());
		serializableRequestResource.setName(requestResource.getName());
		serializableRequestResource.setRequestMappingPath(requestResource.getRequestMappingPath());
		serializableRequestResource.setAccessStrategy(requestResource.getAccessStrategy());
		serializableRequestResource.setRoles(requestResource.getRoles());
		DefaultCapacitySupport.evlaute(requestResource, serializableRequestResource);
		return serializableRequestResource;
	}

}

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
			SerializableRequestResource source) {
		DefaultRequestResource target = new DefaultRequestResource();
		target.setEnable(source.getEnable());
		target.setName(source.getName());
		target.setRequestMappingPath(source.getRequestMappingPath());
		target.setAccessStrategy(source.getAccessStrategy());
		target.setRoles(source.getRoles());
		DefaultCapacitySupport.evlaute(instance, source, target);
		return target;
	}

	public static SerializableRequestResource toSerializableRequestResource(RequestResource source) {
		SerializableRequestResource target = new SerializableRequestResource();
		target.setEnable(source.getEnable());
		target.setName(source.getName());
		target.setRequestMappingPath(source.getRequestMappingPath());
		target.setAccessStrategy(source.getAccessStrategy());
		target.setRoles(source.getRoles());
		DefaultCapacitySupport.evlaute(source, target);
		return target;
	}

}

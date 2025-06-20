/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;

import com.apoollo.commons.util.request.context.Instances;
import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.core.AccessStrategy;
import com.apoollo.commons.util.request.context.core.DefaultCapacitySupport;

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

	public static RequestResource toRequestResource(Instances instances,
			SerializableRequestResource source) {
		DefaultRequestResource target = new DefaultRequestResource();
		target.setEnable(source.getEnable());
		target.setName(source.getName());
		target.setRequestMappingPath(source.getRequestMappingPath());
		target.setAccessStrategy(source.getAccessStrategy());
		target.setRoles(source.getRoles());
		DefaultCapacitySupport.evlaute(instances, source, target);
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

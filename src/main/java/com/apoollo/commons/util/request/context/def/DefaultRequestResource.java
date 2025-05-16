/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import com.apoollo.commons.util.request.context.RequestResource;
import com.apoollo.commons.util.request.context.RequestResourceAccessStrategy;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
@Getter
@Setter
public class DefaultRequestResource implements RequestResource {

	private Boolean enable;
	private String resourcePin;
	private String name;
	private String requestMappingPath;
	private String accessStrategy;
	private Class<? extends RequestResourceAccessStrategy> customizeAccessStrategyClass;
	private Long limtUserQps;
	private Long limtPlatformQps;
	private String[] roles;
	private Boolean enableSync;
	private Boolean enableBodyDigestValidate;
	private String bodyDigestSecret;

	public DefaultRequestResource() {
	}

	public DefaultRequestResource(Boolean enable, String resourcePin, String name, String requestMappingPath,
			String accessStrategy, Class<? extends RequestResourceAccessStrategy> customizeAccessStrategyClass,
			Long limtUserQps, Long limtPlatformQps, String[] roles, Boolean enableSync,
			Boolean enableBodyDigestValidate, String bodyDigestSecret) {
		super();
		this.enable = enable;
		this.resourcePin = resourcePin;
		this.name = name;
		this.requestMappingPath = requestMappingPath;
		this.accessStrategy = accessStrategy;
		this.customizeAccessStrategyClass = customizeAccessStrategyClass;
		this.limtUserQps = limtUserQps;
		this.limtPlatformQps = limtPlatformQps;
		this.roles = roles;
		this.enableSync = enableSync;
		this.enableBodyDigestValidate = enableBodyDigestValidate;
		this.bodyDigestSecret = bodyDigestSecret;
	}

}

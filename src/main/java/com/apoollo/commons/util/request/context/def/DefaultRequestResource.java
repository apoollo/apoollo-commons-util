/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.HttpCodeNameHandler;
import com.apoollo.commons.util.request.context.RequestResource;
import com.apoollo.commons.util.request.context.SignatureDecryptor;

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
	private AccessStrategy accessStrategy;
	private Long limtUserQps;
	private Long limtPlatformQps;
	private String[] roles;
	private Boolean enableSync;
	private Boolean enableBodySignatureValidate;
	private String bodySignatureDecyptorSecret;
	private SignatureDecryptor bodySignatureDecyptor;
	private Boolean enableContentEscape;
	private EscapeMethod contentEscapeMethod;
	private HttpCodeNameHandler httpCodeNameHandler;

	public DefaultRequestResource() {
	}

	public DefaultRequestResource(Boolean enable, String resourcePin, String name, String requestMappingPath,
			AccessStrategy accessStrategy, Long limtUserQps, Long limtPlatformQps, String[] roles, Boolean enableSync,
			Boolean enableBodySignatureValidate, String bodySignatureDecyptorSecret,
			SignatureDecryptor bodySignatureDecyptor, Boolean enableContentEscape, EscapeMethod contentEscapeMethod,
			HttpCodeNameHandler httpCodeNameHandler) {
		super();
		this.enable = enable;
		this.resourcePin = resourcePin;
		this.name = name;
		this.requestMappingPath = requestMappingPath;
		this.accessStrategy = accessStrategy;
		this.limtUserQps = limtUserQps;
		this.limtPlatformQps = limtPlatformQps;
		this.roles = roles;
		this.enableSync = enableSync;
		this.enableBodySignatureValidate = enableBodySignatureValidate;
		this.bodySignatureDecyptorSecret = bodySignatureDecyptorSecret;
		this.bodySignatureDecyptor = bodySignatureDecyptor;
		this.enableContentEscape = enableContentEscape;
		this.contentEscapeMethod = contentEscapeMethod;
		this.httpCodeNameHandler = httpCodeNameHandler;
	}

}

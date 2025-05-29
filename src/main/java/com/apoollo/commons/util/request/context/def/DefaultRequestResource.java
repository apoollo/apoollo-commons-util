/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import java.util.List;

import com.apoollo.commons.util.request.context.EscapeMethod;
import com.apoollo.commons.util.request.context.WrapResponseHandler;
import com.apoollo.commons.util.request.context.RequestResource;

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
	private Boolean enableSignature;
	private String signatureSecret;
	private List<String> signatureExcludeHeaderNames;
	private List<String> signatureIncludeHeaderNames;
	private Boolean enableContentEscape;
	private EscapeMethod contentEscapeMethod;
	private Boolean enableResponseWrapper;
	private WrapResponseHandler wrapResponseHandler;

}

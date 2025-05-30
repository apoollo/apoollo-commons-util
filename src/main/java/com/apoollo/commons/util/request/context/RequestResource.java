/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.List;

import com.apoollo.commons.util.request.context.def.AccessStrategy;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
public interface RequestResource {

	public Boolean getEnable();

	public String getResourcePin();

	public String getName();

	public String getRequestMappingPath();

	public AccessStrategy getAccessStrategy();

	public Long getLimtUserQps();

	public Long getLimtPlatformQps();

	public String[] getRoles();

	public Boolean getEnableSync();
	
	public Boolean getEnableNonce();
	
	public Long getNonceDuration();
	
	public NonceValidator getNonceValidator();
	
	public Boolean getEnableSignature();
	
	public String getSignatureSecret();
	
	public List<String> getSignatureExcludeHeaderNames();
	
	public List<String> getSignatureIncludeHeaderNames();
	
	public Boolean getEnableContentEscape();
	
	public EscapeMethod getContentEscapeMethod();
	
	public Boolean getEnableResponseWrapper();
	
	public WrapResponseHandler getWrapResponseHandler();
	
}

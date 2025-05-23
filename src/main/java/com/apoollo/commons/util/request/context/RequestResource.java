/**
 * 
 */
package com.apoollo.commons.util.request.context;

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
	
	public Boolean getEnableBodySignatureValidate();
	
	public String getBodySignatureDecyptorSecret();
	
	public SignatureDecryptor getBodySignatureDecyptor();
	
	public Boolean getEnableContentEscape();
	
	public EscapeMethod getContentEscapeMethod();
	
	public HttpCodeNameHandler getHttpCodeNameHandler();
	
}

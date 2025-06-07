/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.List;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
public interface User extends UserKeyPair, CapacitySupport {
	
	public Boolean getEnable();

	public String getId();

	public String getSecretKeySaltValue();

	public String getUsername();

	public String getUserType();

	public String getUserTypeName();

	public Boolean getEnableRenewal();

	public List<String> getRoles();

	public List<String> getAllowRequestAntPathPatterns();

	public Long getPasswordExpireTime();

	public Object getAttachement();

	public default boolean passwordIsExpired() {
		return null != getPasswordExpireTime() && getPasswordExpireTime() < System.currentTimeMillis();

	}
}

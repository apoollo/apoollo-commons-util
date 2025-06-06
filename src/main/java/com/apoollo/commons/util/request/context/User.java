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

	public String getId();

	public Boolean getEnable();

	public String getSecretKeySaltValue();

	public String getUsername();

	public String getUserType();

	public String getUserTypeName();

	public Boolean getEnableRenewal();

	public List<String> getRoles();

	public List<String> getAllowRequestAntPathPatterns();

	public Long getChangePasswordExpireTime();

	public Object getAttachement();

	public default boolean needResetPassword() {
		return null != getChangePasswordExpireTime() && getChangePasswordExpireTime() < System.currentTimeMillis();

	}
}

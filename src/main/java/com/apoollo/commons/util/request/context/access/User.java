/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import java.util.List;

import com.apoollo.commons.util.request.context.limiter.support.CapacitySupport;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
public interface User extends CapacitySupport {

	public Boolean getEnable();

	public String getId();

	public String getAccessKey();

	public String getSecretKey();

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

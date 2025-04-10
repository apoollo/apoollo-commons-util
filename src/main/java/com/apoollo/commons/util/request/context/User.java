/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.Date;
import java.util.List;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
public interface User extends UserKeyPair {

	public String getId();

	public Boolean getEnable();

	public default List<String> getIpWhiteList() {
		return null;
	}

	public default String getUsername() {
		return null;
	}

	public default String getUserType() {
		return null;
	}

	public default String getUserTypeName() {
		return null;
	}

	public default Boolean getAllowRenewal() {
		return false;
	}

	public default List<String> getRoles() {
		return null;
	}

	public default List<String> getAllowRequestAntPathPatterns() {
		return null;
	}

	public default Object getAttachement() {
		return null;
	}

	public default Date getEnableChangePasswordExpireDate() {
		return null;
	}

	public default boolean needResetPassword() {
		return null != getEnableChangePasswordExpireDate()
				&& getEnableChangePasswordExpireDate().getTime() < System.currentTimeMillis();

	}
}

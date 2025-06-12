/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import com.apoollo.commons.util.request.context.access.UserRequestResourceMatcher.UserMatchesRequestResourceCondition;
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

	public UserMatchesRequestResourceCondition getAuthorizationCondition();

	public Long getPasswordExpireTime();

	public Object getAttachement();

	public UserMatchesRequestResourceCondition getNonceLimiterCondition();

	public UserMatchesRequestResourceCondition getSignatureLimiterCondition();

	public UserMatchesRequestResourceCondition getCorsLimiterCondition();

	public UserMatchesRequestResourceCondition getIpLimiterCondition();

	public UserMatchesRequestResourceCondition getRefererLimiterCondition();

	public UserMatchesRequestResourceCondition getSyncLimiterCondition();

	public UserMatchesRequestResourceCondition getFlowLimiterCondition();

	public UserMatchesRequestResourceCondition getCountLimiterCondition();

	public default boolean passwordIsExpired() {
		return null != getPasswordExpireTime() && getPasswordExpireTime() < System.currentTimeMillis();

	}
}

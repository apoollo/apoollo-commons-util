/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.request.context.Instances;
import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.access.UserRequestResourceMatcher.UserMatchesRequestResourceCondition;
import com.apoollo.commons.util.request.context.core.DefaultCapacitySupport;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
@Getter
@Setter
public class DefaultUser extends DefaultCapacitySupport implements User {

	private Boolean enable;
	private String id;
	private String secretKey;
	private String secretKeySsoSalt;
	private String username;
	private String userType;
	private String userTypeName;
	private Boolean enableRenewal;
	private UserMatchesRequestResourceCondition authorizationCondition;
	private Long passwordValidMillis;
	private Long passwordLastUpdateTimestamp;
	private Object attachement;
	private UserMatchesRequestResourceCondition nonceLimiterCondition;
	private UserMatchesRequestResourceCondition signatureLimiterCondition;
	private UserMatchesRequestResourceCondition corsLimiterCondition;
	private UserMatchesRequestResourceCondition ipLimiterCondition;
	private UserMatchesRequestResourceCondition refererLimiterCondition;
	private UserMatchesRequestResourceCondition syncLimiterCondition;
	private UserMatchesRequestResourceCondition flowLimiterCondition;
	private UserMatchesRequestResourceCondition countLimiterCondition;

	@Getter
	@Setter
	public static class SerializableUser extends SerializebleCapacitySupport {
		private Boolean enable;
		private String id;
		private String secretKey;
		private String secretKeySsoSalt;
		private String username;
		private String userType;
		private String userTypeName;
		private Boolean enableRenewal;
		private UserMatchesRequestResourceCondition authorizationCondition;
		private Long passwordValidMillis;
		private Long passwordLastUpdateTimestamp;
		private Object attachement;
		private UserMatchesRequestResourceCondition nonceLimiterCondition;
		private UserMatchesRequestResourceCondition signatureLimiterCondition;
		private UserMatchesRequestResourceCondition corsLimiterCondition;
		private UserMatchesRequestResourceCondition ipLimiterCondition;
		private UserMatchesRequestResourceCondition refererLimiterCondition;
		private UserMatchesRequestResourceCondition syncLimiterCondition;
		private UserMatchesRequestResourceCondition flowLimiterCondition;
		private UserMatchesRequestResourceCondition countLimiterCondition;
	}

	public static User toUser(Instances instances, SerializableUser source) {
		DefaultUser target = new DefaultUser();
		target.setEnable(source.getEnable());
		target.setId(source.getId());
		target.setSecretKey(source.getSecretKey());
		target.setSecretKeySsoSalt(source.getSecretKeySsoSalt());
		target.setUsername(source.getUsername());
		target.setUserType(source.getUserType());
		target.setUserTypeName(source.getUserTypeName());
		target.setEnableRenewal(source.getEnableRenewal());
		target.setAuthorizationCondition(source.getAuthorizationCondition());
		target.setPasswordLastUpdateTimestamp(source.getPasswordLastUpdateTimestamp());
		target.setPasswordValidMillis(source.getPasswordValidMillis());
		target.setAttachement(source.getAttachement());
		target.setNonceLimiterCondition(source.getNonceLimiterCondition());
		target.setSignatureLimiterCondition(source.getSignatureLimiterCondition());
		target.setCorsLimiterCondition(source.getCorsLimiterCondition());
		target.setIpLimiterCondition(source.getIpLimiterCondition());
		target.setRefererLimiterCondition(source.getRefererLimiterCondition());
		target.setSyncLimiterCondition(source.getSyncLimiterCondition());
		target.setFlowLimiterCondition(source.getFlowLimiterCondition());
		target.setCountLimiterCondition(source.getCountLimiterCondition());
		DefaultCapacitySupport.evlaute(instances, source, target);
		return target;
	}

	public static SerializableUser toSerializableUser(User source) {
		SerializableUser target = new SerializableUser();
		target.setEnable(source.getEnable());
		target.setId(source.getId());
		target.setSecretKey(source.getSecretKey());
		target.setSecretKeySsoSalt(source.getSecretKeySsoSalt());
		target.setUsername(source.getUsername());
		target.setUserType(source.getUserType());
		target.setUserTypeName(source.getUserTypeName());
		target.setEnableRenewal(source.getEnableRenewal());
		target.setAuthorizationCondition(source.getAuthorizationCondition());
		target.setPasswordLastUpdateTimestamp(source.getPasswordLastUpdateTimestamp());
		target.setPasswordValidMillis(source.getPasswordValidMillis());
		target.setAttachement(source.getAttachement());
		target.setNonceLimiterCondition(source.getNonceLimiterCondition());
		target.setSignatureLimiterCondition(source.getSignatureLimiterCondition());
		target.setCorsLimiterCondition(source.getCorsLimiterCondition());
		target.setIpLimiterCondition(source.getIpLimiterCondition());
		target.setRefererLimiterCondition(source.getRefererLimiterCondition());
		target.setSyncLimiterCondition(source.getSyncLimiterCondition());
		target.setFlowLimiterCondition(source.getFlowLimiterCondition());
		target.setCountLimiterCondition(source.getCountLimiterCondition());
		DefaultCapacitySupport.evlaute(source, target);
		return target;
	}

}

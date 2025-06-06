/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.List;

import com.apoollo.commons.util.request.context.User;
import com.apoollo.commons.util.web.spring.Instance;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
@Getter
@Setter
public class DefaultUser extends DefaultCapacitySupport implements User {

	private String id;
	private Boolean enable;
	private String secretKey;
	private String secretKeySaltValue;
	private String username;
	private String userType;
	private String userTypeName;
	private Boolean enableRenewal;
	private List<String> roles;
	private List<String> allowRequestAntPathPatterns;
	private Long changePasswordExpireTime;
	private Object attachement;

	@Getter
	@Setter
	public static class SerializableUser extends SerializebleCapacitySupport {
		private String id;
		private Boolean enable;
		private String secretKey;
		private String secretKeySaltValue;
		private String username;
		private String userType;
		private String userTypeName;
		private Boolean enableRenewal;
		private List<String> roles;
		private List<String> allowRequestAntPathPatterns;
		private Long changePasswordExpireTime;
		private Object attachement;
	}

	public static User toUser(Instance instance, SerializableUser serializableUser) {
		DefaultUser user = new DefaultUser();
		user.setId(serializableUser.getId());
		user.setEnable(serializableUser.getEnable());
		user.setSecretKey(serializableUser.getSecretKey());
		user.setSecretKeySaltValue(serializableUser.getSecretKeySaltValue());
		user.setUsername(serializableUser.getUsername());
		user.setUserType(serializableUser.getUserType());
		user.setUserTypeName(serializableUser.getUserTypeName());
		user.setEnableRenewal(serializableUser.getEnableRenewal());
		user.setRoles(serializableUser.getRoles());
		user.setAllowRequestAntPathPatterns(serializableUser.getAllowRequestAntPathPatterns());
		user.setChangePasswordExpireTime(serializableUser.getChangePasswordExpireTime());
		user.setAttachement(serializableUser.getAttachement());
		DefaultCapacitySupport.evlaute(instance, serializableUser, user);
		return user;
	}

	public static SerializableUser toSerializableUser(User user) {
		SerializableUser serializableUser = new SerializableUser();
		serializableUser.setId(user.getId());
		serializableUser.setEnable(user.getEnable());
		serializableUser.setSecretKey(user.getSecretKey());
		serializableUser.setSecretKeySaltValue(user.getSecretKeySaltValue());
		serializableUser.setUsername(user.getUsername());
		serializableUser.setUserType(user.getUserType());
		serializableUser.setUserTypeName(user.getUserTypeName());
		serializableUser.setEnableRenewal(user.getEnableRenewal());
		serializableUser.setRoles(user.getRoles());
		serializableUser.setAllowRequestAntPathPatterns(user.getAllowRequestAntPathPatterns());
		serializableUser.setChangePasswordExpireTime(user.getChangePasswordExpireTime());
		serializableUser.setAttachement(user.getAttachement());
		DefaultCapacitySupport.evlaute(user, serializableUser);
		return serializableUser;
	}

}

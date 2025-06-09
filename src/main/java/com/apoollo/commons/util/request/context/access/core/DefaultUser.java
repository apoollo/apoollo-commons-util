/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;

import com.apoollo.commons.util.request.context.access.User;
import com.apoollo.commons.util.request.context.core.DefaultCapacitySupport;
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

	private Boolean enable;
	private String id;
	private String secretKey;
	private String secretKeySaltValue;
	private String username;
	private String userType;
	private String userTypeName;
	private Boolean enableRenewal;
	private List<String> roles;
	private List<String> allowRequestAntPathPatterns;
	private Long passwordExpireTime;
	private Object attachement;

	@Getter
	@Setter
	public static class SerializableUser extends SerializebleCapacitySupport {
		private Boolean enable;
		private String id;
		private String secretKey;
		private String secretKeySaltValue;
		private String username;
		private String userType;
		private String userTypeName;
		private Boolean enableRenewal;
		private List<String> roles;
		private List<String> allowRequestAntPathPatterns;
		private Long passwordExpireTime;
		private Object attachement;
	}

	public static User toUser(Instance instance, SerializableUser source) {
		DefaultUser target = new DefaultUser();
		target.setEnable(source.getEnable());
		target.setId(source.getId());
		target.setSecretKey(source.getSecretKey());
		target.setSecretKeySaltValue(source.getSecretKeySaltValue());
		target.setUsername(source.getUsername());
		target.setUserType(source.getUserType());
		target.setUserTypeName(source.getUserTypeName());
		target.setEnableRenewal(source.getEnableRenewal());
		target.setRoles(source.getRoles());
		target.setAllowRequestAntPathPatterns(source.getAllowRequestAntPathPatterns());
		target.setPasswordExpireTime(source.getPasswordExpireTime());
		target.setAttachement(source.getAttachement());
		DefaultCapacitySupport.evlaute(instance, source, target);
		return target;
	}

	public static SerializableUser toSerializableUser(User source) {
		SerializableUser target = new SerializableUser();
		target.setEnable(source.getEnable());
		target.setId(source.getId());
		target.setSecretKey(source.getSecretKey());
		target.setSecretKeySaltValue(source.getSecretKeySaltValue());
		target.setUsername(source.getUsername());
		target.setUserType(source.getUserType());
		target.setUserTypeName(source.getUserTypeName());
		target.setEnableRenewal(source.getEnableRenewal());
		target.setRoles(source.getRoles());
		target.setAllowRequestAntPathPatterns(source.getAllowRequestAntPathPatterns());
		target.setPasswordExpireTime(source.getPasswordExpireTime());
		target.setAttachement(source.getAttachement());
		DefaultCapacitySupport.evlaute(source, target);
		return target;
	}

}

/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.util.Date;
import java.util.List;

import com.apoollo.commons.util.request.context.User;

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
	private Date changePasswordExpireDate;
	private Object attachement;
	
}

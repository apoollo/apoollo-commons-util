/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import java.util.Date;
import java.util.List;

import com.apoollo.commons.util.request.context.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年8月30日
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUser implements User {

	private String id;
	private Boolean enable;
	private String accessKey;
	private String secretKey;
	private String secretKeySaltValue;
	private Boolean allowRenewal;
	private List<String> ipWhiteList;
	private List<String> allowRequestAntPathPatterns;
	private List<String> roles;
	private Object attachement;
	private Date enableChangePasswordExpireDate;
}

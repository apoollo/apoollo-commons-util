/**
 * 
 */
package com.apoollo.commons.util.filesystem.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class RemoteLoginProperties {

	private String host;
	private String physicalHost;
	private Integer port;
	
	
	private String username;
	private String password;
	
	private String rootUserName;
	private String rootPassword;
}

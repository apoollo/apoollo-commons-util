/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 */
public interface UserKeyPair {
	

	public String getAccessKey();
	
	public String getSecretKey();
	
	public String getSecretKeySaltValue();
}

/**
 * 
 */
package com.apoollo.commons.util.exception.refactor;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationJwtTokenExpiredException extends AppException {

	private static final long serialVersionUID = 1208875468214968303L;

	public AppAuthenticationJwtTokenExpiredException(String message) {
		super(message);
	}

}

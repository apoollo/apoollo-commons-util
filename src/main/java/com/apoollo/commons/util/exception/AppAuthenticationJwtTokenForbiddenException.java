/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationJwtTokenForbiddenException extends AppException {

	private static final long serialVersionUID = -6008711066057386078L;

	public AppAuthenticationJwtTokenForbiddenException(String message) {
		super(message);
	}

	public AppAuthenticationJwtTokenForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

}

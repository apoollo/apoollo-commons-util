/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthorizationForbiddenException extends AppException {

	private static final long serialVersionUID = 2205416146990645920L;

	public AppAuthorizationForbiddenException(String message) {
		super(message);
	}

	public AppAuthorizationForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

}

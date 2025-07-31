/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationUserPasswordExpiredException extends AppException {


	private static final long serialVersionUID = 3910140504993439293L;

	public AppAuthenticationUserPasswordExpiredException(String message) {
		super(message);
	}

	public AppAuthenticationUserPasswordExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

}

/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppUserPasswordExpiredException extends AppException {


	private static final long serialVersionUID = 3910140504993439293L;

	public AppUserPasswordExpiredException(String message) {
		super(message);
	}

	public AppUserPasswordExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

}

/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationUserDisabledException extends AppException {

	private static final long serialVersionUID = -564401350050359029L;

	public AppAuthenticationUserDisabledException(String message) {
		super(message);
	}

	public AppAuthenticationUserDisabledException(String message, Throwable cause) {
		super(message, cause);
	}

}

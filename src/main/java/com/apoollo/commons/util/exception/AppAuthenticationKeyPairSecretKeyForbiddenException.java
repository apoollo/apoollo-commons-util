/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationKeyPairSecretKeyForbiddenException extends AppException {

	private static final long serialVersionUID = 7824276573124583109L;

	public AppAuthenticationKeyPairSecretKeyForbiddenException(String message) {
		super(message);
	}

	public AppAuthenticationKeyPairSecretKeyForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

}

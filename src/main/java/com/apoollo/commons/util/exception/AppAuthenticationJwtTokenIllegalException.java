/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationJwtTokenIllegalException extends AppException {

	private static final long serialVersionUID = -8940509572641039549L;

	public AppAuthenticationJwtTokenIllegalException(String message) {
		super(message);
	}

	public AppAuthenticationJwtTokenIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

}

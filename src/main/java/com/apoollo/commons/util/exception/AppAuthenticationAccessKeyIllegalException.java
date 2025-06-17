/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppAuthenticationAccessKeyIllegalException extends AppException {

	private static final long serialVersionUID = 5252361527475715755L;

	public AppAuthenticationAccessKeyIllegalException(String message) {
		super(message);
	}

	public AppAuthenticationAccessKeyIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

}

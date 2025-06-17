/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppSignatureLimiterSignatureIllegalException extends AppException {

	private static final long serialVersionUID = 7966149297352010246L;

	public AppSignatureLimiterSignatureIllegalException(String message) {
		super(message);
	}

	public AppSignatureLimiterSignatureIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

}

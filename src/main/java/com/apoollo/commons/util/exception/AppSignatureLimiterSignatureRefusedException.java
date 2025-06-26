/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppSignatureLimiterSignatureRefusedException extends AppException {

	private static final long serialVersionUID = 7966149297352010246L;

	public AppSignatureLimiterSignatureRefusedException(String message) {
		super(message);
	}

	public AppSignatureLimiterSignatureRefusedException(String message, Throwable cause) {
		super(message, cause);
	}

}

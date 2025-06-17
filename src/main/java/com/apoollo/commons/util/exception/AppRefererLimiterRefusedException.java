/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppRefererLimiterRefusedException extends AppException {

	private static final long serialVersionUID = 5962132814257864863L;

	public AppRefererLimiterRefusedException(String message) {
		super(message);
	}

	public AppRefererLimiterRefusedException(String message, Throwable cause) {
		super(message, cause);
	}

}

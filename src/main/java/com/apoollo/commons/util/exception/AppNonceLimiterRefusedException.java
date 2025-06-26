/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppNonceLimiterRefusedException extends AppException {

	private static final long serialVersionUID = -7485230020709579058L;

	public AppNonceLimiterRefusedException(String message) {
		super(message);
	}

	public AppNonceLimiterRefusedException(String message, Throwable cause) {
		super(message, cause);
	}

}

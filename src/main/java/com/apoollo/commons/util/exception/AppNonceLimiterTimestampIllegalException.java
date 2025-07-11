/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppNonceLimiterTimestampIllegalException extends AppException {

	private static final long serialVersionUID = -6558246938938923235L;

	public AppNonceLimiterTimestampIllegalException(String message) {
		super(message);
	}

	public AppNonceLimiterTimestampIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

}

/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppSyncLimiterRefusedException extends AppException {

	private static final long serialVersionUID = 8997085770218029362L;

	public AppSyncLimiterRefusedException(String message) {
		super(message);
	}

	public AppSyncLimiterRefusedException(String message, Throwable cause) {
		super(message, cause);
	}

}

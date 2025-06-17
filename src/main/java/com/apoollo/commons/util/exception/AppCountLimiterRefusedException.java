/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppCountLimiterRefusedException extends AppException {

	private static final long serialVersionUID = 4254903324127754777L;

	public AppCountLimiterRefusedException(String message) {
		super(message);
	}

	public AppCountLimiterRefusedException(String message, Throwable cause) {
		super(message, cause);
	}

}

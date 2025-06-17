/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppIpLimiterIncludeListRefusedException extends AppException {

	private static final long serialVersionUID = 1646440727434233839L;

	public AppIpLimiterIncludeListRefusedException(String message) {
		super(message);
	}

	public AppIpLimiterIncludeListRefusedException(String message, Throwable cause) {
		super(message, cause);
	}

}

/**
 * 
 */
package com.apoollo.commons.util.exception.refactor;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppCorsLimiterRefusedException extends AppException {

	private static final long serialVersionUID = 5946733840147022594L;

	public AppCorsLimiterRefusedException(String message) {
		super(message);
	}

}

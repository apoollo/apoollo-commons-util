/**
 * 
 */
package com.apoollo.commons.util.exception.refactor;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppIpLimiterExcludeListRefusedException extends AppException {

	private static final long serialVersionUID = 1201087544352271257L;

	public AppIpLimiterExcludeListRefusedException(String message) {
		super(message);
	}

}

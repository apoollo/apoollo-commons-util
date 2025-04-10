/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 */
public class AppRequestTimeoutLimitException extends AppException {

	private static final long serialVersionUID = -7929848468076511755L;

	public AppRequestTimeoutLimitException(String message) {
		super(message);
	}

	public AppRequestTimeoutLimitException(String message, Throwable cause) {
		super(message, cause);
	}
}

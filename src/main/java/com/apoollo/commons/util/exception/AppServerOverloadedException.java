/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-17
 */
public class AppServerOverloadedException extends AppException {

	private static final long serialVersionUID = -3828948199021995086L;

	public AppServerOverloadedException(String message) {
		super(message);
	}

	public AppServerOverloadedException(String message, Throwable cause) {
		super(message, cause);
	}

}

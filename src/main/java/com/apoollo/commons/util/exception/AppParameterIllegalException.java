/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-17
 */
public class AppParameterIllegalException extends AppException {

	private static final long serialVersionUID = 8426543332641610713L;

	public AppParameterIllegalException(String message) {
		super(message);
	}

	public AppParameterIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

}

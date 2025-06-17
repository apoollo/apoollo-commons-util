/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 */
public class AppRequestResourceNotExistsException extends AppException {

	private static final long serialVersionUID = -3524003605238596279L;

	public AppRequestResourceNotExistsException(String message) {
		super(message);
	}

	public AppRequestResourceNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}

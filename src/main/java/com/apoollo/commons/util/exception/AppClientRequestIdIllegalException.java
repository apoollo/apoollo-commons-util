/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppClientRequestIdIllegalException extends AppException {

	private static final long serialVersionUID = -236593893782122192L;

	public AppClientRequestIdIllegalException(String message) {
		super(message);
	}

	public AppClientRequestIdIllegalException(String message, Throwable cause) {
		super(message, cause);
	}

}

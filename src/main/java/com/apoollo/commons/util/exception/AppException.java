/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 */
public class AppException extends RuntimeException {

	private static final long serialVersionUID = -2145073965547797367L;

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

}

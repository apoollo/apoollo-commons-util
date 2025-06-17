/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 */
public class AppRequestResourceDisabledException extends AppException {

	private static final long serialVersionUID = -1294884186887927505L;

	public AppRequestResourceDisabledException(String message) {
		super(message);
	}

	public AppRequestResourceDisabledException(String message, Throwable cause) {
		super(message, cause);
	}

}

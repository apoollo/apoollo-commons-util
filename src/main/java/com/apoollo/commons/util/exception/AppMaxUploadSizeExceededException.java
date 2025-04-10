/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 */
public class AppMaxUploadSizeExceededException extends AppException {

	private static final long serialVersionUID = 369869592664749913L;

	public AppMaxUploadSizeExceededException() {

	}

	public AppMaxUploadSizeExceededException(String message) {
		super(message);
	}
}

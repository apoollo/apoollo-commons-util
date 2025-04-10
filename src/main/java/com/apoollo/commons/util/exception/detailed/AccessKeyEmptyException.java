/**
 * 
 */
package com.apoollo.commons.util.exception.detailed;

import com.apoollo.commons.util.exception.AppException;

/**
 * @author liuyulong
 */
public class AccessKeyEmptyException extends AppException {

	private static final long serialVersionUID = -863259687426989617L;

	public AccessKeyEmptyException() {
	}

	public AccessKeyEmptyException(String message) {
		super(message);
	}

}

/**
 * 
 */
package com.apoollo.commons.util.exception.detailed;

import com.apoollo.commons.util.exception.AppException;

/**
 * @author liuyulong
 */
public class TimeoutIllegalArgumentException extends AppException {

	private static final long serialVersionUID = 9008208774721766385L;

	public TimeoutIllegalArgumentException() {
	}

	public TimeoutIllegalArgumentException(String message) {
		super(message);
	}

}

/**
 * 
 */
package com.apoollo.commons.util.exception;

/**
 * @author liuyulong
 */
public class AppExceedingDailyMaximumUseTimesLimitException extends AppException {

	private static final long serialVersionUID = 1905145404684369269L;

	public AppExceedingDailyMaximumUseTimesLimitException(String message) {
		super(message);
	}

}

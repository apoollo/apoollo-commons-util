/**
 * 
 */
package com.apoollo.commons.util.exception.refactor;

/**
 * @author liuyulong
 * @since 2025-06-16
 */
public class AppFlowLimiterRefusedException extends AppException {

	private static final long serialVersionUID = -6511654811642010116L;

	public AppFlowLimiterRefusedException(String message) {
		super(message);
	}

}

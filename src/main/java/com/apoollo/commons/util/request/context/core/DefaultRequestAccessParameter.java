/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import com.apoollo.commons.util.model.MinMax;
import com.apoollo.commons.util.request.context.RequestAccessParameter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class DefaultRequestAccessParameter implements RequestAccessParameter {

	private Long requestTimesPerSecond;
	private Long requestMaximumTimesToday;
	private Long requestDefaultTimeoutMillis;
	private MinMax<Long> requestTimeoutRangeMillis;
	private MinMax<Double> requestThresholdValue;

}

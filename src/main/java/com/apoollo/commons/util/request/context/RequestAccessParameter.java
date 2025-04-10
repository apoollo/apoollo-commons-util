/**
 * 
 */
package com.apoollo.commons.util.request.context;

import com.apoollo.commons.util.model.MinMax;

/**
 * @author liuyulong
 */
public interface RequestAccessParameter {

	public Long getRequestTimesPerSecond();

	public Long getRequestMaximumTimesToday();

	public Long getRequestDefaultTimeoutMillis();

	public MinMax<Long> getRequestTimeoutRangeMillis();

	public MinMax<Double> getRequestThresholdValue();
}

/**
 * 
 */
package com.apoollo.commons.util.chart;

import java.util.List;

/**
 * @author liuyulong
 */
public interface DataStatistcItem {

	public default boolean hasCategory() {
		return null != getCategoryCode();
	}

	public String getCategoryCode();

	public default String getCategoryName() {
		return null;
	}

	public List<DataSeriesItem> getDataSeriesItems();

}

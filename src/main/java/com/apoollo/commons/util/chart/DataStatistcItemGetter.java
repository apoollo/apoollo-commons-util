/**
 * 
 */
package com.apoollo.commons.util.chart;

/**
 * @author liuyulong
 */
public interface DataStatistcItemGetter {

	public String getCategoryCode();

	public default String getCategoryName() {
		return null;
	}

	public String getSeriesItemCode();

	public String getSeriesItemName();

	public Number getValue();
}

/**
 * 
 */
package com.apoollo.commons.util.chart;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistogramChart {

	private List<String> categories;
	private List<SeriesItem> series;
	
}

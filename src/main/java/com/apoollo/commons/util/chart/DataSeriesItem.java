package com.apoollo.commons.util.chart;

import com.apoollo.commons.util.request.context.CodeName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DataSeriesItem implements CodeName<String, String> {

	private String code;
	private String name;
	private Number value;

}

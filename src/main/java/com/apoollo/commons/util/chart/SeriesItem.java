/**
 * 
 */
package com.apoollo.commons.util.chart;

import java.util.ArrayList;
import java.util.List;

import com.apoollo.commons.util.request.context.CodeName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class SeriesItem implements CodeName<String, String> {

	private String code;
	private String name;
	private List<Number> data = new ArrayList<>();

	@Override
	public boolean equals(Object obj) {
		return this.overrideEquals(obj);
	}

	@Override
	public int overrideHashCode() {
		return this.hashCode();
	}

}

/**
 * 
 */
package com.apoollo.commons.util.persistence;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年8月16日
 */
@Getter
@Setter
public class PageOutput<T> {

	private Long totalElements;
	private List<T> content;

	public PageOutput() {
	}

	/**
	 * @param totalElements
	 * @param content
	 */
	public PageOutput(Long totalElements, List<T> content) {
		super();
		this.totalElements = totalElements;
		this.content = content;
	}

	
}

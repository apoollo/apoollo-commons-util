/**
 * 
 */
package com.apoollo.commons.util.model;

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
public class IncludeExclude<T> {

	private List<T> includes;
	private List<T> excludes;
}

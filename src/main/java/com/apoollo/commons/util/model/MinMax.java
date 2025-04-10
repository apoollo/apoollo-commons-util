/**
 * 
 */
package com.apoollo.commons.util.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年8月9日
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinMax<T extends Comparable<? super T>> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2691379700977889361L;
	private T min;
	private T max;
}

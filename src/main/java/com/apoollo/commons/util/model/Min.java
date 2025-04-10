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
@NoArgsConstructor
@AllArgsConstructor
public class Min<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387492655699909797L;
	private T min;
}

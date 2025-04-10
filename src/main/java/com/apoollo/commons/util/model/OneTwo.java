/**
 * 
 */
package com.apoollo.commons.util.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2023年9月14日
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OneTwo<O, T> {

	private O one;
	private T two;
}

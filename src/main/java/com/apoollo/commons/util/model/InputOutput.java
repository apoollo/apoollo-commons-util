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
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputOutput<I,O> {

	private I input;
	private O output;
}

/**
 * 
 */
package com.apoollo.commons.util.filesystem.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
public class CommandResult {

	private String inputStreamAsString;
	private String errorStreamAsString;
}

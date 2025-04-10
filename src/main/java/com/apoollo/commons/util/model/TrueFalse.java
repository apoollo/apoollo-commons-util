/**
 * 
 */
package com.apoollo.commons.util.model;

import lombok.Getter;

/**
 * @author liuyulong
 */
@Getter
public enum TrueFalse {

	TRUE("true", 1, "yes", true), //
	FALSE("false", 0, "no", false);

	private String booleanStringValue;
	private Integer intValue;
	private String yesNoValue;
	private Boolean booleanValue;

	/**
	 * @param booleanStringValue
	 * @param intValue
	 * @param yesNoValue
	 * @param booleanValue
	 */
	private TrueFalse(String booleanStringValue, Integer intValue, String yesNoValue, Boolean booleanValue) {
		this.booleanStringValue = booleanStringValue;
		this.intValue = intValue;
		this.yesNoValue = yesNoValue;
		this.booleanValue = booleanValue;
	}

	public static TrueFalse get(Boolean booleanValue) {
		return Boolean.TRUE.equals(booleanValue) ? TRUE : FALSE;
	}

}

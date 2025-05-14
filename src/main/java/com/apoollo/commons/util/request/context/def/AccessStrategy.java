/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import lombok.Getter;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
@Getter
public enum AccessStrategy {

	PRIVATE_REQUEST("private"), //
	PUBLIC_REQUEST("public"),
	CUSTOM("custom");

	private String accessStrategyPin;

	/**
	 * @param accessStrategyPin
	 */
	private AccessStrategy(String accessStrategyPin) {
		this.accessStrategyPin = accessStrategyPin;
	}

}

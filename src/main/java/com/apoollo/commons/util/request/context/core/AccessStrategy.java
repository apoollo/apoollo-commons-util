/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import lombok.Getter;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
@Getter
public enum AccessStrategy {

	PUBLIC, //
	PRIVATE_HEADER_JWT_TOKEN, //
	PRIVATE_HEADER_KEY_PAIR, //
	PRIVATE_PARAMETER_KEY_PAIR, //
	PRIVATE_JSON_BODY_JWT_TOKEN, //
	PRIVATE_JSON_BODY_KEY_PAIR//
	;

}

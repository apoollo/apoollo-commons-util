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

	PUBLIC, //
	PRIVATE_HEADER_JWT_TOKEN, //
	PRIVATE_HEADER_KEY_PAIR, //
	PRIVATE_PARAMETER_KEY_PAIR, //
	PRIVATE_BODY_JWT_TOKEN, //
	PRIVATE_BODY_KEY_PAIR//
	;

}

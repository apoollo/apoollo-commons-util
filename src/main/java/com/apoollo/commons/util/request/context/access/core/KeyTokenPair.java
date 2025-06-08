/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.request.context.access.TokenPair;

import lombok.Getter;
import lombok.Setter;

/**
 * liuyulong
 */
@Getter
@Setter
public class KeyTokenPair implements TokenPair<String> {

	private String accessKey;
	private String token;
}

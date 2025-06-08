/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.request.context.access.TokenPair;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
public class KeyTokenPair implements TokenPair<String> {

	private String accessKey;
	private String token;
}

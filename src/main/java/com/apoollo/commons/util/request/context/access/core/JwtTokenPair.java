/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import com.apoollo.commons.util.JwtUtils.JwtToken;
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
public class JwtTokenPair implements TokenPair<JwtToken> {

	private String accessKey;
	private JwtToken token;

}

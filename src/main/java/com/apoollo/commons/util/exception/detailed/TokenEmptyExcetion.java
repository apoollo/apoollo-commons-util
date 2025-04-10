/**
 * 
 */
package com.apoollo.commons.util.exception.detailed;

import com.apoollo.commons.util.exception.AppException;

/**
 * @author liuyulong
 */
public class TokenEmptyExcetion extends AppException {

	private static final long serialVersionUID = -5166452311685277527L;

	public TokenEmptyExcetion() {
	}

	public TokenEmptyExcetion(String message) {
		super(message);
	}

}

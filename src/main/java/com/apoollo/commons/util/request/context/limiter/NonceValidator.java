/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter;

/**
 * @author liuyulong
 * @since 2025-05-30
 */
public interface NonceValidator {

	public boolean isValid(String nonce, long duration);
}

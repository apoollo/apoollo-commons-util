/**
 * 
 */
package com.apoollo.commons.util.request.context.model;

/**
 * @author liuyulong
 * @since 2025-06-06
 */
public class RequestConstants {

	public static final String REQUEST_HEADER_REFERER = "referer";

	public static final String REQUEST_HEADER_REQUEST_ID = "request-id";

	public static final String REQUEST_HEADER_TIMESTAMP = "x-timestamp";

	public static final String REQUEST_HEADER_NONCE = "x-nonce";

	public static final String REQUEST_HEADER_SIGNATURE = "x-signature";

	// 版本
	public static final String RESPONSE_HEADER_VERSION = "x-version";

	// 用户密码过期剩余时间
	public static final String RESPONSE_HEADER_USER_PASSWORD_EXPIRE_REMAIN_DURATION = "x-user-password-expire-remain-duration";

	// 续期TOKEN
	public static final String RESPONSE_HEADER_RENEWAL_AUTHORIZATION = "x-renewal-authorization";
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.exception.AppIllegalArgumentException;
import com.apoollo.commons.util.request.context.limiter.NonceLimiter;
import com.apoollo.commons.util.request.context.limiter.NonceValidator;
import com.apoollo.commons.util.request.context.model.RequestConstants;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultNonceLimiter implements NonceLimiter {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNonceLimiter.class);

	private NonceValidator nonceValidator;

	public DefaultNonceLimiter(NonceValidator nonceValidator) {
		super();
		this.nonceValidator = nonceValidator;
	}

	public void limit(HttpServletRequest request, NonceValidator nonceLimiterValidator, Long nonceLimiterDuration) {

		String timestamp = request.getHeader(RequestConstants.REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isBlank(timestamp)) {
			throw new AppIllegalArgumentException(
					"header [" + RequestConstants.REQUEST_HEADER_TIMESTAMP + "] must not be null");
		}
		Long expireTimestampLong = nonceLimiterDuration;
		if (null == expireTimestampLong) {
			throw new RuntimeException("nonceDuration must not be null");
		}
		try {
			expireTimestampLong += Long.valueOf(timestamp);
		} catch (NumberFormatException e) {
			LOGGER.error("parse timestamp error:", e);
			throw new AppIllegalArgumentException("parse timestamp [" + timestamp + "] error");
		}
		long currentTimestamp = System.currentTimeMillis();
		if (currentTimestamp > expireTimestampLong) {
			LOGGER.error("currentTimestamp :" + currentTimestamp + ", expireTimestamp:" + expireTimestampLong);
			throw new AppIllegalArgumentException("timestamp [" + timestamp + "] already expire");
		}
		String nonce = request.getHeader(RequestConstants.REQUEST_HEADER_NONCE);
		if (StringUtils.isBlank(nonce)) {
			throw new AppIllegalArgumentException(
					"header [" + RequestConstants.REQUEST_HEADER_NONCE + "] must not be null");
		}
		NonceValidator nonceValidator = nonceLimiterValidator;
		if (null == nonceValidator) {
			nonceValidator = this.nonceValidator;
		}
		if (!nonceValidator.isValid(nonce, nonceLimiterDuration)) {
			throw new AppIllegalArgumentException("header [" + RequestConstants.REQUEST_HEADER_NONCE + "] invalid");
		}
	}
}

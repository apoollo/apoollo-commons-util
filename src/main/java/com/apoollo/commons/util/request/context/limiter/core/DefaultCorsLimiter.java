/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.io.IOException;

import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;

import com.apoollo.commons.util.exception.AppIllegalArgumentException;
import com.apoollo.commons.util.exception.refactor.AppCorsLimiterRefusedException;
import com.apoollo.commons.util.request.context.limiter.CorsLimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2025-06-05
 */
public class DefaultCorsLimiter implements CorsLimiter {

	private CorsProcessor corsProcessor = new DefaultCorsProcessor() {
		@Override
		protected void rejectRequest(ServerHttpResponse response) throws IOException {
			throw new AppIllegalArgumentException("cors refused");
		}
	};

	@Override
	public void limit(HttpServletRequest request, HttpServletResponse response, CorsConfiguration corsLimiterConfiguration) {
		try {
			if (!corsProcessor.processRequest(corsLimiterConfiguration, request, response)) {
				throw new AppCorsLimiterRefusedException("cors refused");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}

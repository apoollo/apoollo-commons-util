/**
 * 
 */
package com.apoollo.commons.util.request.context.limiter.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.exception.AppAuthenticationAccessKeyIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenExpiredException;
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationKeyPairSecretKeyForbiddenException;
import com.apoollo.commons.util.exception.AppAuthenticationKeyPairTokenIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationTokenIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationUserDisabledException;
import com.apoollo.commons.util.exception.AppAuthorizationForbiddenException;
import com.apoollo.commons.util.exception.AppCorsLimiterRefusedException;
import com.apoollo.commons.util.exception.AppCountLimiterRefusedException;
import com.apoollo.commons.util.exception.AppException;
import com.apoollo.commons.util.exception.AppFlowLimiterRefusedException;
import com.apoollo.commons.util.exception.AppHttpCodeNameMessageException;
import com.apoollo.commons.util.exception.AppIpLimiterExcludeListRefusedException;
import com.apoollo.commons.util.exception.AppIpLimiterIncludeListRefusedException;
import com.apoollo.commons.util.exception.AppNonceLimiterNonceIllegalException;
import com.apoollo.commons.util.exception.AppNonceLimiterTimestampIllegalException;
import com.apoollo.commons.util.exception.AppParameterIllegalException;
import com.apoollo.commons.util.exception.AppRefererLimiterRefusedException;
import com.apoollo.commons.util.exception.AppRequestResourceDisabledException;
import com.apoollo.commons.util.exception.AppRequestResourceNotExistsException;
import com.apoollo.commons.util.exception.AppServerOverloadedException;
import com.apoollo.commons.util.exception.AppSignatureLimiterSignatureIllegalException;
import com.apoollo.commons.util.exception.AppSyncLimiterRefusedException;
import com.apoollo.commons.util.request.context.HttpCodeName;
import com.apoollo.commons.util.request.context.HttpCodeNameMessage;
import com.apoollo.commons.util.request.context.RequestContext;
import com.apoollo.commons.util.request.context.Response;
import com.apoollo.commons.util.request.context.core.DefaultHttpCodeName;
import com.apoollo.commons.util.request.context.core.DefaultHttpCodeNameMessage;
import com.apoollo.commons.util.request.context.limiter.WrapResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public class DefaultWrapResponseHandler implements WrapResponseHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWrapResponseHandler.class);

	private static final Map<Class<? extends ErrorResponse>, HttpCodeName<Integer, String>> ERROR_RESPONSE_EXCEPTION_MAPPING = new HashMap<>() {
		private static final long serialVersionUID = -2202437691642912457L;

		{
			put(HttpRequestMethodNotSupportedException.class,
					new DefaultHttpCodeName<>(4100, "HttpRequestMethodNotSupported", 200));
			put(HttpMediaTypeNotSupportedException.class,
					new DefaultHttpCodeName<>(4101, "HttpMediaTypeNotSupported", 200));
			put(HttpMediaTypeNotAcceptableException.class,
					new DefaultHttpCodeName<>(4102, "HttpMediaTypeNotAcceptable", 200));
			put(MissingPathVariableException.class, new DefaultHttpCodeName<>(4103, "MissingPathVariable", 200));
			put(MissingServletRequestParameterException.class,
					new DefaultHttpCodeName<>(4104, "MissingServletRequestParameter", 200));
			put(MissingServletRequestPartException.class,
					new DefaultHttpCodeName<>(4105, "MissingServletRequestPart", 200));
			put(ServletRequestBindingException.class, new DefaultHttpCodeName<>(4106, "ServletRequestBinding", 200));
			put(MethodArgumentNotValidException.class, new DefaultHttpCodeName<>(4107, "MethodArgumentNotValid", 200));
			put(HandlerMethodValidationException.class,
					new DefaultHttpCodeName<>(4108, "HandlerMethodValidation", 200));
			put(NoHandlerFoundException.class, new DefaultHttpCodeName<>(4109, "NoHandlerFound", 200));
			put(NoResourceFoundException.class, new DefaultHttpCodeName<>(4110, "NoResourceFound", 200));
			put(AsyncRequestTimeoutException.class, new DefaultHttpCodeName<>(4111, "AsyncRequestTimeout", 200));
		}
	};

	private static final Map<Class<? extends Exception>, HttpCodeName<Integer, String>> FRAMEWORK_EXCEPTION_MAPPING = new HashMap<>() {
		private static final long serialVersionUID = 3853334954341313585L;

		{
			put(TypeMismatchException.class, new DefaultHttpCodeName<>(4301, "TypeMismatch", 200));
			put(HttpMessageNotReadableException.class, new DefaultHttpCodeName<>(4302, "HttpMessageNotReadable", 200));
			put(BindException.class, new DefaultHttpCodeName<>(4303, "BindError", 200));
			put(ConversionNotSupportedException.class, new DefaultHttpCodeName<>(5500, "ConversionNotSupported", 200));
			put(HttpMessageNotWritableException.class, new DefaultHttpCodeName<>(5501, "HttpMessageNotWritable", 200));
			put(MethodValidationException.class, new DefaultHttpCodeName<>(5502, "MethodValidation", 200));
			put(AsyncRequestNotUsableException.class, new DefaultHttpCodeName<>(5503, "AsyncRequestNotUsable", 200));
		}
	};

	private static final HttpCodeNameMessage<Integer, String, String> OK = new DefaultHttpCodeNameMessage<>(2000, "Ok",
			200, "success");

	private static final HttpCodeNameMessage<Integer, String, String> SYSTEM_ERROR = new DefaultHttpCodeNameMessage<>(
			5000, "AppSystemError", 200, "app system error");

	private static final Map<Class<? extends AppException>, HttpCodeName<Integer, String>> CODE_NAME_EXCEPTION_MAPPING = new HashMap<>() {
		private static final long serialVersionUID = 8730699429353651670L;

		{
			// request resource
			put(AppRequestResourceNotExistsException.class,
					new DefaultHttpCodeName<>(4500, "RequestResourceNotExists", 200));
			put(AppRequestResourceDisabledException.class, new DefaultHttpCodeName<>(4501, "ResourceDisabled", 200));

			// nonce limiter
			put(AppNonceLimiterNonceIllegalException.class,
					new DefaultHttpCodeName<>(4510, "NonceLimiterNonceIllegal", 200));
			put(AppNonceLimiterTimestampIllegalException.class,
					new DefaultHttpCodeName<>(4511, "NonceLimiterTimestampIllegal", 200));

			// signature limiter
			put(AppSignatureLimiterSignatureIllegalException.class,
					new DefaultHttpCodeName<>(4520, "LimiterSignatureIllegal", 200));

			// cors limiter
			put(AppCorsLimiterRefusedException.class, new DefaultHttpCodeName<>(4530, "CorsLimiterRefused", 200));

			// ip limiter
			put(AppIpLimiterExcludeListRefusedException.class,
					new DefaultHttpCodeName<>(4540, "IpLimiterExcludeListRefused", 200));
			put(AppIpLimiterIncludeListRefusedException.class,
					new DefaultHttpCodeName<>(4541, "IpLimiterIncludeListRefused", 200));

			// referer limiter
			put(AppRefererLimiterRefusedException.class, new DefaultHttpCodeName<>(4550, "RefererLimiterRefused", 200));

			// sync limiter
			put(AppSyncLimiterRefusedException.class, new DefaultHttpCodeName<>(4560, "SyncLimiterRefused", 200));

			// flow limiter
			put(AppFlowLimiterRefusedException.class, new DefaultHttpCodeName<>(4570, "FlowLimiterRefused", 200));

			// counter limiter
			put(AppCountLimiterRefusedException.class, new DefaultHttpCodeName<>(4580, "CountLimiterRefused", 200));

			// authentication
			put(AppAuthenticationAccessKeyIllegalException.class,
					new DefaultHttpCodeName<>(4590, "AuthenticationAccessKeyIllegal", 200));
			put(AppAuthenticationTokenIllegalException.class,
					new DefaultHttpCodeName<>(4591, "AuthenticationTokenIllegal", 200));
			put(AppAuthenticationUserDisabledException.class,
					new DefaultHttpCodeName<>(4592, "AuthenticationUserDisabled", 200));

			// jwt token
			put(AppAuthenticationJwtTokenIllegalException.class,
					new DefaultHttpCodeName<>(4600, "AuthenticationJwtTokenIllegal", 200));
			put(AppAuthenticationJwtTokenExpiredException.class,
					new DefaultHttpCodeName<>(4601, "AuthenticationJwtTokenExpired", 200));

			// key pair
			put(AppAuthenticationKeyPairTokenIllegalException.class,
					new DefaultHttpCodeName<>(4610, "AuthenticationKeyPairTokenIllegal", 200));
			put(AppAuthenticationKeyPairSecretKeyForbiddenException.class,
					new DefaultHttpCodeName<>(4611, "AuthenticationKeyPairSecretKeyForbidden", 200));

			// authorization
			put(AppAuthorizationForbiddenException.class,
					new DefaultHttpCodeName<>(4620, "AuthorizationForbidden", 200));

			// overloaded
			put(AppServerOverloadedException.class, new DefaultHttpCodeName<>(4630, "ServerOverloaded", 200));

			// coarse grain
			put(AppParameterIllegalException.class, new DefaultHttpCodeName<>(4998, "ParameterIllegal", 200));
			put(AppException.class, new DefaultHttpCodeName<>(4999, "BadRequest", 200));

		}
	};

	private ObjectMapper objectMapper;

	public DefaultWrapResponseHandler(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	@Override
	public Map<String, Object> getNormallyResponse(RequestContext requestContext, Object object) {
		Map<String, Object> responseBody = null;
		if (object instanceof Response response) {
			Integer code = null;
			String name = null;
			String message = null;
			Boolean success = null;
			if (null == response.getCode()) {
				code = OK.getCode();
				name = OK.getName();
				message = OK.getMessage();
				success = true;
			} else {
				code = response.getCode();
				name = response.getName();
				message = response.getMessage();
				success = response.getSuccess();
			}
			responseBody = getResponseMap(code, name, message, success,
					LangUtils.defaultString(response.getRequestId(), requestContext.getRequestId()),
					LangUtils.defaultValue(response.getElapsedTime(), requestContext.getElapsedTime()),
					response.getData());
		} else {
			responseBody = getResponseMap(OK.getCode(), OK.getName(), OK.getMessage(), true,
					requestContext.getRequestId(), requestContext.getElapsedTime(), object);
		}
		return responseBody;
	}

	@Override
	public void writeExceptionResponse(HttpServletResponse response, RequestContext requestContext, Exception ex) {
		HttpCodeNameMessage<Integer, String, String> httpCodeNameMessage = null;
		if (ex instanceof AppHttpCodeNameMessageException appException) {
			httpCodeNameMessage = new DefaultHttpCodeNameMessage<Integer, String, String>(appException.getCode(),
					appException.getName(), appException.getHttpCode(), appException.getMessage());
		} else if (ex instanceof AppException) {
			httpCodeNameMessage = getHttpCodeNameMessage(CODE_NAME_EXCEPTION_MAPPING, ex);
		} else if (ex instanceof ErrorResponse errorResponse) {
			httpCodeNameMessage = getHttpCodeNameMessage(ERROR_RESPONSE_EXCEPTION_MAPPING, ex);
		} else {
			httpCodeNameMessage = getHttpCodeNameMessage(FRAMEWORK_EXCEPTION_MAPPING, ex);
		}
		if (null == httpCodeNameMessage) {
			httpCodeNameMessage = SYSTEM_ERROR;
		}
		LOGGER.error(httpCodeNameMessage.getMessage(), ex);
		Map<String, Object> responseMap = getResponseMap(httpCodeNameMessage.getCode(), httpCodeNameMessage.getName(),
				httpCodeNameMessage.getMessage(), OK.getCode().equals(httpCodeNameMessage.getCode()),
				requestContext.getRequestId(), requestContext.getElapsedTime(),
				requestContext.getHintOfExceptionCatchedData());
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(httpCodeNameMessage.getHttpCode());
			response.getOutputStream().write(objectMapper.writeValueAsBytes(responseMap));
		} catch (IOException e) {
			LOGGER.error("response error:", e);
		}

	}

	protected HttpCodeNameMessage<Integer, String, String> getHttpCodeNameMessage(
			Map<?, HttpCodeName<Integer, String>> mapping, Exception ex) {
		HttpCodeNameMessage<Integer, String, String> httpCodeNameMessage = null;
		HttpCodeName<Integer, String> httpCodeName = mapping.get(ex.getClass());
		if (null != httpCodeName) {
			httpCodeNameMessage = new DefaultHttpCodeNameMessage<Integer, String, String>(httpCodeName.getCode(),
					httpCodeName.getName(), httpCodeName.getHttpCode(), ex.getMessage());
		}
		return httpCodeNameMessage;
	}

	protected Map<String, Object> getResponseMap(Integer code, String name, String message, Boolean success,
			String requestId, Long elapsedTime, Object data) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("code", code);
		responseMap.put("name", name);
		responseMap.put("message", message);
		responseMap.put("success", success);
		responseMap.put("requestId", requestId);
		responseMap.put("elapsedTime", elapsedTime);
		responseMap.put("data", data);
		return responseMap;
	}

}

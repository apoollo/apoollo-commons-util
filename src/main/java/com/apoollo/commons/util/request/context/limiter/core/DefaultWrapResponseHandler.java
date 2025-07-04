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
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenForbiddenException;
import com.apoollo.commons.util.exception.AppAuthenticationJwtTokenIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationKeyPairSecretKeyForbiddenException;
import com.apoollo.commons.util.exception.AppAuthenticationKeyPairTokenIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationTokenIllegalException;
import com.apoollo.commons.util.exception.AppAuthenticationUserDisabledException;
import com.apoollo.commons.util.exception.AppAuthorizationForbiddenException;
import com.apoollo.commons.util.exception.AppClientRequestIdIllegalException;
import com.apoollo.commons.util.exception.AppCorsLimiterRefusedException;
import com.apoollo.commons.util.exception.AppCountLimiterRefusedException;
import com.apoollo.commons.util.exception.AppException;
import com.apoollo.commons.util.exception.AppFlowLimiterRefusedException;
import com.apoollo.commons.util.exception.AppHttpCodeNameMessageException;
import com.apoollo.commons.util.exception.AppIpLimiterExcludeListRefusedException;
import com.apoollo.commons.util.exception.AppIpLimiterIncludeListRefusedException;
import com.apoollo.commons.util.exception.AppNonceLimiterRefusedException;
import com.apoollo.commons.util.exception.AppNonceLimiterTimestampIllegalException;
import com.apoollo.commons.util.exception.AppParameterIllegalException;
import com.apoollo.commons.util.exception.AppRefererLimiterRefusedException;
import com.apoollo.commons.util.exception.AppRequestResourceDisabledException;
import com.apoollo.commons.util.exception.AppRequestResourceNotExistsException;
import com.apoollo.commons.util.exception.AppServerOverloadedException;
import com.apoollo.commons.util.exception.AppSignatureLimiterSignatureRefusedException;
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

	private static final Map<Class<? extends ErrorResponse>, HttpCodeName<Integer, String>> SPRING_BOOT_ERROR_RESPONSE_EXCEPTION_MAPPING = new HashMap<>() {
		private static final long serialVersionUID = -2202437691642912457L;

		{
			put(HttpRequestMethodNotSupportedException.class,
					new DefaultHttpCodeName<>(41000, "HttpRequestMethodNotSupported", 200));
			put(HttpMediaTypeNotSupportedException.class,
					new DefaultHttpCodeName<>(41001, "HttpMediaTypeNotSupported", 200));
			put(HttpMediaTypeNotAcceptableException.class,
					new DefaultHttpCodeName<>(41002, "HttpMediaTypeNotAcceptable", 200));
			put(MissingPathVariableException.class, new DefaultHttpCodeName<>(41003, "MissingPathVariable", 200));
			put(MissingServletRequestParameterException.class,
					new DefaultHttpCodeName<>(41004, "MissingServletRequestParameter", 200));
			put(MissingServletRequestPartException.class,
					new DefaultHttpCodeName<>(41005, "MissingServletRequestPart", 200));
			put(ServletRequestBindingException.class, new DefaultHttpCodeName<>(41006, "ServletRequestBinding", 200));
			put(MethodArgumentNotValidException.class, new DefaultHttpCodeName<>(41007, "MethodArgumentNotValid", 200));
			put(HandlerMethodValidationException.class,
					new DefaultHttpCodeName<>(41008, "HandlerMethodValidation", 200));
			put(NoHandlerFoundException.class, new DefaultHttpCodeName<>(41009, "NoHandlerFound", 200));
			put(NoResourceFoundException.class, new DefaultHttpCodeName<>(41010, "NoResourceFound", 200));
			put(AsyncRequestTimeoutException.class, new DefaultHttpCodeName<>(41011, "AsyncRequestTimeout", 200));
		}
	};

	private static final Map<Class<? extends Exception>, HttpCodeName<Integer, String>> SPRING_BOOT_FRAMEWORK_EXCEPTION_MAPPING = new HashMap<>() {
		private static final long serialVersionUID = 3853334954341313585L;

		{
			put(TypeMismatchException.class, new DefaultHttpCodeName<>(41012, "TypeMismatch", 200));
			put(HttpMessageNotReadableException.class, new DefaultHttpCodeName<>(41013, "HttpMessageNotReadable", 200));
			put(BindException.class, new DefaultHttpCodeName<>(41014, "BindError", 200));

			put(ConversionNotSupportedException.class, new DefaultHttpCodeName<>(50001, "ConversionNotSupported", 200));
			put(HttpMessageNotWritableException.class, new DefaultHttpCodeName<>(50002, "HttpMessageNotWritable", 200));
			put(MethodValidationException.class, new DefaultHttpCodeName<>(50003, "MethodValidation", 200));
			put(AsyncRequestNotUsableException.class, new DefaultHttpCodeName<>(50004, "AsyncRequestNotUsable", 200));
		}
	};

	private static final HttpCodeNameMessage<Integer, String, String> OK = new DefaultHttpCodeNameMessage<>(20000, "Ok",
			200, "success");

	private static final HttpCodeNameMessage<Integer, String, String> SYSTEM_ERROR = new DefaultHttpCodeNameMessage<>(
			50000, "SystemError", 200, "system error");

	private static final Map<Class<? extends AppException>, HttpCodeName<Integer, String>> APOOLLO_CODE_NAME_EXCEPTION_MAPPING = new HashMap<>() {
		private static final long serialVersionUID = 8730699429353651670L;

		{
			// request resource
			put(AppClientRequestIdIllegalException.class,
					new DefaultHttpCodeName<>(42000, "ClientRequestIdIllegal", 200));
			put(AppRequestResourceNotExistsException.class,
					new DefaultHttpCodeName<>(42001, "RequestResourceNotExists", 200));
			put(AppRequestResourceDisabledException.class, new DefaultHttpCodeName<>(42002, "ResourceDisabled", 200));

			// nonce limiter
			put(AppNonceLimiterRefusedException.class, new DefaultHttpCodeName<>(42010, "NonceLimiterRefused", 200));
			put(AppNonceLimiterTimestampIllegalException.class,
					new DefaultHttpCodeName<>(42011, "NonceLimiterTimestampIllegal", 200));

			// signature limiter
			put(AppSignatureLimiterSignatureRefusedException.class,
					new DefaultHttpCodeName<>(42020, "SignatureLimiterSignatureRefused", 200));

			// cors limiter
			put(AppCorsLimiterRefusedException.class, new DefaultHttpCodeName<>(42030, "CorsLimiterRefused", 200));

			// ip limiter
			put(AppIpLimiterExcludeListRefusedException.class,
					new DefaultHttpCodeName<>(42040, "IpLimiterExcludeListRefused", 200));
			put(AppIpLimiterIncludeListRefusedException.class,
					new DefaultHttpCodeName<>(42041, "IpLimiterIncludeListRefused", 200));

			// referer limiter
			put(AppRefererLimiterRefusedException.class,
					new DefaultHttpCodeName<>(42050, "RefererLimiterRefused", 200));

			// sync limiter
			put(AppSyncLimiterRefusedException.class, new DefaultHttpCodeName<>(42060, "SyncLimiterRefused", 200));

			// flow limiter
			put(AppFlowLimiterRefusedException.class, new DefaultHttpCodeName<>(42070, "FlowLimiterRefused", 200));

			// counter limiter
			put(AppCountLimiterRefusedException.class, new DefaultHttpCodeName<>(42080, "CountLimiterRefused", 200));

			// authentication
			put(AppAuthenticationAccessKeyIllegalException.class,
					new DefaultHttpCodeName<>(42090, "AuthenticationAccessKeyIllegal", 200));
			put(AppAuthenticationTokenIllegalException.class,
					new DefaultHttpCodeName<>(42091, "AuthenticationTokenIllegal", 200));
			put(AppAuthenticationUserDisabledException.class,
					new DefaultHttpCodeName<>(42092, "AuthenticationUserDisabled", 200));

			// jwt token
			put(AppAuthenticationJwtTokenIllegalException.class,
					new DefaultHttpCodeName<>(42100, "AuthenticationJwtTokenIllegal", 200));
			put(AppAuthenticationJwtTokenExpiredException.class,
					new DefaultHttpCodeName<>(42101, "AuthenticationJwtTokenExpired", 200));
			put(AppAuthenticationJwtTokenForbiddenException.class,
					new DefaultHttpCodeName<>(42102, "AuthenticationJwtTokenForbidden", 200));

			// key pair
			put(AppAuthenticationKeyPairTokenIllegalException.class,
					new DefaultHttpCodeName<>(42110, "AuthenticationKeyPairTokenIllegal", 200));
			put(AppAuthenticationKeyPairSecretKeyForbiddenException.class,
					new DefaultHttpCodeName<>(42111, "AuthenticationKeyPairSecretKeyForbidden", 200));

			// authorization
			put(AppAuthorizationForbiddenException.class,
					new DefaultHttpCodeName<>(42120, "AuthorizationForbidden", 200));

			// overloaded
			put(AppServerOverloadedException.class, new DefaultHttpCodeName<>(42130, "ServerOverloaded", 200));

			// coarse grain
			put(AppParameterIllegalException.class, new DefaultHttpCodeName<>(42998, "ParameterIllegal", 200));
			put(AppException.class, new DefaultHttpCodeName<>(42999, "BadRequest", 200));

		}
	};

	// 41000-41999 之间是SpringBoot编码
	// 42000-42999 之间是框架编码
	// 43000-49999 之间是应用编码

	private ObjectMapper objectMapper;

	public DefaultWrapResponseHandler(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	@Override
	public Object getNormallyResponse(RequestContext requestContext, Object object) {
		Object responseObject = null;
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
			responseObject = getResponse(code, name, message, success,
					LangUtils.defaultString(response.getRequestId(), requestContext.getRequestId()),
					LangUtils.defaultValue(response.getElapsedTime(), requestContext.getElapsedTime()),
					response.getData());
		} else {
			responseObject = getResponse(OK.getCode(), OK.getName(), OK.getMessage(), true,
					requestContext.getRequestId(), requestContext.getElapsedTime(), object);
		}
		return responseObject;
	}

	@Override
	public Object writeAndGetExceptionResponse(HttpServletResponse response, RequestContext requestContext, Exception ex) {
		HttpCodeNameMessage<Integer, String, String> httpCodeNameMessage = null;
		if (ex instanceof AppException) {
			if (ex instanceof AppHttpCodeNameMessageException appException) {
				httpCodeNameMessage = new DefaultHttpCodeNameMessage<Integer, String, String>(appException.getCode(),
						appException.getName(), appException.getHttpCode(), appException.getMessage());
			} else {
				httpCodeNameMessage = getHttpCodeNameMessage(getApoolloCodeNameExceptionMapping(), ex);
			}
		} else if (ex instanceof ErrorResponse) {
			httpCodeNameMessage = getHttpCodeNameMessage(getSpringBootErrorResponseExceptionMapping(), ex);
		} else {
			httpCodeNameMessage = getHttpCodeNameMessage(getSpringBootExceptionMapping(), ex);
		}
		if (null == httpCodeNameMessage) {
			httpCodeNameMessage = getSystemErrorCodeMessage();
		}
		LOGGER.error(httpCodeNameMessage.getMessage(), ex);
		Object responseObject = getResponse(httpCodeNameMessage.getCode(), httpCodeNameMessage.getName(),
				httpCodeNameMessage.getMessage(), OK.getCode().equals(httpCodeNameMessage.getCode()),
				requestContext.getRequestId(), requestContext.getElapsedTime(),
				requestContext.getHintOfExceptionCatchedData());

		writeResponse(response, httpCodeNameMessage.getHttpCode(), responseObject);
		return responseObject;
	}

	protected HttpCodeNameMessage<Integer, String, String> getSystemErrorCodeMessage() {
		return SYSTEM_ERROR;
	}

	protected Map<Class<? extends AppException>, HttpCodeName<Integer, String>> getApoolloCodeNameExceptionMapping() {
		return APOOLLO_CODE_NAME_EXCEPTION_MAPPING;
	}

	protected Map<Class<? extends ErrorResponse>, HttpCodeName<Integer, String>> getSpringBootErrorResponseExceptionMapping() {
		return SPRING_BOOT_ERROR_RESPONSE_EXCEPTION_MAPPING;
	}

	protected Map<Class<? extends Exception>, HttpCodeName<Integer, String>> getSpringBootExceptionMapping() {
		return SPRING_BOOT_FRAMEWORK_EXCEPTION_MAPPING;
	}

	protected void writeResponse(HttpServletResponse response, int httpCode, Object responseMap) {
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(httpCode);
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

	protected Object getResponse(Integer code, String name, String message, Boolean success,
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

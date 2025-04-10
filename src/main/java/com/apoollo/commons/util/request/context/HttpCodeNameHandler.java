/**
 * 
 */
package com.apoollo.commons.util.request.context;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.RegexUtils;

/**
 * @author liuyulong
 */
public interface HttpCodeNameHandler {

	/**
	 * 默认成功Code
	 */
	public HttpCodeName<String, String> getOk();

	public HttpCodeName<String, String> getNoHandlerFound();

	public HttpCodeName<String, String> getForbbiden();

	public HttpCodeName<String, String> getIllegalArgument();

	public HttpCodeName<String, String> getServerError();

	public HttpCodeName<String, String> getServerOverloaded();

	public HttpCodeName<String, String> getTimeoutLimit();

	public HttpCodeName<String, String> getExceedingDailyMaximumUseTimesLimit();

	public HttpCodeName<String, String> getAppMaxUploadSizeExceededException();

	public default HttpCodeName<String, String> getAccessKeyEmpty() {
		return getForbbiden();
	}

	public default HttpCodeName<String, String> getTokenEmpty() {
		return getForbbiden();
	}

	public default HttpCodeName<String, String> getIpLimit() {
		return getForbbiden();
	}

	public default HttpCodeName<String, String> getTimeoutIllegalArgument() {
		return getIllegalArgument();
	}

	public default <T> Response<T> getResponse(Boolean success, String code, String message, T data) {
		Response<T> response = newInstance();
		response.setCode(code);
		response.setElapsedTime(null);
		response.setData(data);
		response.setMessage(message);
		response.setRequestId(null);
		response.setSuccess(success);
		return response;
	}

	public default <T> Response<T> getResponse(CodeName<String, String> codeMessage, Object[] messageCompileArgs,
			String appendMessage, T data) {
		return getResponse(processIsExecuteSuccess(codeMessage.getCode()), codeMessage.getCode(),
				getMessage(codeMessage.getName(), messageCompileArgs, appendMessage), data);
	}

	public default <T> Response<T> success(T data) {
		return getResponse(getOk(), null, null, data);
	}

	public default String getMessage(String messageExpression, Object[] messageCompileArgs, String appendMessage) {
		return getDefaultMessage(messageExpression, messageCompileArgs, appendMessage);
	}

	public static String appendMessage(String message, String appendMessage) {
		if (StringUtils.isNotBlank(appendMessage)) {
			message = StringUtils.joinWith(":", message, appendMessage);
		}
		return message;
	}

	public static String getDefaultMessage(String messageExpression, Object[] messageCompileArgs,
			String appendMessage) {
		String message = null;
		if (messageExpression.contains("${") && messageExpression.contains("}")) {
			message = appendMessage(RegexUtils.compile(messageExpression, messageCompileArgs), appendMessage);
		} else {
			if (null != messageCompileArgs && messageCompileArgs.length > 0) {
				message = LangUtils.join(LangUtils.getStream(messageCompileArgs), ",", messageExpression + ":", "");
			} else {
				message = appendMessage(messageExpression, appendMessage);
			}
		}
		return message;
	}

	/**
	 * 整个请求是否执行成功
	 */
	public default boolean processIsExecuteSuccess(String code) {
		return getOk().getCode().equals(code);
	}

	public <T> Response<T> newInstance();

	public default void restResponse(Object requestBody, Response<?> resopnse) {

	}

}

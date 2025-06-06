/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import com.apoollo.commons.util.request.context.HttpCodeName;
import com.apoollo.commons.util.request.context.WrapResponseHandler;
import com.apoollo.commons.util.request.context.Response;

/**
 * @author liuyulong
 */
public class DefaultWrapResponseHandler implements WrapResponseHandler {

	@Override
	public HttpCodeName<String, String> getOk() {
		return DefaultHttpStatus.OK;
	}

	@Override
	public HttpCodeName<String, String> getNoHandlerFound() {
		return DefaultHttpStatus.NO_HANDLER_FOUND;
	}

	@Override
	public HttpCodeName<String, String> getForbbiden() {
		return DefaultHttpStatus.FORBIDDEN;
	}

	@Override
	public HttpCodeName<String, String> getIllegalArgument() {
		return DefaultHttpStatus.ILLEGAL_ARGUMENT;
	}

	@Override
	public HttpCodeName<String, String> getServerError() {
		return DefaultHttpStatus.SERVER_ERROR;
	}

	@Override
	public HttpCodeName<String, String> getServerOverloaded() {
		return DefaultHttpStatus.SERVER_OVERLOADED;
	}

	@Override
	public HttpCodeName<String, String> getTimeoutLimit() {
		return DefaultHttpStatus.REQUEST_TIMOUT_LIMIT;
	}

	@Override
	public HttpCodeName<String, String> getExceedingDailyMaximumUseTimesLimit() {
		return DefaultHttpStatus.EXCEEDING_DAILY_MAXIMUM_USE_TIMES_LIMIT;
	}

	@Override
	public HttpCodeName<String, String> getAppMaxUploadSizeExceededException() {
		return DefaultHttpStatus.MaxUploadSizeExceceded;
	}

	@Override
	public <T> Response<T> newInstance() {
		return new AppResponse<>();
	}

}

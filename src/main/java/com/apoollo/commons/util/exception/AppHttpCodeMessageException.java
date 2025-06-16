/**
 * 
 */
package com.apoollo.commons.util.exception;

import com.apoollo.commons.util.request.context.HttpCodeName;

/**
 * @author liuyulong
 */
public class AppHttpCodeMessageException extends AppException {

	private static final long serialVersionUID = -533330540016251432L;

	private HttpCodeName<String, String> httpCodeName;

	private Object[] messageCompileArgs;

	public AppHttpCodeMessageException(HttpCodeName<String, String> codeName) {
		this(codeName, null);
	}

	public AppHttpCodeMessageException(HttpCodeName<String, String> httpCodeName, Object[] messageCompileArgs) {
		super("AppHttpCodeMessage Error:");
		this.httpCodeName = httpCodeName;
		this.messageCompileArgs = messageCompileArgs;
	}

	public HttpCodeName<String, String> getHttpCodeName() {
		return httpCodeName;
	}

	public Object[] getMessageCompileArgs() {
		return messageCompileArgs;
	}

}

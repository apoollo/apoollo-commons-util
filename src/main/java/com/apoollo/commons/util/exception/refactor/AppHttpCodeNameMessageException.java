/**
 * 
 */
package com.apoollo.commons.util.exception.refactor;

import com.apoollo.commons.util.request.context.HttpCodeNameMessage;
import com.apoollo.commons.util.request.context.HttpCodeNameMessageFormat;

/**
 * @author liuyulong
 */
public class AppHttpCodeNameMessageException extends AppException {

	private static final long serialVersionUID = -533330540016251432L;

	private Integer code;
	private String name;
	private Integer httpCode;

	public AppHttpCodeNameMessageException(HttpCodeNameMessage<Integer, String, String> codeName) {
		super(codeName.getMessage());
		this.code = codeName.getCode();
		this.name = codeName.getName();
		this.httpCode = codeName.getHttpCode();
	}

	public AppHttpCodeNameMessageException(HttpCodeNameMessageFormat<Integer, String> codeName,
			Object[] messageCompileArgs) {
		super(codeName.getMessage().format(messageCompileArgs));
		this.code = codeName.getCode();
		this.name = codeName.getName();
		this.httpCode = codeName.getHttpCode();
	}

	public AppHttpCodeNameMessageException(Integer code, String name, Integer httpCode, String message) {
		super(message);
		this.code = code;
		this.name = name;
		this.httpCode = httpCode;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Integer getHttpCode() {
		return httpCode;
	}

}

/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@NoArgsConstructor
public class Request<T> extends DefaultRequestId {

	private String accessKey;
	private T data;
	public Request(String requestId, String accessKey, T data) {
		super(requestId);
		this.accessKey = accessKey;
		this.data = data;
	}
	
	
	
	
}

/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import com.apoollo.commons.util.request.context.RequestId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@NoArgsConstructor
public class DefaultRequestId implements RequestId {

	private String requestId;

	/**
	 * @param requestId
	 */
	public DefaultRequestId(String requestId) {
		super();
		this.requestId = requestId;
	}
	
	

}

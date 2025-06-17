/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import com.apoollo.commons.util.request.context.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse<T> extends DefaultRequestId implements Response<T> {

	private Boolean success;
	private Integer code;
	private String name;
	private String message;
	private Long elapsedTime;
	private T data;

}

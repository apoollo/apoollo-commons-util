/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 */
public interface Response<T> extends RequestId {
	
	public Boolean getSuccess();
	public Long getElapsedTime();
	public Integer getCode();
	public String getName();
	public String getMessage();
	public T getData();
	
	public void setSuccess(Boolean success);
	public void setElapsedTime(Long elapsedTime);
	public void setCode(Integer code);
	public void setName(String name);
	public void setMessage(String message);
	public void setData(T data);

}

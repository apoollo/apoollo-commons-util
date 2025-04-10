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
	public String getCode();
	public String getMessage();
	public T getData();
	
	public void setSuccess(Boolean success);
	public void setElapsedTime(Long elapsedTime);
	public void setCode(String code);
	public void setMessage(String message);
	public void setData(T data);

}

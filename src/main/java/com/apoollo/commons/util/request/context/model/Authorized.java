package com.apoollo.commons.util.request.context.model;

/**
 * @author liuyulong
 */
public class Authorized<T> {
	private boolean success;
	private T authorizedValue;
	
	public Authorized() {
	}

	/**
	 * @param success
	 * @param authorizedValue
	 */
	public Authorized(boolean success, T authorizedValue) {
		super();
		this.success = success;
		this.authorizedValue = authorizedValue;
	}

	/**
	 * @return the success
	 */
	public boolean getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the authorizedValue
	 */
	public T getAuthorizedValue() {
		return authorizedValue;
	}

	/**
	 * @param authorizedValue the authorizedValue to set
	 */
	public void setAuthorizedValue(T authorizedValue) {
		this.authorizedValue = authorizedValue;
	}

}
package com.apoollo.commons.util.request.context.def;

import com.apoollo.commons.util.request.context.CodeName;

/**
 * @author liuyulong
 */
public class DefaultCodeName<C, N> implements CodeName<C, N> {

	private C code;
	private N name;

	public DefaultCodeName() {

	}

	/**
	 * @param code
	 * @param name
	 */
	public DefaultCodeName(C code, N name) {
		super();
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public C getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(C code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public N getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(N name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		return this.overrideEquals(obj);
	}

	@Override
	public int hashCode() {
		return this.overrideHashCode();
	}

}
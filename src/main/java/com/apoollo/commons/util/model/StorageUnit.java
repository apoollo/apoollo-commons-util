/**
 * 
 */
package com.apoollo.commons.util.model;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */

public enum StorageUnit {

	BYTE("Byte", "字节", 1L), //
	KILOBYTE("KB", "千字节", 1L * 1024), //
	MBYTE("MB", "兆字节", 1024L * 1024), //
	GIGABYTE("GB", "吉字节", 1024L * 1024 * 1024) //
	;

	private String code;
	private String name;
	private Long byteCount;

	/**
	 * @param code
	 * @param name
	 * @param byteUnit
	 */
	private StorageUnit(String code, String name, Long byteCount) {
		this.code = code;
		this.name = name;
		this.byteCount = byteCount;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the byteCount
	 */
	public Long getByteCount() {
		return byteCount;
	}

	public static boolean is(StorageUnit instance, String code) {
		return instance.getCode().equals(code);
	}

	public static boolean contains(String code) {
		return LangUtils.getStream(StorageUnit.values()).filter(instance -> instance.getCode().equals(code)).findFirst()
				.isPresent();
	}

}

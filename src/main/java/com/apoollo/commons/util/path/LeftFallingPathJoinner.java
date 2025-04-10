/**
 * 
 */
package com.apoollo.commons.util.path;

/**
 * liuyulong
 */
public class LeftFallingPathJoinner implements PathJoiner {

	private static final LeftFallingPathJoinner FALLING_PATH_JOINNER = new LeftFallingPathJoinner();

	public static PathJoiner getPathJoiner() {
		return FALLING_PATH_JOINNER;
	}

	@Override
	public String getRootStart() {
		return "/";
	}

	@Override
	public String getPathSplitor() {
		return "/";
	}

}

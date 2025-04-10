/**
 * 
 */
package com.apoollo.commons.util.path;

/**
 * liuyulong
 */
public class RightFallingPathJoinner implements PathJoiner {

	private static final RightFallingPathJoinner FALLING_PATH_JOINNER = new RightFallingPathJoinner();

	public static PathJoiner getPathJoiner() {
		return FALLING_PATH_JOINNER;
	}

	@Override
	public String getPathSplitor() {
		return "\\";
	}

}

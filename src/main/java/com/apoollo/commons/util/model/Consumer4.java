/**
 * 
 */
package com.apoollo.commons.util.model;

/**
 * @author liuyulong
 */
public interface Consumer4<ONE, TWO, THREE, FOUR> {

	void accept(ONE one, TWO two, THREE three, FOUR four);
}

/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.function.Function;

/**
 * @author liuyulong
 */
public interface Invocation<I, O> {

	public O invoke(Function<I, O> function, I input);
}

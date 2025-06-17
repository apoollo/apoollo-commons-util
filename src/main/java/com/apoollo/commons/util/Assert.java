/**
 * 
 */
package com.apoollo.commons.util;

import java.util.function.Supplier;

import org.apache.commons.lang3.BooleanUtils;

import com.apoollo.commons.util.exception.AppException;
import com.apoollo.commons.util.exception.AppHttpCodeNameMessageException;
import com.apoollo.commons.util.exception.AppParameterIllegalException;
import com.apoollo.commons.util.request.context.HttpCodeNameMessage;
import com.apoollo.commons.util.request.context.HttpCodeNameMessageFormat;

/**
 * @author liuyulong
 * @since 2023年8月9日
 */
public class Assert {

	public static <T extends AppException> void isTrue(Supplier<Boolean> expression, Supplier<T> exceptionGetter) {
		if (BooleanUtils.isNotTrue(expression.get())) {
			throw exceptionGetter.get();
		}
	}

	public static <T extends AppException> void isTrue(boolean expression, Supplier<T> exceptionGetter) {
		if (!expression) {
			throw exceptionGetter.get();
		}
	}

	public static void isTrue(boolean expression, HttpCodeNameMessageFormat<Integer, String> codeName,
			Object... messageCompileArgs) {
		isTrue(expression, () -> {
			return new AppHttpCodeNameMessageException(codeName, messageCompileArgs);
		});
	}

	public static void isTrue(boolean expression, HttpCodeNameMessage<Integer, String, String> codeName) {
		isTrue(expression, () -> {
			return new AppHttpCodeNameMessageException(codeName);
		});
	}

	public static <T extends AppException> void isTrue(boolean expression, String message) {
		isTrue(expression, () -> new AppParameterIllegalException(message));
	}

}

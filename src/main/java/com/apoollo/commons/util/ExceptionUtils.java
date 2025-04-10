package com.apoollo.commons.util;

import com.apoollo.commons.util.exception.AppException;

/**
 * @author liuyulong
 */
public class ExceptionUtils {

	@FunctionalInterface
	public interface ThrowingSupplier<T, E extends Throwable> {
		T get() throws E;
	}

	public static <T, E extends Throwable> T unchecked(ThrowingSupplier<T, E> supplier) {
		try {
			return supplier.get();
		} catch (Throwable exception) {
			throw new RuntimeException(exception);
		}
	}

	public static <T, E extends Throwable> T uncheckedToAppException(ThrowingSupplier<T, E> supplier,
			String throwMessage) {
		try {
			return supplier.get();
		} catch (Throwable exception) {
			throw new AppException(throwMessage, exception);
		}
	}
}
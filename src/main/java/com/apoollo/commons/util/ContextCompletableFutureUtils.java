/**
 * 
 */
package com.apoollo.commons.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import com.apoollo.commons.util.exception.AppException;
import com.apoollo.commons.util.request.context.RequestContext;

/**
 * @author liuyulong
 */
public class ContextCompletableFutureUtils {

	public static <T> T completableFutureGet(CompletableFuture<T> completableFuture) {
		try {
			return completableFuture.get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			Throwable throwable = e.getCause();
			if (throwable instanceof AppException) {
				throw (AppException) throwable;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	public static <T> CompletableFuture<T> completableSupplyAsync(Supplier<T> supplier) {
		RequestContext requestContext = RequestContext.getRequired();
		CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(() -> {
			T result = null;
			try {
				RequestContext.reset(requestContext);
				result = supplier.get();
			} finally {
				RequestContext.release();
			}
			return result;
		});
		return completableFuture;
	}

}

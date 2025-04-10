/**
 * 
 */
package com.apoollo.commons.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.function.ThrowingSupplier;

/**
 * 用于重试函数
 * 
 * <pre>
 * 重试因素包含： <br/>
 * 1、参数：需要重试的次数 <br/>
 * 2、被重试函数返回值的自定义断言 <br/>
 * 3、被重试函数触发异常的自定义断言
 * 
 * <pre>
 * 
 * @author liuyulong
 */
public class RetryUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryUtils.class);

    /**
     * 重试普通签名的函数，此函数叫做被重试函数{@code retryFunction}，比如：{@code public void call() }
     * 
     * @param <T>                     被重试函数{@code retryFunction}的返回值类型
     * @param maxInvokeTimes          最大调用次数，此数值控制最多调用被重试函数{@code retryFunction}的次数,数值的有效取值范围:
     *                                >0 , 否则将不被重试
     * @param retryFunction           被重试函数
     * @param responseRetryPredicate  业务重试断言，是否重试会根据被函数的返回值进行自定义判断，是否重试除此断言外，还会判断重试次数
     * @param exceptionRetryPredicate 异常重试断言，是否重试会根据被函数的异常类型进行自定义判断，是否重试除此断言外，还会判断重试次数
     * @return 被重试函数{@code retryFunction}的返回值
     * @throws Throwable 被重试函数{@code retryFunction}抛出的异常
     */
    public static <T> T retry(int maxInvokeTimes, Supplier<T> retryFunction, Predicate<T> responseRetryPredicate,
            Predicate<Throwable> exceptionRetryPredicate) throws Throwable {
        return retryThrowingSupplier(maxInvokeTimes, getThrowingSupplier(retryFunction), responseRetryPredicate,
                exceptionRetryPredicate);
    }

    /**
     * 
     * 重试带有抛出异常签名的函数{@code retryFunction}，此函数叫做被重试函数，比如：{@code public void call() throws Exception }
     * 
     * @param <T>                     被重试函数{@code retryFunction}的返回值类型
     * @param maxInvokeTimes          最大调用次数，此数值控制最多调用被重试函数{@code retryFunction}的次数,
     *                                数值的有效取值范围: >0 , 否则将不被重试
     * @param retryFunction           被重试函数
     * @param responseRetryPredicate  业务重试断言，是否重试会根据被函数的返回值进行自定义判断，是否重试除此断言外，还会判断重试次数
     * @param exceptionRetryPredicate 异常重试断言，是否重试会根据被函数的异常类型进行自定义判断，是否重试除此断言外，还会判断重试次数
     * @return 被重试函数{@code retryFunction}的返回值
     * @throws Throwable 被重试函数{@code retryFunction}抛出的异常
     */
    public static <T> T retryThrowingSupplier(int maxInvokeTimes, ThrowingSupplier<T> retryFunction,
            Predicate<T> responseRetryPredicate, Predicate<Throwable> exceptionRetryPredicate) throws Throwable {
        // 最大次数减一，是因为默认需要调用一次
        int realInvokeTimes = maxInvokeTimes - 1;

        // 检查次数的有效范围
        int retryTimesLimit = LangUtils.ifElseSupplier(realInvokeTimes >= 0, () -> realInvokeTimes, () -> 1);

        // 执行重试
        return retry(retryTimesLimit, new AtomicInteger(1), retryFunction, responseRetryPredicate,
                exceptionRetryPredicate);
    }

    private static <T> T retry(int retryTimesLimit, AtomicInteger retryTimes, ThrowingSupplier<T> retryFunction,
            Predicate<T> responseRetryPredicate, Predicate<Throwable> exceptionRetryPredicate) throws Throwable {
        T response = null;

        boolean retry = false;
        Supplier<Boolean> beforeRetryPolicy = () -> timesRetryPolicy(retryTimesLimit, retryTimes.get());
        Throwable exception = null;
        try {
            // 执行被重试函数
            response = retryFunction.getWithException();
            // 非异常情况下检查是否需要重试
            retry = beforeRetryPolicy.get() && null != response && responseRetryPredicate.test(response);
        } catch (Throwable e) {
            // 异常情况下检查是否需要重试
            exception = e;
            retry = beforeRetryPolicy.get() && exceptionRetryPredicate.test(exception);
        }
        if (retry) {
            if (null != exception) {
                LOGGER.error("timeout exception :", exception);
            }
            LOGGER.info("retryTimesLimit: {}, retryTimes: {}", retryTimesLimit, retryTimes);
            // 如果判断需要重试，则重试次数加1
            retryTimes.incrementAndGet();
            // 执行下一次重试
            response = retry(retryTimesLimit, retryTimes, retryFunction, responseRetryPredicate,
                    exceptionRetryPredicate);

        } else if (null != exception) {
            // 如果不需要重试，则表示进入最后了最后一次调用，有异常，则直接抛出
            throw exception;
        }
        return response;

    }

    private static <T> ThrowingSupplier<T> getThrowingSupplier(Supplier<T> supplier) {
        return new ThrowingSupplier<T>() {

            @Override
            public T getWithException() throws Exception {
                return supplier.get();
            }
        };
    }

    private static boolean timesRetryPolicy(int retryTimesLimit, int retryTimes) {
        return retryTimes <= retryTimesLimit;
    }

    /*
     * public static boolean exceptionShouldRetry(Throwable e) { return
     * RetryUtils.exceptionShouldRetry(e,
     * LangUtils.toList(SocketTimeoutException.class,
     * HttpConnectTimeoutException.class,
     * org.apache.http.conn.ConnectTimeoutException.class,
     * io.netty.channel.ConnectTimeoutException.class,
     * io.netty.handler.timeout.TimeoutException.class,
     * java.util.concurrent.TimeoutException.class, EOFException.class)); }
     */
    public static boolean exceptionShouldRetry(Throwable e, List<Class<? extends Throwable>> retryThrowableClasses) {
        if (null == e) {
            return false;
        } else if (retryThrowableClasses.contains(e.getClass())) {

            return true;
        } else {
            return exceptionShouldRetry(e.getCause(), retryThrowableClasses);
        }
    }

}

/**
 * 
 */
package com.apoollo.commons.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.apoollo.commons.util.model.Processor;
import com.apoollo.commons.util.request.context.Invocation;
import com.apoollo.commons.util.request.context.core.TimeoutContextInvocation;

/**
 * @author liuyulong
 */
public class ContextInvocationUtils {

    public static <I, O> O invoke(Function<I, O> function, I input, String loggingName, String[] inputMaskPropertyNames,
            String[] outputMaskPropertyNames) {
        Invocation<I, O> contextInvocation = new TimeoutContextInvocation<>(loggingName, inputMaskPropertyNames,
                outputMaskPropertyNames);
        return contextInvocation.invoke(function, input);
    }

    public static <I, O> O invoke(Function<I, O> function, I input, String loggingName,
            String[] inputMaskPropertyNames) {
        return invoke(function, input, loggingName, inputMaskPropertyNames, null);
    }

    public static <I, O> O invoke(Function<I, O> function, I input, String loggingName) {
        return invoke(function, input, loggingName, null, null);
    }

    public static <I> void invoke(Consumer<I> consumer, I input, String loggingName, String[] inputMaskPropertyNames) {
        invoke((param) -> {
            consumer.accept(param);
            return null;
        }, input, loggingName, inputMaskPropertyNames, null);
    }

    public static <O> O invoke(Supplier<O> supplier, String loggingName, String[] outputMaskPropertyNames) {
        return invoke((param) -> supplier.get(), null, loggingName, null, outputMaskPropertyNames);
    }

    public static void invoke(Processor processor, String loggingName) {
        invoke((param) -> {
            processor.process();
            return null;
        }, null, loggingName, null, null);
    }
}

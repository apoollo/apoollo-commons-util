/**
 * 
 */
package com.apoollo.commons.util;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.exception.AppCodeNameException;
import com.apoollo.commons.util.exception.AppException;
import com.apoollo.commons.util.exception.AppHttpCodeMessageException;
import com.apoollo.commons.util.exception.AppIllegalArgumentException;
import com.apoollo.commons.util.request.context.CodeName;
import com.apoollo.commons.util.request.context.HttpCodeName;

/**
 * @author liuyulong
 * @since 2023年8月9日
 */
public class Assert {

    public static <T extends AppException> void isTrue(boolean expression, Supplier<T> exceptionGetter) {
        if (!expression) {
            throw exceptionGetter.get();
        }
    }

    public static void isTrue(boolean expression, CodeName<String, String> codeName, Supplier<String> message) {
        isTrue(expression, () -> {
            if (null != message) {
                return new AppCodeNameException(codeName, message.get());
            } else {
                return new AppCodeNameException(codeName, null);
            }
        });
    }

    public static void isTrue(boolean expression, HttpCodeName<String, String> httpCodeName, Object... values) {
        isTrue(expression, () -> {
            return new AppHttpCodeMessageException(httpCodeName, values);
        });
    }

    public static void notNull(Object object, HttpCodeName<String, String> codeName, Object... values) {
        isTrue(null != object, codeName, values);
    }

    public static void isTrue(boolean expression, String message) {
        isTrue(expression, () -> {
            return new AppIllegalArgumentException(message);
        });
    }

    public static void notNull(Object object, String message) {
        isTrue(null != object, message);
    }

    public static void isNull(Object object, String message) {
        isTrue(null == object, message);
    }

    public static void isNotBlank(String input, String message) {
        isTrue(StringUtils.isNotBlank(input), message);
    }

    public static void isValidId(Number number, String message) {
        isTrue(null != number && number.doubleValue() > 0, message);
    }

    public static void isTrueOfApp(boolean expression, Supplier<String> message) {
        isTrue(expression, () -> {
            if (null == message) {
                return new AppException();
            } else {
                return new AppException(message.get());
            }
        });
    }

    public static void isTrueOfApp(boolean expression, String message) {
        isTrueOfApp(expression, () -> message);
    }

    public static void notNullOfApp(Object object, Supplier<String> message) {
        isTrueOfApp(null != object, message);
    }

    public static void isNotBlankOfApp(String input, Supplier<String> message) {
        isTrueOfApp(StringUtils.isNotBlank(input), message);
    }

}

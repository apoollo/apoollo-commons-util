/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import java.util.function.Consumer;

/**
 * @author liuyulong
 */
public interface AuthorizationRenewal<T, R> {

    public R renewal(User user, T token, Consumer<R> consumer);

    public default R renewal(User user, T token) {
        return renewal(user, token, null);
    }
}

/**
 * 
 */
package com.apoollo.commons.util.redis.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 */
public interface RedisNameSpaceKey {

    public String getNamespace();

    public default String getKey(String serviceNamespace, List<String> services) {
        return getKey(serviceNamespace, services.stream());
    }

    public default String getKey(String serviceNamespace, Stream<String> services) {
        return getKey(Stream.concat(LangUtils.getStream(serviceNamespace), services));
    }

    public default String getKey(String... services) {
        return getKey(LangUtils.getStream(services));
    }

    public default String getKey(Stream<String> services) {
        return new StringBuilder(getNamespace())//
                .append(getSplitor())//
                .append(services.collect(Collectors.joining(getSplitor())))//
                .toString();
    }

    public default String getSplitor() {
        return ":";
    }

}

/**
 * 
 */
package com.apoollo.commons.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.apoollo.commons.util.model.DisableSSLValidation;
import com.apoollo.commons.util.model.HttpClientResponse;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author liuyulong
 */
public class OkHttpClientUtils {

    public static String getString(OkHttpClient okHttpClient, String url, Headers headers) {
        return get(okHttpClient, url, headers,
                (response, responseBody) -> ExceptionUtils.unchecked(responseBody::string));
    }

    public static String postAndGetString(OkHttpClient okHttpClient, String url, Headers headers,
            RequestBody requestBody) {
        return post(okHttpClient, url, headers, requestBody,
                (response, responseBody) -> ExceptionUtils.unchecked(responseBody::string));
    }

    public static String putAndGetString(OkHttpClient okHttpClient, String url, Headers headers,
            RequestBody requestBody) {
        return put(okHttpClient, url, headers, requestBody,
                (response, responseBody) -> ExceptionUtils.unchecked(responseBody::string));
    }

    public static <T> T get(OkHttpClient okHttpClient, String url, Headers headers,
            BiFunction<Response, ResponseBody, T> responseFunction) {
        return execute(okHttpClient, (builder, request) -> builder.get(), url, headers, null, responseFunction);
    }

    @SuppressWarnings("unchecked")
    public static <T> HttpClientResponse<T> restfulPost(OkHttpClient okHttpClient, String url, Headers headers,
            Object object, Class<T> responseClass) {

        return restfulPost(okHttpClient, url, headers, object, (value) -> {
            T entity = null;
            if (StringUtils.isNotBlank(value)) {
                if (String.class == responseClass) {

                    entity = (T) value;
                } else {
                    entity = JSON.parseObject(value, responseClass);
                }
            }
            return entity;
        });
    }

    public static <T> HttpClientResponse<T> restfulPost(OkHttpClient okHttpClient, String url, Headers headers,
            Object object, Function<String, T> fn) {
        byte[] requestBytesBody = null;
        if (object instanceof String) {
            requestBytesBody = ((String) object).getBytes(StandardCharsets.UTF_8);

        } else if (null != object) {
            requestBytesBody = JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
        }
        RequestBody requestBody = null;
        if (null != requestBytesBody) {
            requestBody = RequestBody.create(requestBytesBody);
        } else {
            requestBody = RequestBody.create(new byte[0]);
        }
        if (null == headers) {
            headers = Headers.of("Content-Type", "application/json");
        }
        return post(okHttpClient, url, headers, requestBody, (response, responseBody) -> {
            String value = ExceptionUtils.unchecked(responseBody::string);

            return new HttpClientResponse<T>(response.code(), fn.apply(value));
        });
    }

    public static <T> T post(OkHttpClient okHttpClient, String url, Headers headers, RequestBody requestBody,
            BiFunction<Response, ResponseBody, T> responseFunction) {
        return execute(okHttpClient, (builder, request) -> builder.post(request), url, headers, requestBody,
                responseFunction);
    }

    public static <T> T put(OkHttpClient okHttpClient, String url, Headers headers, RequestBody requestBody,
            BiFunction<Response, ResponseBody, T> responseFunction) {
        return execute(okHttpClient, (builder, request) -> builder.put(request), url, headers, requestBody,
                responseFunction);
    }

    public static <T> T execute(OkHttpClient okHttpClient,
            BiFunction<Request.Builder, RequestBody, Request.Builder> methodFunction, String url, Headers headers,
            RequestBody requestBody, BiFunction<Response, ResponseBody, T> responseFunction) {
        return execute(okHttpClient, buildRequest(methodFunction, url, headers, requestBody), responseFunction);
    }

    public static Request buildRequest(BiFunction<Request.Builder, RequestBody, Request.Builder> requestMethodFunction,
            String url, Headers headers, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder().url(url);
        if (null != headers) {
            builder = builder.headers(headers);
        }
        Request request = requestMethodFunction.apply(builder, requestBody).build();
        return request;
    }

    public static <T> T execute(OkHttpClient okHttpClient, Request request,
            BiFunction<Response, ResponseBody, T> responseFunction) {
        if (null == okHttpClient) {
            okHttpClient = getDefaultOkHttpClient();
        }
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute(); ResponseBody responseBody = response.body()) {
            T result = null;
            if (null != responseFunction) {
                result = responseFunction.apply(response, responseBody);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getDefaultOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient//
                .Builder()//
                .callTimeout(Duration.ofMillis(30000))//
                .connectTimeout(Duration.ofMillis(10000))//
                .readTimeout(Duration.ofMillis(10000))//
                .writeTimeout(Duration.ofMillis(10000))//
                .build();
        return okHttpClient;
    }

    public static okhttp3.OkHttpClient.Builder getDisableSslValidationBuilder() {
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        DisableSSLValidation disableSSLValidation = new DisableSSLValidation();
        disableSSLValidation.apply((disabledSSLSocketFactory, disabledTrustManager, hostnameVerifier) -> {
            builder.sslSocketFactory(disabledSSLSocketFactory, disabledTrustManager);
            builder.hostnameVerifier(hostnameVerifier);
        });
        return builder;
    }
}

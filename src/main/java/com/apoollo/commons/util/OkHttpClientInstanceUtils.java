/**
 * 
 */
package com.apoollo.commons.util;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.apoollo.commons.util.model.HttpClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author liuyulong
 */
public class OkHttpClientInstanceUtils {

    public static OkHttpClientInstance getDisableSslValidationOkHttpClient(
            RequestConnectionParam requestConnectionParam) {
        OkHttpClient httpClient = OkHttpClientUtils.getDisableSslValidationBuilder()//
                .callTimeout(LangUtils.defaultValue(requestConnectionParam.getCallTimeout(), Duration.ofSeconds(2)))//
                .connectTimeout(
                        LangUtils.defaultValue(requestConnectionParam.getConnectTimeout(), Duration.ofSeconds(2)))//
                .readTimeout(LangUtils.defaultValue(requestConnectionParam.getReadTimeout(), Duration.ofSeconds(2)))//
                .writeTimeout(LangUtils.defaultValue(requestConnectionParam.getWriteTimeout(), Duration.ofSeconds(2)))//
                .build();
        return new OkHttpClientInstance(requestConnectionParam.getUrl(), httpClient);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class OkHttpClientInstance {
        private String url;
        private OkHttpClient okHttpClient;

        public OkHttpClientInstance refactor(BiFunction<OkHttpClient, String, OkHttpClientInstance> fn) {
            return fn.apply(okHttpClient, url);
        }

        public OkHttpClientInstance refactorUrl(Function<String, String> fn) {
            return new OkHttpClientInstance(fn.apply(url), okHttpClient);
        }

        public OkHttpClientInstance refactorOkHttpClient(Function<OkHttpClient, OkHttpClient> fn) {
            return new OkHttpClientInstance(url, fn.apply(okHttpClient));
        }

        public String getString(Headers headers) {
            return OkHttpClientUtils.getString(okHttpClient, url, headers);
        }

        public String postAndGetString(Headers headers, RequestBody requestBody) {
            return OkHttpClientUtils.putAndGetString(okHttpClient, url, headers, requestBody);
        }

        public String putAndGetString(Headers headers, RequestBody requestBody) {
            return OkHttpClientUtils.putAndGetString(okHttpClient, url, headers, requestBody);
        }

        public <T> T get(Headers headers, BiFunction<Response, ResponseBody, T> responseFunction) {
            return OkHttpClientUtils.get(okHttpClient, url, headers, responseFunction);
        }

        public <T> HttpClientResponse<T> restfulPost(Headers headers, Object object, Class<T> responseClass) {
            return OkHttpClientUtils.restfulPost(okHttpClient, url, headers, object, responseClass);
        }

        public <T> HttpClientResponse<T> restfulPost(Headers headers, Object object, Function<String, T> fn) {
            return OkHttpClientUtils.restfulPost(okHttpClient, url, headers, object, fn);
        }

        public <T> T post(Headers headers, RequestBody requestBody,
                BiFunction<Response, ResponseBody, T> responseFunction) {
            return OkHttpClientUtils.post(okHttpClient, url, headers, requestBody, responseFunction);
        }

        public <T> T put(Headers headers, RequestBody requestBody,
                BiFunction<Response, ResponseBody, T> responseFunction) {
            return OkHttpClientUtils.put(okHttpClient, url, headers, requestBody, responseFunction);
        }

        public <T> T execute(BiFunction<Request.Builder, RequestBody, Request.Builder> methodFunction, Headers headers,
                RequestBody requestBody, BiFunction<Response, ResponseBody, T> responseFunction) {
            return OkHttpClientUtils.execute(okHttpClient, methodFunction, url, headers, requestBody, responseFunction);
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestConnectionParam {

        private String url;
        private Duration callTimeout;
        private Duration connectTimeout;
        private Duration readTimeout;
        private Duration writeTimeout;

        public RequestConnectionParam(String url) {
            this.url = url;
        }
    }

}

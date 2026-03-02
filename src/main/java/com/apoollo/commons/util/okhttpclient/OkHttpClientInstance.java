/**
 * 
 */
package com.apoollo.commons.util.okhttpclient;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.apoollo.commons.util.model.HttpClientResponse;
import com.apoollo.commons.util.okhttpclient.OkHttpClientUtils.HttpClientParam;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author liuyulong6
 */
public class OkHttpClientInstance {

	private OkHttpClient okHttpClient;

	public OkHttpClientInstance(HttpClientParam httpClientParam) {
		this.okHttpClient = OkHttpClientUtils.getOkHttpClient(httpClientParam);
	}

	public String getString(String url, Headers headers) {
		return OkHttpClientUtils.getString(okHttpClient, url, headers);
	}

	public String postAndGetString(String url, Headers headers, RequestBody requestBody) {
		return OkHttpClientUtils.putAndGetString(okHttpClient, url, headers, requestBody);
	}

	public String putAndGetString(String url, Headers headers, RequestBody requestBody) {
		return OkHttpClientUtils.putAndGetString(okHttpClient, url, headers, requestBody);
	}

	public <T> T get(String url, Headers headers, BiFunction<Response, ResponseBody, T> responseFunction) {
		return OkHttpClientUtils.get(okHttpClient, url, headers, responseFunction);
	}

	public <T> HttpClientResponse<T> restfulPost(String url, Headers headers, Object object, Class<T> responseClass) {
		return OkHttpClientUtils.restfulPost(okHttpClient, url, headers, object, responseClass);
	}

	public <T> HttpClientResponse<T> restfulPost(String url, Headers headers, Object object, Function<String, T> fn) {
		return OkHttpClientUtils.restfulPost(okHttpClient, url, headers, object, fn);
	}

	public <T> T post(String url, Headers headers, RequestBody requestBody,
			BiFunction<Response, ResponseBody, T> responseFunction) {
		return OkHttpClientUtils.post(okHttpClient, url, headers, requestBody, responseFunction);
	}

	public <T> T put(String url, Headers headers, RequestBody requestBody,
			BiFunction<Response, ResponseBody, T> responseFunction) {
		return OkHttpClientUtils.put(okHttpClient, url, headers, requestBody, responseFunction);
	}

	public <T> T execute(String url, BiFunction<Request.Builder, RequestBody, Request.Builder> methodFunction,
			Headers headers, RequestBody requestBody, BiFunction<Response, ResponseBody, T> responseFunction) {
		return OkHttpClientUtils.execute(okHttpClient, methodFunction, url, headers, requestBody, responseFunction);
	}
}

/**
 * 
 */
package com.apoollo.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author liuyulong
 * @since 2023年8月10日
 */
public class MdcUtils {

	private static final String REQUEST_ID_KEY = "request-id";

	private static final String TRACE_ID = "traceId";

	public static String getId() {
		String id = MDC.get(REQUEST_ID_KEY);
		if (null == id) {
			id = MDC.get(TRACE_ID);
		}
		return StringUtils.trim(id);
	}

	public static void addAll(String id) {
		if (null != id) {
			addTraceId(id);
			addRequestId(id);
		}
	}

	public static void releaseAll() {
		relaseTraceId();
		relaseRequestId();
	}

	public static void addTraceId(String traceId) {
		MDC.put(TRACE_ID, StringUtils.join(" ", traceId));

	}

	public static void relaseTraceId() {
		MDC.remove(TRACE_ID);
	}

	public static void addRequestId(String requestId) {
		MDC.put(REQUEST_ID_KEY, StringUtils.join(" ", requestId));

	}

	public static void relaseRequestId() {
		MDC.remove(REQUEST_ID_KEY);
	}

}

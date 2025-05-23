/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.List;

/**
 * @author liuyulong
 * @since 2025-05-21
 */
public interface EscapeMethod {

	public String escape(String input);

	public List<String> getSupportEscapeContentTypes();

	public String escapeByContentType(String contentType, String content);
}

/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author liuyulong
 * @since 2025-05-21
 */
public interface EscapeMethod {

	public String escape(String input);

	public List<String> getSupportEscapeContentTypes();

	public byte[] escapeByContentType(Charset charset, String contentType, byte[] content);
}

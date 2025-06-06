/**
 * 
 */
package com.apoollo.commons.util.request.context.core;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.apoollo.commons.util.request.context.EscapeMethod;

/**
 * @author liuyulong
 * @since 2025-05-21
 */
public class DefaultEscapeXss implements EscapeMethod {

	@Override
	public String escape(String input) {
		return StringEscapeUtils.escapeHtml4(input);
	}

	@Override
	public List<String> getSupportEscapeContentTypes() {
		return List.of(MediaType.APPLICATION_JSON_VALUE);
	}

	private class EscapeValueFilter implements ValueFilter {

		@Override
		public Object apply(Object object, String name, Object value) {
			if (value instanceof String) {
				return escape((String) value);
			}
			return value;
		}
	}

	@Override
	public byte[] escapeByContentType(Charset charset, String contentType, byte[] content) {
		if (StringUtils.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)) {
			ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
			JSONReader.Context context = new JSONReader.Context(provider);
			content = JSON.toJSONBytes(JSON.parse(content, 0, content.length, charset, context),
					new EscapeValueFilter());
		}
		return content;
	}

}

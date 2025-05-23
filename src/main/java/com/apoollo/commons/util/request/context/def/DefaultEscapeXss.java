/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.ValueFilter;
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
	public String escapeByContentType(String contentType, String content) {
		if (StringUtils.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)) {
			content = JSON.toJSONString(JSON.parse(content), new EscapeValueFilter());
		}
		return content;
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

}

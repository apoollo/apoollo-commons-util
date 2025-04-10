/**
 * 
 */
package com.apoollo.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.model.KeyValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
public class UrlUtils {

	private static final Pattern PATTERN = Pattern.compile("^(http|https)://([^/]+)([^\\?]+)?\\??(.*)$");

	public static Url parse(String url) {
		Url instance = null;
		if (StringUtils.isNotBlank(url)) {
			Matcher matcher = PATTERN.matcher(url);
			if (matcher.find()) {
				instance = new Url();
				instance.setProtocol(matcher.group(1));
				instance.setDomain(matcher.group(2));
				instance.setPath(matcher.group(3));
				instance.setQuery(new ArrayList<>());
				LangUtils.getStream(matcher.group(4))//
						.flatMap(queryString -> LangUtils.getStream(StringUtils.split(queryString, "&"))) //
						.map(keyValueString -> {
							KeyValue<String, String> keyValue = null;
							if (StringUtils.isNotEmpty(keyValueString)) {
								String[] keyValueArray = StringUtils.split(keyValueString, "=");
								if (null != keyValueArray && 2 == keyValueArray.length) {
									keyValue = new KeyValue<>(keyValueArray[0], keyValueArray[1]);
								}
							}
							return keyValue;
						})//
						.filter(Objects::nonNull)//
						.forEach(instance.getQuery()::add);

			}
		}
		return instance;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Url {
		private String protocol;
		private String domain;
		private String path;
		private List<KeyValue<String, String>> query;

		public Url addQuery(String key, String value) {
			if (null == query) {
				query = new ArrayList<>();
			}
			this.query.add(new KeyValue<String, String>(key, value));
			return this;
		}

		public String serializeQuery() {
			return LangUtils.getStream(query)
					.map(keyValue -> StringUtils.join(keyValue.getKey(), "=", keyValue.getValue()))
					.collect(Collectors.joining("&"));
		}

		public String serializePathQuery() {
			String pathQuery = path;
			if (CollectionUtils.isNotEmpty(query)) {
				pathQuery = StringUtils.join(pathQuery, "?", serializeQuery());
			}
			return pathQuery;
		}

		public String serializeHome() {
			return StringUtils.join(protocol, "://", domain);
		}

		public String serialize() {
			return StringUtils.join(serializeHome(), serializePathQuery());
		}
	}
}

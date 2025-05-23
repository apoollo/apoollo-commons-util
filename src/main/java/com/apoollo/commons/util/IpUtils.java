/**
 * 
 */
package com.apoollo.commons.util;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 */
public class IpUtils {

	private static final String[] IP_HEADERS = new String[] { "x-forwarded-for", "x-real-ip", "proxy_protocol_addr",
			"remote-host", "Proxy-Client-IP", "WL-Proxy-Client-IP" };

	private static String getValue(String value) {
		if (StringUtils.isBlank(value) || "unknown".equalsIgnoreCase(value)) {
			return null;
		}
		return value;
	}

	public static String tryGetRealIp(HttpServletRequest request) {
		String headerValue = LangUtils.getStream(IP_HEADERS)//
				.map(header -> {
					String value = getValue(request.getHeader(header));
					// if (null == value) {
					// value = getValue(request.getHeader(header.toUpperCase()));
					// }
					// if (null == value) {
					// value = getValue(request.getHeader(header.toLowerCase()));
					// }
					return value;
				})//
				.filter(Objects::nonNull)//
				.findFirst()//
				.orElse(null);
		String ip = null;
		if (null != headerValue) {
			if (headerValue.contains(",")) {
				ip = LangUtils.getStream(StringUtils.split(headerValue, ","))//
						.filter(StringUtils::isNotBlank)//
						.findFirst()//
						.orElse(null);
			} else {
				ip = headerValue;
			}
		} else {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static boolean matchesIp(String input) {
		boolean ret = false;
		if (StringUtils.isNotBlank(input)) {
			// String regex0To255Group = "^0|1[0-9]{0,2}|2[0-5]{0,2}$";
			// String regex0To255Group = "^[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-5]{2}$";
			// 1位数|2位数|3位数1开头|3位数2开头,小于250|3位数,250至255
			String regex0To255Group = "^[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]$";

			String[] parts = StringUtils.split(input, ".");

			if (parts.length == 4 || parts.length == 5) {

				ret = !(LangUtils.getStream(parts).filter(part -> !part.matches(regex0To255Group)

				).findAny().isPresent());
			}
		}
		return ret;
	}

}

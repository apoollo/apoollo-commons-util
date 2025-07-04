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

	public static String tryGetClientRealIp(HttpServletRequest request) {
		return LangUtils.getStream(IP_HEADERS)//
				.map(headerKey -> getValue(request.getHeader(headerKey)))//
				.filter(Objects::nonNull)//
				.map(headerValue->{
					if (headerValue.contains(",")) {
						return LangUtils.getStream(StringUtils.split(headerValue, ","))//
								.filter(StringUtils::isNotBlank)//
								.findFirst()//
								.orElse(null);
					}else {
						return headerValue;
					}
				})
				.findFirst()//
				.orElseGet(()->request.getRemoteAddr());
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

/**
 * 
 */
package com.apoollo.commons.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author liuyulong
 */
public class IpUtils {

	private static final String[] IP_HEADERS = new String[] { "x-forwarded-for", "x-real-ip", "proxy_protocol_addr",
			"remote-host", "Proxy-Client-IP", "WL-Proxy-Client-IP" };

	private static volatile String LOCAL = null;

	static {
		try {
			InetAddress address = IpUtils.getIPv4();
			if (address != null) {
				LOCAL = address.getHostAddress();
			}
		} catch (Exception e) {
		}
	}

	public static String getLocalIP() {
		return LOCAL;
	}

	public static List<InetAddress> getIpV4s() throws SocketException {
		ArrayList<InetAddress> v4Interfaces = new ArrayList<>();

		Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
		while (enumeration.hasMoreElements()) {
			NetworkInterface interfaces = enumeration.nextElement();
			if (interfaces.isUp() && !interfaces.isLoopback() && !interfaces.isVirtual()) {
				Enumeration<InetAddress> addresses = interfaces.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
						v4Interfaces.add(address);
					}
				}
			}
		}
		return v4Interfaces;
	}

	private static InetAddress getIPv4() throws SocketException, UnknownHostException {

		List<InetAddress> v4Interfaces = getIpV4s();
		int v4InterfacesSize = v4Interfaces.size();
		if (v4InterfacesSize == 0) {
			return null;
		}
		for (InetAddress address : v4Interfaces) {

			final String host = address.getHostAddress();
			if (host == null) {
				continue;
			}
			if (host.startsWith("127.0")) {
				continue;
			}
			return address;
		}

		return v4Interfaces.get(v4InterfacesSize - 1);
	}

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
				.map(headerValue -> {
					if (headerValue.contains(",")) {
						return LangUtils.getStream(StringUtils.split(headerValue, ","))//
								.filter(StringUtils::isNotBlank)//
								.findFirst()//
								.orElse(null);
					} else {
						return headerValue;
					}
				}).findFirst()//
				.orElseGet(() -> request.getRemoteAddr());
	}

	public static boolean matchesIp(String input) {
		return matchesIp(input, null);
	}

	public static boolean matchesIp(String input, Predicate<String> partAllow) {
		boolean ret = false;
		if (StringUtils.isNotBlank(input)) {
			// 1位数|2位数|3位数1开头|3位数2开头,小于250|3位数,250至255
			String regex0To255Group = "^[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]$";

			String[] parts = StringUtils.split(input, ".");

			if (parts.length == 4 || parts.length == 5) {

				if (null == partAllow) {
					ret = !(LangUtils.getStream(parts).filter(part -> !(part.matches(regex0To255Group))

					).findAny().isPresent());

				} else {
					ret = !(LangUtils.getStream(parts)
							.filter(part -> !(partAllow.test(part) || part.matches(regex0To255Group))).findAny()
							.isPresent());
				}

			}
		}
		return ret;
	}

}

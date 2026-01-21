/**
 * 
 */
package com.apoollo.commons.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 * @since 2026-01-21
 */
public class ExportUtils {

	public static void setDownloadHeaders(HttpServletResponse response, String contentType, String fileName)
			throws UnsupportedEncodingException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		StringBuilder contentDispositionValue = new StringBuilder()//
				.append("attachment; filename=")//
				.append(encodedFileName)//
				.append(";")//
				.append("filename*=")//
				.append("utf-8''")//
				.append(encodedFileName)//
		;
		response.setHeader("Content-disposition", contentDispositionValue.toString());
	}
}

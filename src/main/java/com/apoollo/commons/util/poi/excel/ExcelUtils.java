package com.apoollo.commons.util.poi.excel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.ExportUtils;
import com.apoollo.commons.util.poi.excel.model.PositionWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public class ExcelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

	public static <T> void export(PositionWorkbook positionWorkbook, HttpServletResponse response) {
		ServletOutputStream outputStream = null;
		try {
			ExportUtils.setDownloadHeaders(response,
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
					positionWorkbook.getFileName());
			outputStream = response.getOutputStream();
			export(positionWorkbook, outputStream);
		} catch (IOException e) {
			LOGGER.error("response get output stream has problem,the message is {}", e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("export excel exception,the message is {}", e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	public static <T> void export(PositionWorkbook positionWorkbook, OutputStream stream) throws IOException {
		ExcelComposer excelComposer = new DefaultExcelComposer(positionWorkbook.getWorkbook(), positionWorkbook);
		try (Workbook composeWorkbook = excelComposer.compose()) {
			composeWorkbook.write(stream);
		}
	}

}

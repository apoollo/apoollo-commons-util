package com.apoollo.commons.util.poi.doc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.apoollo.commons.util.ExportUtils;

import jakarta.servlet.http.HttpServletResponse;

public class DocxUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocxUtils.class);

	public static void export(HttpServletResponse response, String fileName, String classPathTemplateFileName,
			List<TableData<?>> tableDatas, Map<String, Object> paragraphMapping) {
		try {
			ExportUtils.setDownloadHeaders(response,
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8", fileName);
			export(classPathTemplateFileName, tableDatas, paragraphMapping, response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("export docx exception,the message is {}", e.getMessage(), e);
			throw new RuntimeException("export error:", e);
		}
	}

	public static <T> void export(String classPathTemplateFileName, List<TableData<?>> tableDatas,
			Map<String, Object> paragraphMapping, OutputStream outputStream) throws IOException {

		ClassPathResource classPathResource = new ClassPathResource(classPathTemplateFileName);
		try (XWPFDocument document = new XWPFDocument(classPathResource.getInputStream())) {

			if (CollectionUtils.isNotEmpty(tableDatas)) {
				List<XWPFTable> tables = document.getTables();

				for (int i = 0; i < tableDatas.size(); i++) {
					@SuppressWarnings("unchecked")
					TableData<T> tableData = (TableData<T>) tableDatas.get(i);
					if (null != tableData) {
						TableUtils.setTableRows(tables.get(i), tableData.getStartRowIndex(), tableData.getDatas(),
								tableData.getConsumer(), tableData.getFunction());
					}
				}
			}
			if (MapUtils.isNotEmpty(paragraphMapping)) {
				List<XWPFParagraph> paragraphs = document.getParagraphs();
				ParagraphUtils.compileParagraphs(paragraphs, paragraphMapping);
			}
			document.write(outputStream);
		}
	}
}

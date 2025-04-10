package com.apoollo.commons.util.poi.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.poi.excel.model.PositionWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public class ExportUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUtils.class);

    public static <T> void exportExcel(PositionWorkbook positionWorkbook, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            setDownloadHeaders(response, positionWorkbook.getFileName());
            outputStream = response.getOutputStream();
            exportExcel(positionWorkbook, outputStream);
        } catch (IOException e) {
            LOGGER.error("response get output stream has problem,the message is {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("export exception,the message is {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (null != outputStream) {
                    response.flushBuffer();
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("close response output stream exception,the message is {}", e.getMessage());
            }
        }
    }

    public static <T> void exportExcel(PositionWorkbook positionWorkbook, OutputStream stream) throws IOException {
        ExcelComposer excelComposer = new DefaultExcelComposer(positionWorkbook.getWorkbook(), positionWorkbook);
        try (Workbook composeWorkbook = excelComposer.compose()) {
            composeWorkbook.write(stream);
        }
    }

    public static void setDownloadHeaders(HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
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

/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class PositionWorkbook {

    private String fileName;

    private Workbook workbook;

    private CellStyle defaultCellStyle;

    private CellStyle dateCellStyle;

    private Map<BiPredicate<Integer, Integer>, Consumer<Cell>> lastConsumers;

    private Map<String, PositionSheet> freeSheets;

    private Map<String, ListSheet> autoListSheets;

    public PositionWorkbook addLastConsumer(BiPredicate<Integer, Integer> predicate, Consumer<Cell> consumer) {
        if (null == lastConsumers) {
            lastConsumers = new HashMap<>();
        }
        lastConsumers.put(predicate, consumer);
        return this;
    }

    public PositionWorkbook addLastCellStyleConsumer(BiPredicate<Integer, Integer> predicate, CellStyle cellStyle) {
        return this.addLastConsumer(predicate, (cell) -> {
            cell.setCellStyle(cellStyle);
        });
    }

    public static CellStyle createTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = createDefaultCellStyle(workbook);
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle createDefaultDateCellStyle(Workbook workbook) {
        CellStyle cellStyle = createDefaultCellStyle(workbook);
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("yyyy/MM/dd HH:mm:ss"));
        return cellStyle;
    }

    public static CellStyle createDefaultCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
    }
}

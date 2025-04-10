/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class PositionSheet {

    private Consumer<Sheet> sheetConsumer;

    private Map<Integer, PositionRow> rows;

    public PositionSheet() {
        super();
    }

    public PositionSheet(Consumer<Sheet> sheetConsumer, Map<Integer, PositionRow> rows) {
        super();
        this.sheetConsumer = sheetConsumer;
        this.rows = rows;
    }

    public PositionSheet addRow(int rowIndex, PositionRow positionRow) {
        if (null == rows) {
            rows = new TreeMap<>();
        }
        PositionRow originPositionRow = rows.get(rowIndex);
        if (null == originPositionRow) {
            rows.put(rowIndex, positionRow);
        } else {
            if (null != positionRow.getRowConsumer()) {
                positionRow.setRowConsumer(positionRow.getRowConsumer());
            }
            if (null != positionRow.getCells()) {
                if (null != originPositionRow.getCells()) {
                    originPositionRow.getCells().putAll(positionRow.getCells());
                } else {
                    originPositionRow.setCells(positionRow.getCells());
                }
            }
        }

        return this;
    }

    public PositionSheet addCell(int rowIndex, int cellIndex, PositionCell positionCell) {
        this.addRow(rowIndex, new PositionRow().addCell(cellIndex, positionCell));
        return this;
    }

    public PositionSheet addSheetConsumer(Consumer<Sheet> sheetConsumer) {
        if (null == this.sheetConsumer) {
            this.sheetConsumer = sheetConsumer;
        } else {
            this.sheetConsumer = this.sheetConsumer.andThen(sheetConsumer);
        }
        return this;
    }

    public PositionSheet addMergedRegionCell(int firstRow, int lastRow, int firstCol, int lastCol,
            PositionCell positionCell) {
        addMergedRegionCell(null, firstRow, lastRow, firstCol, lastCol, positionCell);
        return this;
    }

    public PositionSheet addMergedRegionCell(Workbook workbook, int firstRow, int lastRow, int firstCol, int lastCol,
            PositionCell positionCell) {
        addSheetConsumer((sheet) -> {
            addMergedRegionCell(workbook, sheet, firstRow, lastRow, firstCol, lastCol, positionCell);
        });
        return this;
    }

    private void addMergedRegionCell(Workbook workbook, Sheet sheet, int firstRow, int lastRow, int firstCol,
            int lastCol, PositionCell positionCell) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        IntStream.range(firstRow, lastRow + 1).forEach(rowIndex -> {
            IntStream.range(firstCol, lastCol + 1).forEach(columnIndex -> {
                if (!(rowIndex == firstRow && firstCol == columnIndex)) {
                    addCell(rowIndex, columnIndex, new PositionCell());
                }
            });
        });

        if (null != positionCell) {
            if (null == positionCell.getCellConsumer() && null != workbook) {
                positionCell.setCellConsumer(cell -> {
                    cell.setCellStyle(PositionWorkbook.createTitleCellStyle(workbook));
                });
            }
            addCell(firstRow, firstCol, positionCell);
        }
    }

}

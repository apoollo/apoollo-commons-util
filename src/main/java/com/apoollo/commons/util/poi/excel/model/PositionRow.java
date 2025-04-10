/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Row;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class PositionRow {

    private Consumer<Row> rowConsumer;

    private Map<Integer, PositionCell> cells;

    public PositionRow addCell(int cellIndex, PositionCell positionCell) {
        if (null == cells) {
            cells = new TreeMap<>();
        }
        cells.put(cellIndex, positionCell);
        return this;
    }

}

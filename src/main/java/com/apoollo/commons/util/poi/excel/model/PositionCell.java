/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import java.io.Serializable;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Cell;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class PositionCell {

    private Serializable value;

    private Consumer<Cell> cellConsumer;

    public PositionCell() {
    }

    public PositionCell(Serializable value) {
        this(value, null);
    }

    public PositionCell(Serializable value, Consumer<Cell> cellConsumer) {
        super();
        this.value = value;
        this.cellConsumer = cellConsumer;
    }

}

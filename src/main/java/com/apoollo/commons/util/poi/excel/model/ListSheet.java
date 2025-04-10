/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class ListSheet {

    private CellStyle titlesCellStyle;

    private CellStyle valuesCellStyle;

    private CellStyle dateCellStyle;

    private Integer rowStartIndex;

    private Integer columnStartIndex;

    private Map<Integer, Integer> columnWidth;

    private List<String> titles;

    private List<? extends Object> values;

    private NestedProperties nestedProperties;

    private Boolean rowAutoSerialNumber;

    private Boolean autoMergeCells;
}

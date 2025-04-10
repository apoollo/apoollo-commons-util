/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NestedObjectColumnSeria {

    private Boolean nested;

    private Integer columnIndex;

    private NestedObjectColumnSeria next;

}

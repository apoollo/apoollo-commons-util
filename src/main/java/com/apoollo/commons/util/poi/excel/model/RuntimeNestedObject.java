/**
 * 
 */
package com.apoollo.commons.util.poi.excel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
public class RuntimeNestedObject {

    private int rowIndex;// 当前行数
    private int columnValueIndex;// 当前值列下标
    
    private int columnNestedValueIndex;// 嵌入列当前下标
    private Integer lastNoneNestedObjectColumnValueIndex;//最后一个非嵌入对象的值列下标
}

/**
 * 
 */
package com.apoollo.commons.util.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
public class OCREffectiveDate {

	private YearMonthDate effectiveStartDate;// 有效期开始时间
	private YearMonthDate effectiveEndDate;// 有效期结束时间 
	private Boolean effectiveDateIsLongValid;// 是否是长期
}

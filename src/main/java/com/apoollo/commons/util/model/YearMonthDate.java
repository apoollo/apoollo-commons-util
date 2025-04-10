/**
 * 
 */
package com.apoollo.commons.util.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuyulong
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearMonthDate {
	private Integer year;
	private Integer month;
	private Integer date;

	public Date toSystemDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, date);
		return DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH).getTime();
	}
}

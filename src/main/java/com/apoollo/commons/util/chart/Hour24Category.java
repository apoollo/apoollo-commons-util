package com.apoollo.commons.util.chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.apoollo.commons.util.request.context.CodeName;
import com.apoollo.commons.util.request.context.core.DefaultCodeName;

import lombok.Getter;

@Getter
public class Hour24Category implements Category {

	private Calendar start;
	private Calendar end;

	public Hour24Category() {
		super();

		this.end = Calendar.getInstance();
		this.end.setTime(new Date());
		this.end.set(Calendar.MINUTE, 59);
		this.end.set(Calendar.SECOND, 59);
		this.end.set(Calendar.MILLISECOND, 999);

		this.start = Calendar.getInstance();
		start.setLenient(false);
		this.start.setTime(this.end.getTime());
		this.start.add(Calendar.HOUR_OF_DAY, -23);
		this.start.set(Calendar.MINUTE, 0);
		this.start.set(Calendar.SECOND, 0);
		this.start.set(Calendar.MILLISECOND, 0);
	}

	@Override
	public List<CodeName<String, String>> getCategories() {

		List<CodeName<String, String>> codeNames = new ArrayList<>();

		long startMillis = start.getTimeInMillis();
		long endMillis = end.getTimeInMillis();

		long hourMillis = 1000 * 60 * 60;
		for (long i = startMillis; i <= endMillis; i += hourMillis) {

			String hourFormat = DateFormatUtils.format(i, "yyyyMMddHH");
			String monthDayHourFormat = DateFormatUtils.format(i, "dd日HH时");
			CodeName<String, String> codeName = new DefaultCodeName<>(hourFormat, monthDayHourFormat);
			codeNames.add(codeName);
		}
		return codeNames;
	}
}
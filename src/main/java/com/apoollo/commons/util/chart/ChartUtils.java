/**
 * 
 */
package com.apoollo.commons.util.chart;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.CodeName;

/**
 * @author liuyulong
 */
public class ChartUtils {

	public static HistogramChart getHistogramChart(CategoryGetter categoryGetter,
			List<? extends DataStatistcItemGetter> dataStatistcGetterList) {
		List<CodeName<String, String>> categories = categoryGetter.getCategories(dataStatistcGetterList);
		return new HistogramChart(
				LangUtils.getStream(categories).map(codeName -> codeName.getName()).collect(Collectors.toList()),
				getSeries(categories, dataStatistcGetterList));
	}

	public static List<SeriesItem> getSeries(List<CodeName<String, String>> categories,
			List<? extends DataStatistcItemGetter> dataStatistcItemGetterList) {
		List<String> categoryCodes = LangUtils.getStream(categories).map(CodeName::getCode)
				.collect(Collectors.toList());

		Map<String, SeriesItem> seriesItemMap = new LinkedHashMap<>();

		LangUtils.getStream(dataStatistcItemGetterList)//
				.sorted(Comparator.comparing(DataStatistcItemGetter::getCategoryCode))//
				.forEach(databaseStatisticGetter -> {

					SeriesItem seriesItem = seriesItemMap.get(databaseStatisticGetter.getSeriesItemCode());
					if (null == seriesItem) {
						seriesItem = new SeriesItem();
						List<Number> data = seriesItem.getData();
						LangUtils.getStream(categoryCodes).forEach((category) -> {
							data.add(0);
						});
						seriesItemMap.put(databaseStatisticGetter.getSeriesItemCode(), seriesItem);
					}
					seriesItem.setCode(databaseStatisticGetter.getSeriesItemCode());
					seriesItem.setName(databaseStatisticGetter.getSeriesItemName());
					int index = categoryCodes.indexOf(databaseStatisticGetter.getCategoryCode());
					if (-1 == index) {
						throw new RuntimeException("category not match:" + databaseStatisticGetter.getCategoryCode());
					}
					seriesItem.getData().set(index, databaseStatisticGetter.getValue());
				});
		return LangUtils.getStream(seriesItemMap.values()).collect(Collectors.toList());
	}
}

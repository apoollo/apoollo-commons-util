/**
 * 
 */
package com.apoollo.commons.util.chart;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.CodeName;

/**
 * @author liuyulong
 */
public class ChartUtils {

	public static <L, R, K> void appendSerias(List<L> leftList, Function<L, K> leftKey, Function<L, String> leftName,
			List<R> resultList, Function<R, K> resultKey, BiFunction<K, String, R> resultConstructor) {
		LangUtils.getStream(leftList).forEach(left -> {
			if (!LangUtils.getStream(resultList).filter(result -> leftKey.apply(left).equals(resultKey.apply(result)))
					.findAny().isPresent()) {
				resultList.add(resultConstructor.apply(leftKey.apply(left), leftName.apply(left)));
			}
		});

	}

	public static HistogramChart getHistogramChart(Category category,
			List<? extends DataStatistcItem> dataStatisticItemList) {
		List<CodeName<String, String>> categories = category.getCategories(dataStatisticItemList);
		return new HistogramChart(
				LangUtils.getStream(categories).map(codeName -> codeName.getName()).collect(Collectors.toList()),
				getSeries(categories, dataStatisticItemList));
	}

	public static List<SeriesItem> getSeries(List<CodeName<String, String>> categories,
			List<? extends DataStatistcItem> dataStatisticItemList) {

		List<String> categoryCodes = LangUtils.getStream(categories).map(CodeName::getCode)
				.collect(Collectors.toList());

		Map<String, SeriesItem> seriesItemMap = new LinkedHashMap<>();

		LangUtils.getStream(dataStatisticItemList)//
				.forEach(databaseStatisticItem -> {

					LangUtils.getStream(databaseStatisticItem.getDataSeriesItems()).forEach(dataSeriesItem -> {

						String dataSeriesItemCode = dataSeriesItem.getCode();
						SeriesItem seriesItem = seriesItemMap.get(dataSeriesItemCode);
						if (null == seriesItem) {
							seriesItem = new SeriesItem();
							List<Number> data = seriesItem.getData();
							LangUtils.getStream(categoryCodes).forEach((category) -> {
								data.add(0);
							});
							seriesItemMap.put(dataSeriesItemCode, seriesItem);
						}

						seriesItem.setCode(dataSeriesItemCode);
						seriesItem.setName(dataSeriesItem.getName());
						if (databaseStatisticItem.hasCategory()) {
							if (null == databaseStatisticItem.getCategoryCode()) {
								throw new RuntimeException("categoryCode must not be null");
							}
							int index = categoryCodes.indexOf(databaseStatisticItem.getCategoryCode());
							if (-1 == index) {
								throw new RuntimeException(
										"category not match:" + databaseStatisticItem.getCategoryCode());
							}
							seriesItem.getData().set(index, dataSeriesItem.getValue());
						}

					});

				});

		return LangUtils.getStream(seriesItemMap.values())//
				.sorted(Comparator.comparing(SeriesItem::getCode))//
				.collect(Collectors.toList());
	}
}

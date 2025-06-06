/**
 * 
 */
package com.apoollo.commons.util.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.CodeName;
import com.apoollo.commons.util.request.context.core.DefaultCodeName;

/**
 * @author liuyulong
 */
public interface CategoryGetter {

	public List<CodeName<String, String>> getCategories();

	public default List<CodeName<String, String>> getCategories(
			List<? extends DataStatistcItemGetter> dataStatistcGetterList) {
		List<CodeName<String, String>> codeNames = getCategories();
		if (null == codeNames) {
			Map<String, CodeName<String, String>> categoryMap = new HashMap<>();
			LangUtils.getStream(dataStatistcGetterList).forEach(dataStatistcGetter -> {
				if (categoryMap.isEmpty() || !categoryMap.containsKey(dataStatistcGetter.getCategoryCode())) {
					categoryMap.put(dataStatistcGetter.getCategoryCode(), new DefaultCodeName<String, String>(
							dataStatistcGetter.getCategoryCode(), dataStatistcGetter.getCategoryName()));
				}
			});
			codeNames = LangUtils.getStream(categoryMap.values()).collect(Collectors.toList());
		}
		return codeNames;
	}

}

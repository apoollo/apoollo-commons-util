/**
 * 
 */
package com.apoollo.commons.util.json;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.BeanContext;
import com.alibaba.fastjson2.filter.ContextValueFilter;
import com.alibaba.fastjson2.filter.Filter;
import com.apoollo.commons.util.model.Function3;

/**
 * @author liuyulong6
 * @since 2026-04-09
 */
public class JSONUtils {

	// JSONWriter.Feature.WriteMapNullValue

	public static String toJsonMaskString(Object object, List<MaskProperty> maskProperties,
			JSONWriter.Feature... features) {
		return toJsonString(object, getMaskValueFilter(maskProperties), features);
	}

	public static String toJsonString(Object object, JSONWriter.Feature... features) {
		return toJsonString(object, null, features);
	}

	public static String toJsonString(Object object, Filter filter, JSONWriter.Feature... features) {
		String ret = null;
		if (object instanceof String) {
			ret = (String) object;
		} else {
			ret = JSON.toJSONString(object, filter, features);
		}
		return ret;
	}

	public static Function3<TriggerModel, String, Integer, Object> getMaskFunction() {
		String mask = "***";
		return (TriggerModel triggerModel, String needMaskValue, Integer triggerPropertyValueLength) -> {
			String maskResult = null;
			if (TriggerModel.IMMEDIATE_MASK == triggerModel) {
				maskResult = mask;
			} else if (TriggerModel.MASK_BY_LENGTH == triggerModel) {
				int diff = needMaskValue.length() - triggerPropertyValueLength;
				if (diff > 0) {
					maskResult = needMaskValue.substring(0, triggerPropertyValueLength) + mask;
				} else {
					maskResult = needMaskValue;
				}
			} else {
				maskResult = mask;
			}
			return maskResult;
		};
	}

	public static ContextValueFilter getMaskValueFilter(List<MaskProperty> maskProperties) {
		ContextValueFilter contextValueFilter = null;
		if (null != maskProperties && !maskProperties.isEmpty()) {
			contextValueFilter = (BeanContext context, Object object, String name, Object value) -> {
				Object targetValue = value;
				if (value instanceof Serializable) {
					targetValue = maskProperties.stream()//
							.filter(Objects::nonNull)//
							.filter(maskProperty -> StringUtils.equals(maskProperty.getConditionPropertyName(), name)
									&& (null == maskProperty.getConditionFunction()
											|| maskProperty.getConditionFunction().apply(context, object, name, value)))//
							.map(maskProperty -> {
								Function3<TriggerModel, String, Integer, Object> maskFunction = null == maskProperty
										.getMaskFunction() ? getMaskFunction() : maskProperty.getMaskFunction();

								return maskFunction.apply(maskProperty.getTriggerModel(), value.toString(),
										maskProperty.getTriggerPropertyValueLength());
							})//
							.findAny()//
							.orElse(value);
				}
				return targetValue;
			};
		}
		return contextValueFilter;
	}
}

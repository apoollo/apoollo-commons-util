/**
 * 
 */
package com.apoollo.commons.util.json;

import com.alibaba.fastjson2.filter.BeanContext;
import com.apoollo.commons.util.model.Function3;
import com.apoollo.commons.util.model.Function4;

/**
 * @author liuyulong6
 * @since 2026-04-09
 */
public interface Mask {

	public Function4<BeanContext, Object, String, Object, Boolean> getConditionFunction();

	public default Function3<TriggerModel, String, Integer, Object> getMaskFunction() {
		return JSONUtils.getMaskFunction();
	}
}

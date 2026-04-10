/**
 * 
 */
package com.apoollo.commons.util.json;

import com.alibaba.fastjson2.filter.BeanContext;
import com.apoollo.commons.util.model.Function4;

/**
 * @author liuyulong6
 * @since 2026-04-09
 */
public class DefaultMask implements Mask {

	@Override
	public Function4<BeanContext, Object, String, Object, Boolean> getConditionFunction() {
		return null;
	}

}

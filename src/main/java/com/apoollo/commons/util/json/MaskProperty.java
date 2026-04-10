/**
 * 
 */
package com.apoollo.commons.util.json;

import com.alibaba.fastjson2.filter.BeanContext;
import com.apoollo.commons.util.model.Function3;
import com.apoollo.commons.util.model.Function4;

/**
 * @author liuyulong6
 * @since 2026-04-07
 */
public class MaskProperty {

	// 触发的名称
	private String conditionPropertyName;

	// 触发的函数：FastJSON2上下文，属性名称所在的对象，属性名称，属性值
	private Function4<BeanContext, Object, String, Object, Boolean> conditionFunction;

	// 触发模式
	private TriggerModel triggerModel;

	// 触发属性值的长度
	private Integer triggerPropertyValueLength;

	// 脱敏函数: 触发模式，属性值，触发属性值的长度
	private Function3<TriggerModel, String, Integer, Object> maskFunction;
	
	/**
	 * @param conditionPropertyName
	 * @param conditionFunction
	 * @param triggerModel
	 * @param triggerPropertyValueLength
	 * @param maskFunction
	 */
	public MaskProperty(String conditionPropertyName,
			Function4<BeanContext, Object, String, Object, Boolean> conditionFunction, TriggerModel triggerModel,
			Integer triggerPropertyValueLength, Function3<TriggerModel, String, Integer, Object> maskFunction) {
		super();
		this.conditionPropertyName = conditionPropertyName;
		this.conditionFunction = conditionFunction;
		this.triggerModel = triggerModel;
		this.triggerPropertyValueLength = triggerPropertyValueLength;
		this.maskFunction = maskFunction;
	}

	/**
	 * @return the conditionPropertyName
	 */
	public String getConditionPropertyName() {
		return conditionPropertyName;
	}

	/**
	 * @param conditionPropertyName the conditionPropertyName to set
	 */
	public void setConditionPropertyName(String conditionPropertyName) {
		this.conditionPropertyName = conditionPropertyName;
	}

	/**
	 * @return the conditionFunction
	 */
	public Function4<BeanContext, Object, String, Object, Boolean> getConditionFunction() {
		return conditionFunction;
	}

	/**
	 * @param conditionFunction the conditionFunction to set
	 */
	public void setConditionFunction(Function4<BeanContext, Object, String, Object, Boolean> conditionFunction) {
		this.conditionFunction = conditionFunction;
	}

	/**
	 * @return the triggerModel
	 */
	public TriggerModel getTriggerModel() {
		return triggerModel;
	}

	/**
	 * @param triggerModel the triggerModel to set
	 */
	public void setTriggerModel(TriggerModel triggerModel) {
		this.triggerModel = triggerModel;
	}

	/**
	 * @return the triggerPropertyValueLength
	 */
	public Integer getTriggerPropertyValueLength() {
		return triggerPropertyValueLength;
	}

	/**
	 * @param triggerPropertyValueLength the triggerPropertyValueLength to set
	 */
	public void setTriggerPropertyValueLength(Integer triggerPropertyValueLength) {
		this.triggerPropertyValueLength = triggerPropertyValueLength;
	}

	/**
	 * @return the maskFunction
	 */
	public Function3<TriggerModel, String, Integer, Object> getMaskFunction() {
		return maskFunction;
	}

	/**
	 * @param maskFunction the maskFunction to set
	 */
	public void setMaskFunction(Function3<TriggerModel, String, Integer, Object> maskFunction) {
		this.maskFunction = maskFunction;
	}
	
	
	

}

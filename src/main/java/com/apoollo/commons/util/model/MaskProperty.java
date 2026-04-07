/**
 * 
 */
package com.apoollo.commons.util.model;

/**
 * @author liuyulong6
 * @since 2026-04-07
 */
public class MaskProperty {

	public enum TriggerModel {
		IMMEDIATE_MASK, MASK_BY_LENGTH,
	}

	private String triggerPropertyName;

	private TriggerModel triggerModel;

	private Integer propertyValueLimitLength;

	public MaskProperty(String triggerPropertyName) {
		super();
		this.triggerPropertyName = triggerPropertyName;
	}

	public MaskProperty(String triggerPropertyName, TriggerModel triggerModel) {
		super();
		this.triggerPropertyName = triggerPropertyName;
		this.triggerModel = triggerModel;
	}

	public MaskProperty(String triggerPropertyName, TriggerModel triggerModel, Integer propertyValueLimitLength) {
		super();
		this.triggerPropertyName = triggerPropertyName;
		this.triggerModel = triggerModel;
		this.propertyValueLimitLength = propertyValueLimitLength;
	}

	/**
	 * @return the triggerPropertyName
	 */
	public String getTriggerPropertyName() {
		return triggerPropertyName;
	}

	/**
	 * @param triggerPropertyName the triggerPropertyName to set
	 */
	public void setTriggerPropertyName(String triggerPropertyName) {
		this.triggerPropertyName = triggerPropertyName;
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
	 * @return the propertyValueLimitLength
	 */
	public Integer getPropertyValueLimitLength() {
		return propertyValueLimitLength;
	}

	/**
	 * @param propertyValueLimitLength the propertyValueLimitLength to set
	 */
	public void setPropertyValueLimitLength(Integer propertyValueLimitLength) {
		this.propertyValueLimitLength = propertyValueLimitLength;
	}

}

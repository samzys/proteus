package com.infoscient.proteus.modelica.types;

public class MReal extends MPrimitive {
	public String value, quantity, unit, displayUnit, min, max, start, fixed,
			nominal, stateSelect;


	public void updateFields(String argName, String updatedValue) {
		if (argName.equals("quantity")) {
			this.quantity = updatedValue;
		} else if (argName.equals("unit")) {
			this.unit = updatedValue;
		} else if (argName.equals("displayUnit")) {
			this.displayUnit = updatedValue;
		} else if (argName.equals("min")) {
			this.min = updatedValue;
		} else if (argName.equals("max")) {
			this.max = updatedValue;
		} else if (argName.equals("start")) {
			this.start = updatedValue;
		} else if (argName.equals("fixed")) {
			fixed = updatedValue;
		} else if (argName.equals("nominal")) {
			this.nominal = updatedValue;
		} else if (argName.equals("stateSelect")) {
			stateSelect = updatedValue;
		}
	}

	public void toPrintout() {
		System.out.println("RealType with value of  name=" + name + ", value="
				+ value + ", quantity=" + quantity + ", unit=" + unit
				+ ", displayUnit=" + displayUnit + ", min=" + min + ", max="
				+ max + ", start=" + start + ", fixed=" + fixed + ", nominal="
				+ nominal + ", stateSelected=" + stateSelect);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infoscient.proteus.modelica.types.MPrimitive#getUnit()
	 */
	public String getUnit() {
		if (unit != null)
			return unit;
		if (displayUnit != null)
			return displayUnit;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infoscient.proteus.modelica.types.MPrimitive#getQuantity()
	 */
	@Override
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the displayUnit
	 */
	public String getDisplayUnit() {
		return displayUnit;
	}

	/**
	 * @param displayUnit
	 *            the displayUnit to set
	 */
	public void setDisplayUnit(String displayUnit) {
		this.displayUnit = displayUnit;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the fixed
	 */
	public String getFixed() {
		return fixed;
	}

	/**
	 * @param fixed
	 *            the fixed to set
	 */
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the nominal
	 */
	public String getNominal() {
		return nominal;
	}

	/**
	 * @param nominal
	 *            the nominal to set
	 */
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}

	/**
	 * @return the stateSelected
	 */
	public String getStateSelect() {
		return stateSelect;
	}

	/**
	 * @param stateSelected
	 *            the stateSelected to set
	 */
	public void setStateSelect(String stateSelect) {
		this.stateSelect = stateSelect;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String getType() {
		return "Real";
	}

}

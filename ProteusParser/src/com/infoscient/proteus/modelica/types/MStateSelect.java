package com.infoscient.proteus.modelica.types;

public class MStateSelect extends MPrimitive {
	// type enumeration
	String value;
	String[] allowedChoice;

	// 1. never "Do not use as state as all."
	// 2. avoid
	// "Use as state, if it cannot be avoided (but only if variable appears  differentiated and no other potential state with attribute  default, prefer, or always can be selected).",
	// 3. default
	// "Use as state if appropriate, but only if variable appears differentiated.",
	// 4. prefer
	// "Prefer it as state over those having the default value (also variables can be selected, which do not appear differentiated). ",
	// 5. always "Do use it as a state."


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
	 * @return the allowedChoice
	 */
	public String[] getAllowedChoice() {
		return allowedChoice;
	}

	/**
	 * @param allowedChoice
	 *            the allowedChoice to set
	 */
	public void setAllowedChoice(String[] allowedChoice) {
		this.allowedChoice = allowedChoice;
	}

	public String getUnit() {
		return null;
	}

	public void toPrintout() {

	}

	public void updateFields(String argName, String updatedValue) {

	}

	@Override
	public String getDisplayUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFixed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMax() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNominal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuantity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateSelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return "StateSelect";
	}

}

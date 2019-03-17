package com.infoscient.proteus.modelica.types;

public class MString extends MPrimitive {
	public String value;
	public String start;

	public void updateFields(String argName, String updatedValue) {
		if (updatedValue != null)
			this.start = updatedValue;
	}

	public void toPrintout() {
		System.out.println("String type with value of value=" + value
				+ ", start=" + start);
	}

	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
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
	public String getStateSelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return "String";
	}

}

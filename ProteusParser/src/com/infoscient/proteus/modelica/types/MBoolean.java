package com.infoscient.proteus.modelica.types;

public class MBoolean extends MPrimitive {
	String value;
	String start;

	

	String fixed;

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

	public void updateFields(String argName, String updatedValue) {
		if (argName.equals("start")) {
			this.start = updatedValue;
		} else if (argName.equals("fixed")) {
			this.fixed = updatedValue;
		}
	}

	public void toPrintout() {
		// TODO Auto-generated method stub
		System.out.println("Boolean Type with value of  start=" + start
				+ ", fixed=" + fixed);
	}

	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayUnit() {
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
		return null;
	}

	@Override
	public String getStateSelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return "Boolean";
	}
}

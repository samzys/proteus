package com.infoscient.proteus.modelica.types;

public class MInteger extends MPrimitive {
	public String value, start, min, max, fixed;

	public void updateFields(String argName, String updatedValue) {
		if (argName.equals("min")) {
			this.min = updatedValue;
		} else if (argName.equals("max")) {
			this.max = updatedValue;
		} else if (argName.equals("start")) {
			this.start = updatedValue;
		} else if (argName.equals("fixed")) {
			fixed = updatedValue;
		}
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

	public void toPrintout() {
		// TODO Auto-generated method stub
		System.out.println("Integer Type with value of  name=" + name
				+ ", value=" + value + ", min=" + min + ", max=" + max
				+ ", start=" + start + ", fixed=" + fixed);
	}

	public String getUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	//

	@Override
	public String getDisplayUnit() {
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
		return null;
	}

	@Override
	public String getType() {
		return "Integer";
	}
}

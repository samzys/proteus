package com.infoscient.proteus.modelica.types;

public abstract class MPrimitive {
	public String name;


	public abstract String getType();

	public abstract void updateFields(String argName, String updatedValue);

	public abstract void toPrintout();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract String getUnit();

	public abstract String getQuantity();

	public abstract String getStart();

	public abstract String getDisplayUnit();

	public abstract String getValue();

	public abstract String getFixed();

	public abstract String getStateSelect();

	public abstract String getMin();

	public abstract String getMax();

	public abstract String getNominal();

}
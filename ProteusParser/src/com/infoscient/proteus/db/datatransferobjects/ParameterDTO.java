package com.infoscient.proteus.db.datatransferobjects;


@Deprecated
public class ParameterDTO {
	// this data type will store all of these primitive data in modelica
	protected String type;// Modelica.SIunits.Resistance
	protected String name;// R
	private String value;// 1
	protected boolean inherited;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	public String toString() {
		return inherited + " " + name + " " + value+"\n";
	}
	

}
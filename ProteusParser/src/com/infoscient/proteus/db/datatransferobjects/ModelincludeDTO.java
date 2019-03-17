package com.infoscient.proteus.db.datatransferobjects;

import java.util.HashMap;
import java.util.Map;

public class ModelincludeDTO {
	private String extent;
	private String origin;
	private String rotation;
	private String varName;
	private String typeName;
	private Map<String,String> modification = new HashMap<String,String>();
	public String getExtent() {
		return extent;
	}
	public void setExtent(String extent) {
		this.extent = extent;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getRotation() {
		return rotation;
	}
	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Map<String, String> getModification() {
		return modification;
	}
	
	
}

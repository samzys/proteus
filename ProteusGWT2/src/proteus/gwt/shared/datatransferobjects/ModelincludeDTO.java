package proteus.gwt.shared.datatransferobjects;

import java.util.List;


public class ModelincludeDTO {
	private String extent;
	private String origin;
	private String rotation;
	private String varName;
	private String typeName;
	private String protected_;
	private String final_;
	private String typing;
	private String variability;
	private String causality;
	private String ifFlagName;
	private String arrayForm;
	
	private List<ModificationArgument> modification;
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
	public String getProtected_() {
		return protected_;
	}
	public void setProtected_(String protected_) {
		this.protected_ = protected_;
	}
	public String getFinal_() {
		return final_;
	}
	public String getIfFlagName() {
		return ifFlagName;
	}
	public void setIfFlagName(String ifFlagName) {
		this.ifFlagName = ifFlagName;
	}
	public String getArrayForm() {
		return arrayForm;
	}
	public void setArrayForm(String arrayForm) {
		this.arrayForm = arrayForm;
	}
	public void setFinal_(String final_) {
		this.final_ = final_;
	}
	public String getTyping() {
		return typing;
	}
	public void setTyping(String typing) {
		this.typing = typing;
	}
	public String getVariability() {
		return variability;
	}
	public void setVariability(String variability) {
		this.variability = variability;
	}
	public String getCausality() {
		return causality;
	}
	public void setCausality(String causality) {
		this.causality = causality;
	}
	/**
	 * @return the modification
	 */
	public List<ModificationArgument> getModification() {
		return modification;
	}
	/**
	 * @param modification the modification to set
	 */
	public void setModification(List<ModificationArgument> modification) {
		this.modification = modification;
	}
	
}

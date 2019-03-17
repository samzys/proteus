package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelParameterDTO implements IsSerializable, Serializable {
	
	private static final long serialVersionUID = 1L;
	private String displayUnit;
	private String fixed;
	private boolean header;
	// Integer, Boolean or something else.
	protected boolean inherited;
	private String min;
	private String max;
	// this data type will store all of these primitive data in modelica
	protected String name;// R
	private String nominal;
	private String quantity;// spring constant
	/* sam added 20 , 2011 */
	private String start; // true default for parameter/constant variable. false
							// default for others
	private String stateSelect;

	protected String type;// Modelica.SIunits.Resistance..primitive type Real,

	private String unit;// N/m

	private String value;// 1
	
	private List<EnumerationDTO> enumList = new ArrayList<EnumerationDTO>();
	
	private String annotationGroup;
	
	private String path;
	
	private String comment;
	
	private String desc;
	
	private String annotationTab;
	
	private String annotationInitDialog;
	
	private String typing;
	private String variability;
	private String causality;
	
	private String wholename;
	/**
	 * @return the displayUnit
	 */
	public ModelParameterDTO() {
		
	}
	public String getDisplayUnit() {
		return displayUnit;
	}

	public boolean getHeader() {
		return header;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the nominal
	 */
	public String getNominal() {
		return nominal;
	}

	/**
	 * @return the enumList
	 */
	public List<EnumerationDTO> getEnumList() {
		return enumList;
	}
	public String getWholename() {
		return wholename;
	}
	public void setWholename(String wholename) {
		this.wholename = wholename;
	}
	/**
	 * @return the annotationGroup
	 */
	public String getAnnotationGroup() {
		return annotationGroup;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
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
	 * @return the annotationTab
	 */
	public String getAnnotationTab() {
		return annotationTab;
	}
	/**
	 * @param annotationTab the annotationTab to set
	 */
	public void setAnnotationTab(String annotationTab) {
		this.annotationTab = annotationTab;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @param annotationGroup the annotationGroup to set
	 */
	public void setAnnotationGroup(String annotationGroup) {
		this.annotationGroup = annotationGroup;
	}
	/**
	 * @param enumList the enumList to set
	 */
	public void setEnumList(List<EnumerationDTO> enumList) {
		this.enumList = enumList;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @return the stateSelect
	 */
	public String getStateSelect() {
		return stateSelect;
	}

	/**
	 * @return the annotationInitDialog
	 */
	public String getAnnotationInitDialog() {
		return annotationInitDialog;
	}
	/**
	 * @param annotationInitDialog the annotationInitDialog to set
	 */
	public void setAnnotationInitDialog(String annotationInitDialog) {
		this.annotationInitDialog = annotationInitDialog;
	}
	public String getType() {
		return type;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	public String getValue() {
		return value;
	}

	/**
	 * @return the header
	 */
	public boolean isHeader() {
		return header;
	}

	public boolean isInherited() {
		return inherited;
	}

	/**
	 * @param displayUnit
	 *            the displayUnit to set
	 */
	public void setDisplayUnit(String displayUnit) {
		this.displayUnit = displayUnit;
	}



	/**
	 * @return the fixed
	 */
	public String getFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(boolean header) {
		this.header = header;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param nominal
	 *            the nominal to set
	 */
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @param stateSelect
	 *            the stateSelect to set
	 */
	public void setStateSelect(String stateSelect) {
		this.stateSelect = stateSelect;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param unit
	 * the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return inherited + " " + name + " " + value + "\n";
	}

}
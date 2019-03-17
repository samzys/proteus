package proteus.gwt.server.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigInteger;

/**
 * The persistent class for the parameter database table.
 * 
 */
@Entity
@Table(name = "parameter")
public class Parameter implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManyToOne
	private Component component;

	@Column(name = "DISPLAYUNIT")
	private String displayUnit;

	@Column(name = "FIXED")
	private String fixed;

	@Column(name = "HEADER", columnDefinition = "TINYINT(1)")
	private int header;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "MAX")
	private String max;

	@Column(name = "MIN")
	private String min;

	@Column(name = "NAME")
	private String name;

	@Column(name = "NOMINAL")
	private String nominal;

	@Column(name = "QUANTITY")
	private String quantity;

	@Column(name = "START")
	private String start;

	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "TYPE_ID")
	private Long type_id;
	
	@Column(name = "STATESELECT")
	private String stateSelect;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "VALUE")
	private String value;

	@Column(name= "ANNOTATION_GROUP")
	private String annotationGroup; 
	
	@Column(name = "PARA_DESC")
	private String paraDesc;
	
	@Column(name = "ANNOTATION_TAB")
	private String annotationTab;
	
	@Column(name = "INITIALDIALOG")
	private String annotationInitDialog;
	
	@Column(name = "PROTECTED")
	private String protected_;
	@Column(name = "Final")
	private String final_;
	@Column(name = "TYPING")
	private String typing;
	private String variability;
	private String causality;
	public Parameter() {
	}

	public Component getComponent() {
		return component;
	}

	/**
	 * @return the displayUnit
	 */
	public String getDisplayUnit() {
		return displayUnit;
	}

	/**
	 * @return the header
	 */
	public int getHeader() {
		return header;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
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

	/**
	 * @return the type_id
	 */
	public Long getType_id() {
		return type_id;
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
	 * @param typeId the type_id to set
	 */
	public void setType_id(Long typeId) {
		type_id = typeId;
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
	 * @return the annotationGroup
	 */
	public String getAnnotationGroup() {
		return annotationGroup;
	}

	/**
	 * @param annotationGroup the annotationGroup to set
	 */
	public void setAnnotationGroup(String annotationGroup) {
		this.annotationGroup = annotationGroup;
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
	 * @return the paraDesc
	 */
	public String getParaDesc() {
		return paraDesc;
	}

	/**
	 * @param paraDesc the paraDesc to set
	 */
	public void setParaDesc(String paraDesc) {
		this.paraDesc = paraDesc;
	}

	/**
	 * @return the stateSelect
	 */
	public String getStateSelect() {
		return stateSelect;
	}

	/**
	 * @return the type
	 */
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

	public boolean isHeader() {
		if (header == 0)
			return false;
		else
			return true;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * @param displayUnit
	 *            the displayUnit to set
	 */
	public void setDisplayUnit(String displayUnit) {
		this.displayUnit = displayUnit;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(boolean header) {
		if (header)
			this.header = 1;
		else
			this.header = 0;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(int header) {
		this.header = header;
	}

	public void setId(Long id) {
		this.id = id;
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

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setValue(String value) {
		this.value = value;
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
}
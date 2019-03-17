package proteus.gwt.server.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "includemodification")
public class Includemodification implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Lob()
	@Column(name = "VALUE")
	private String value;

	@Column(name = "START")
	private String start;
	
	@Column(name = "DISPLAYUNIT")
	private String displayUnit;

	@Column(name = "FIXED")
	private String fixed;

	@Column(name = "MAX")
	private String max;

	@Column(name = "MIN")
	private String min;

	@Column(name = "NOMINAL")
	private String nominal;
	
	@Column(name = "QUANTITY")
	private String quantity;
	
	@Column(name = "STATESELECT")
	private String stateSelect;
	
	/**
	 * @return the displayUnit
	 */
	public String getDisplayUnit() {
		return displayUnit;
	}

	/**
	 * @param displayUnit the displayUnit to set
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
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the nominal
	 */
	public String getNominal() {
		return nominal;
	}

	/**
	 * @param nominal the nominal to set
	 */
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the stateSelect
	 */
	public String getStateSelect() {
		return stateSelect;
	}

	/**
	 * @param stateSelect the stateSelect to set
	 */
	public void setStateSelect(String stateSelect) {
		this.stateSelect = stateSelect;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "UNIT")
	private String unit;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Includecomponent includecomponent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Includecomponent getIncludecomponent() {
		return includecomponent;
	}

	public void setIncludecomponent(Includecomponent includecomponent) {
		this.includecomponent = includecomponent;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

}

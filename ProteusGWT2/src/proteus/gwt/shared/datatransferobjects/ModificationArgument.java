package proteus.gwt.shared.datatransferobjects;

public class ModificationArgument {
	public String name;
	public String value;
	public String start;
	public String fixed;
	public String min;
	public String max;
	public String unit;
	public String displayUnit;
	public String quantity;
	public String nominal;
	public String stateSelect;

	@Deprecated
	public ModificationArgument(String name, String value, String start) {
		this.name = name;
		this.value = value;
		this.start = start;
	}

	public ModificationArgument() {
		// TODO Auto-generated constructor stub
	}
	public ModificationArgument(String name, String value, String start,
			String fixed, String min, String max, String unit,
			String displayUnit, String quantity, String nominal,
			String stateSelect) {
		this.name = name;
		this.value = value;
		this.start = start;
		this.fixed = fixed;
		this.quantity = quantity;
		this.displayUnit = displayUnit;
		this.unit = unit;
		this.min = min;
		this.max = max;
		this.nominal = nominal;
		this.stateSelect = stateSelect;
	}

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
	 * @param fixed the fixed to set
	 */
	public void setFixed(String fixed) {
		this.fixed = fixed;
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
}
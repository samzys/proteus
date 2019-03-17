/**
 * 
 */
package proteus.gwt.client.app.ui.canvas;

import proteus.gwt.shared.datatransferobjects.ParameterDTO;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author sam
 * 
 */
public class ParameterProperty extends ParameterDTO {

	private static final long serialVersionUID = 1L;
	private String orignalValue  = null;
	private String orignalStart = null, orignalFixed = null, orignalMin = null,
			orignalMax = null, orignalUnit = null, orignalQuantity = null,
			orignaldisplayUnit = null, orignalNominal = null,
			orignalStateSelect = null;

	private TextBox tbParaValue;

	private TextBox tbParaStart, tbAttStart, tbMin, tbMax, tbDisplayUnit,
			tbNominal, tbQuantity;

	private MyWidget ckParaFixed;

	private ListBox lbFixed, lbStateSelect, lbStart;

	private boolean fixedVar = false;

	private MyWidget paraFixed, paraStart, paraValue;
	private MyWidget attDisplayUnit, attStart, attMin, attMax, attNominal,
			attQuantity, attStateSelect, attFixed;

	/**
	 * @return the orignalValue
	 */
	public String getOrignalValue() {
		return orignalValue;
	}

	/**
	 * @param orignalValue
	 *            the orignalValue to set
	 */
	public void setOrignalValue(String orignalValue) {
		this.orignalValue = orignalValue;
	}

	/**
	 * @return the orignalStart
	 */
	public String getOrignalStart() {
		return orignalStart;
	}


	/**
	 * @param orignalStart
	 *            the orignalStart to set
	 */
	public void setOrignalStart(String orignalStart) {
		this.orignalStart = orignalStart;
	}

	/**
	 * @return the lbStart
	 */
	public ListBox getLbStart() {
		return lbStart;
	}

	/**
	 * @return the orignalFixed
	 */
	public String getOrignalFixed() {
		return orignalFixed;
	}

	/**
	 * @param orignalFixed
	 *            the orignalFixed to set
	 */
	public void setOrignalFixed(String orignalFixed) {
		this.orignalFixed = orignalFixed;
	}

	/**
	 * @return the orignalMin
	 */
	public String getOrignalMin() {
		return orignalMin;
	}

	/**
	 * @param orignalMin
	 *            the orignalMin to set
	 */
	public void setOrignalMin(String orignalMin) {
		this.orignalMin = orignalMin;
	}

	/**
	 * @return the orignalMax
	 */
	public String getOrignalMax() {
		return orignalMax;
	}

	/**
	 * @param orignalMax
	 *            the orignalMax to set
	 */
	public void setOrignalMax(String orignalMax) {
		this.orignalMax = orignalMax;
	}

	/**
	 * @return the orignalUnit
	 */
	public String getOrignalUnit() {
		return orignalUnit;
	}

	/**
	 * @param orignalUnit
	 *            the orignalUnit to set
	 */
	public void setOrignalUnit(String orignalUnit) {
		this.orignalUnit = orignalUnit;
	}

	/**
	 * @return the orignalQuantity
	 */
	public String getOrignalQuantity() {
		return orignalQuantity;
	}

	/**
	 * @param orignalQuantity
	 *            the orignalQuantity to set
	 */
	public void setOrignalQuantity(String orignalQuantity) {
		this.orignalQuantity = orignalQuantity;
	}

	/**
	 * @return the orignaldisplayUnit
	 */
	public String getOrignaldisplayUnit() {
		return orignaldisplayUnit;
	}

	/**
	 * @param orignaldisplayUnit
	 *            the orignaldisplayUnit to set
	 */
	public void setOrignaldisplayUnit(String orignaldisplayUnit) {
		this.orignaldisplayUnit = orignaldisplayUnit;
	}

	/**
	 * @return the orignalNominal
	 */
	public String getOrignalNominal() {
		return orignalNominal;
	}

	/**
	 * @param orignalNominal
	 *            the orignalNominal to set
	 */
	public void setOrignalNominal(String orignalNominal) {
		this.orignalNominal = orignalNominal;
	}

	/**
	 * @return the orignalStateSelect
	 */
	public String getOrignalStateSelect() {
		return orignalStateSelect;
	}

	/**
	 * @param orignalStateSelect
	 *            the orignalStateSelect to set
	 */
	public void setOrignalStateSelect(String orignalStateSelect) {
		this.orignalStateSelect = orignalStateSelect;
	}

	/**
	 * @return the tbAttStart
	 */
	public TextBox getTbAttStart() {
		return tbAttStart;
	}

	/**
	 * @param tbAttStart
	 *            the tbAttStart to set
	 */
	public void setTbAttStart(TextBox tbAttStart) {
		this.tbAttStart = tbAttStart;
	}

	/**
	 * @return the tbMin
	 */
	public TextBox getTbMin() {
		return tbMin;
	}

	/**
	 * @param tbMin
	 *            the tbMin to set
	 */
	public void setTbMin(TextBox tbMin) {
		this.tbMin = tbMin;
	}

	/**
	 * @return the tbMax
	 */
	public TextBox getTbMax() {
		return tbMax;
	}

	/**
	 * @param tbMax
	 *            the tbMax to set
	 */
	public void setTbMax(TextBox tbMax) {
		this.tbMax = tbMax;
	}

	/**
	 * @return the tbDisplayUnit
	 */
	public TextBox getTbDisplayUnit() {
		return tbDisplayUnit;
	}

	/**
	 * @param tbDisplayUnit
	 *            the tbDisplayUnit to set
	 */
	public void setTbDisplayUnit(TextBox tbDisplayUnit) {
		this.tbDisplayUnit = tbDisplayUnit;
	}

	/**
	 * @return the tbNominal
	 */
	public TextBox getTbNominal() {
		return tbNominal;
	}

	/**
	 * @param tbNominal
	 *            the tbNominal to set
	 */
	public void setTbNominal(TextBox tbNominal) {
		this.tbNominal = tbNominal;
	}

	/**
	 * @return the lbFixed
	 */
	public ListBox getLbFixed() {
		return lbFixed;
	}

	/**
	 * @param lbFixed
	 *            the lbFixed to set
	 */
	public void setLbFixed(ListBox lbFixed) {
		this.lbFixed = lbFixed;
	}

	/**
	 * @return the lbStateSelect
	 */
	public ListBox getLbStateSelect() {
		return lbStateSelect;
	}

	/**
	 * @param lbStateSelect
	 *            the lbStateSelect to set
	 */
	public void setLbStateSelect(ListBox lbStateSelect) {
		this.lbStateSelect = lbStateSelect;
	}

	/**
	 * @return the tbParaValue
	 */
	public TextBox getTbParaValue() {
		return tbParaValue;
	}

	/**
	 * @param tbParaValue
	 *            the tbParaValue to set
	 */
	public void setTbParaValue(TextBox tbParaValue) {
		this.tbParaValue = tbParaValue;
	}

	public ParameterProperty() {
	}

	public ParameterProperty(String string, boolean b) {
		this.name = string;
		this.inherited = b;
	}

	public void clearorignalValue() {
		if (this.orignalValue != null) {
			this.setOrignalValue(getValue());
		}

		if (this.orignalStart != null) {
			this.setOrignalStart(getStart());
		}

		if (this.orignalFixed != null) {
			this.setOrignalFixed(getFixed());
		}

		if (this.orignalMin != null) {
			this.setOrignalMin(getMin());
		}
		if (this.orignalMax != null) {
			this.setOrignalMax(getMax());
		}
		if (this.orignalUnit != null) {
			this.setOrignalUnit(getUnit());
		}
		if (this.orignaldisplayUnit != null) {
			this.setOrignaldisplayUnit(getDisplayUnit());
		}
		if (this.orignalNominal != null) {
			this.setOrignalNominal(getNominal());
		}
		if (this.orignalStateSelect != null) {
			this.setOrignalStateSelect(getStateSelect());
		}
		if (this.orignalQuantity != null) {
			this.setOrignalQuantity(getQuantity());
		}
	}

	/**
	 * @return the tbParaStart
	 */
	public TextBox getTbParaStart() {
		return tbParaStart;
	}

	/**
	 * @param tbParaStart
	 *            the tbParaStart to set
	 */
	public void setTbParaStart(TextBox tbParaStart) {
		this.tbParaStart = tbParaStart;
	}

	public String commitChange() {
		String argCodeString = "";
		if (attStart != null) {
			String newVal = attStart.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setStart(orignalStart);
				} else {
					this.setStart(newVal);
					if (orignalStart == null || !orignalStart.equals(newVal)) {
						argCodeString += ", start=" + getStart();
					}
				}
			} else {
				this.setStart(orignalStart);
			}
		}
		if (attFixed != null) {
			String newVal = attFixed.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setFixed(orignalFixed);
				} else {
					this.setFixed(newVal);
					if (orignalFixed == null || !orignalFixed.equals(newVal))
						argCodeString += ", fixed=" + getFixed();
				}
			}
		}

		if (attMin != null) {
			String newVal = attMin.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setMin(orignalMin);
				} else {
					this.setMin(newVal);
					if (orignalMin == null || !orignalMin.equals(newVal))
						argCodeString += ", min=" + getMin();
				}
			}
		}

		if (attMax != null) {
			String newVal = attMax.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setMax(orignalMax);
				} else {
					this.setMax(newVal);
					if (orignalMax == null || !orignalMax.equals(newVal))
						argCodeString += ", max=" + getMax();
				}
			}
		}

		if (attDisplayUnit != null) {
			String newVal = attDisplayUnit.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setDisplayUnit(orignaldisplayUnit);
				} else {
					this.setDisplayUnit(newVal);
					if (orignaldisplayUnit == null
							|| !orignaldisplayUnit.equals(newVal))
						argCodeString += ", displayUnit=" + getDisplayUnit();
				}
			}
		}

		if (attQuantity != null) {
			String newVal = attQuantity.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setQuantity(orignalQuantity);
				} else {
					this.setQuantity(newVal);
					if (orignalQuantity == null
							|| !orignalQuantity.equals(newVal))
						argCodeString += ", quantity=" + getQuantity();
				}
			}
		}

		if (attNominal != null) {
			String newVal = attNominal.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setNominal(orignalNominal);
				} else {
					this.setNominal(newVal);
					if (orignalNominal == null
							|| !orignalNominal.equals(newVal))
						argCodeString += ", nominal=" + getNominal();
				}
			}
		}
		if (attStateSelect != null) {
			String newVal = attStateSelect.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					this.setStateSelect(orignalStateSelect);
				} else {
					this.setStateSelect(newVal);
					if (orignalStateSelect == null
							|| !orignalStateSelect.equals(newVal))
						argCodeString += ", stateSelect=" + getStateSelect();
				}
			}
		}

		// remove the first "," add ()
		boolean variableActive = false;
		if (argCodeString != "" && argCodeString.length() > 0) {
			if (argCodeString.startsWith(",")) {
				argCodeString = argCodeString.substring(1);
				argCodeString = "(" + argCodeString + ")";
				variableActive = true;
			}
		}

		if (paraValue != null) {
			String newVal = paraValue.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				if (newVal.equals(MyCheckBox.INHERITED_STATE)) {
					setValue(orignalValue);
				} else {
					setValue(newVal);
					if (orignalValue == null || !orignalValue.equals(newVal)) {
						argCodeString += "=" + getValue();
						variableActive = true;
					}
				}
			}
		}

		if (variableActive) {
			return getName() + argCodeString;
		}
		return null;
	}

	/**
	 * @return the attDisplayUnit
	 */
	public MyWidget getAttDisplayUnit() {
		return attDisplayUnit;
	}

	/**
	 * @param attDisplayUnit
	 *            the attDisplayUnit to set
	 */
	public void setAttDisplayUnit(MyWidget attDisplayUnit) {
		this.attDisplayUnit = attDisplayUnit;
	}

	/**
	 * @return the attStart
	 */
	public MyWidget getAttStart() {
		return attStart;
	}

	/**
	 * @param attStart
	 *            the attStart to set
	 */
	public void setAttStart(MyWidget attStart) {
		this.attStart = attStart;
	}

	/**
	 * @return the attMin
	 */
	public MyWidget getAttMin() {
		return attMin;
	}

	/**
	 * @param attMin
	 *            the attMin to set
	 */
	public void setAttMin(MyWidget attMin) {
		this.attMin = attMin;
	}

	/**
	 * @return the attMax
	 */
	public MyWidget getAttMax() {
		return attMax;
	}

	/**
	 * @param attMax
	 *            the attMax to set
	 */
	public void setAttMax(MyWidget attMax) {
		this.attMax = attMax;
	}

	/**
	 * @return the attNominal
	 */
	public MyWidget getAttNominal() {
		return attNominal;
	}

	/**
	 * @param attNominal
	 *            the attNominal to set
	 */
	public void setAttNominal(MyWidget attNominal) {
		this.attNominal = attNominal;
	}

	/**
	 * @return the attQuantity
	 */
	public MyWidget getAttQuantity() {
		return attQuantity;
	}

	/**
	 * @param attQuantity
	 *            the attQuantity to set
	 */
	public void setAttQuantity(MyWidget attQuantity) {
		this.attQuantity = attQuantity;
	}

	/**
	 * @return the attStateSelect
	 */
	public MyWidget getAttStateSelect() {
		return attStateSelect;
	}

	/**
	 * @param attStateSelect
	 *            the attStateSelect to set
	 */
	public void setAttStateSelect(MyWidget attStateSelect) {
		this.attStateSelect = attStateSelect;
	}

	/**
	 * @return the attFixed
	 */
	public MyWidget getAttFixed() {
		return attFixed;
	}

	/**
	 * @param attFixed
	 *            the attFixed to set
	 */
	public void setAttFixed(MyWidget attFixed) {
		this.attFixed = attFixed;
	}

	/**
	 * @return the paraFixed
	 */
	public MyWidget getParaFixed() {
		return paraFixed;
	}

	/**
	 * @param paraFixed
	 *            the paraFixed to set
	 */
	public void setParaFixed(MyWidget paraFixed) {
		this.paraFixed = paraFixed;
	}

	/**
	 * @return the paraStart
	 */
	public MyWidget getParaStart() {
		return paraStart;
	}

	/**
	 * @param paraStart
	 *            the paraStart to set
	 */
	public void setParaStart(MyWidget paraStart) {
		this.paraStart = paraStart;
	}

	/**
	 * @return the paraValue
	 */
	public MyWidget getParaValue() {
		return paraValue;
	}

	/**
	 * @param paraValue
	 *            the paraValue to set
	 */
	public void setParaValue(MyWidget paraValue) {
		this.paraValue = paraValue;
	}

	@Deprecated
	public String commitChange0() {
		String argCodeString = "";
		if (tbParaStart != null) {
			String newStart = tbParaStart.getValue();
			if (newStart != null && !newStart.equals("")) {
				this.setStart(newStart);
				if (orignalStart == null || !orignalStart.equals(newStart)) {
					argCodeString += ", start=" + getStart();
				}
			}
		}
		if (ckParaFixed != null && fixedVar) {
			String newFixed = ckParaFixed.getInputValue();
			if (newFixed != null && newFixed != "") {
				this.setFixed(newFixed);
				if (orignalFixed == null || !orignalFixed.equals(newFixed))
					argCodeString += ", fixed=" + getFixed();
			}
		}

		if (tbMin != null) {
			String newMin = tbMin.getValue();
			if (newMin != null && newMin != "") {
				this.setMin(newMin);
				if (orignalMin == null || !orignalMin.equals(newMin))
					argCodeString += ", min=" + getMin();
			}
		}

		if (tbMax != null) {
			String newMax = tbMax.getValue();
			if (newMax != null && newMax != "") {
				this.setMax(newMax);
				if (orignalMax == null || !orignalMax.equals(newMax))
					argCodeString += ", max=" + getMax();
			}
		}

		if (tbDisplayUnit != null) {
			String newDisplayUnit = tbDisplayUnit.getValue();
			if (newDisplayUnit != null && newDisplayUnit != "") {
				this.setDisplayUnit(newDisplayUnit);
				if (orignaldisplayUnit == null
						|| !orignaldisplayUnit.equals(newDisplayUnit))
					argCodeString += ", displayUnit=" + getDisplayUnit();
			}
		}

		if (tbQuantity != null) {
			String newVal = tbQuantity.getValue();
			if (newVal != null && newVal != "") {
				this.setQuantity(newVal);
				if (orignalQuantity == null || !orignalQuantity.equals(newVal))
					argCodeString += ", quantity=" + getQuantity();
			}
		}

		if (tbNominal != null) {
			String newNominal = tbNominal.getValue();
			if (newNominal != null && !newNominal.equals("")) {
				this.setNominal(newNominal);
				if (orignalNominal == null
						|| !orignalNominal.equals(newNominal))
					argCodeString += ", nominal=" + getNominal();
			}
		}
		if (lbStateSelect != null) {
			String newStateSelect = lbStateSelect.getValue(lbStateSelect
					.getSelectedIndex());
			if (newStateSelect != null && !newStateSelect.equals("")) {
				this.setStateSelect(newStateSelect);
				if (orignalStateSelect == null
						|| !orignalStateSelect.equals(newStateSelect))
					argCodeString += ", stateSelect=" + getStateSelect();
			}
		}

		// remove the first "," add ()
		boolean variableActive = false;
		if (argCodeString != "" && argCodeString.length() > 0) {
			if (argCodeString.startsWith(",")) {
				argCodeString = argCodeString.substring(1);
				argCodeString = "(" + argCodeString + ")";
				variableActive = true;
			}
		}

		if (tbParaValue != null) {
			String val = tbParaValue.getValue();
			if (val != null && val != "") {
				setValue(val);
				if (orignalValue == null || !orignalValue.equals(val)) {
					argCodeString += "=" + getValue();
					variableActive = true;
				}
			}
		}

		if (variableActive) {
			return getName() + argCodeString;
		}
		return null;
	}


	public void parameter2Attribute() {
		if (attFixed != null && paraFixed != null) {
			String newVal = paraFixed.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				attFixed.setInputValue(newVal);
			}
		}
		if (attStart != null && paraStart != null) {
			String newVal = paraStart.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				attStart.setInputValue(newVal);
			}else
				attStart.setInputValue(null);
		}
	}

	public void attribute2Parameter() {
		if (attFixed != null && paraFixed != null) {
			String newVal = attFixed.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				setFixedVar(true);
				paraFixed.setInputValue(newVal);
			}
		}

		if (attStart != null && paraStart != null) {
			String newVal = attStart.getInputValue();
			if (newVal != null && !newVal.equals("")) {
				paraStart.setInputValue(newVal);
			} else {
				paraStart.setInputValue(null);
			}
		}
	}

	public void setAttFixed(ValueChangeEvent<Boolean> event) {
		if (lbFixed != null) {
			if (event.getValue()) {
				lbFixed.setSelectedIndex(2);
			} else
				lbFixed.setSelectedIndex(1);
		}
	}

	public void resetAttBox() {
		if (tbAttStart != null)
			tbAttStart.setValue(tbParaStart.getValue());
		if (tbMin != null)
			tbMin.setValue(getMin());
		if (tbMax != null)
			tbMax.setValue(getMax());
		if (tbDisplayUnit != null)
			tbDisplayUnit.setValue(getDisplayUnit());
		if (tbQuantity != null)
			tbQuantity.setValue(getQuantity());
		if (tbNominal != null)
			tbNominal.setValue(getNominal());
		if (lbFixed != null) {
			String val = getFixed();
			if (fixedVar)
				val = ckParaFixed.getInputValue();
			if (val == null) {
				lbFixed.setSelectedIndex(0);
			} else {
				if (val.equals("true")) {
					lbFixed.setSelectedIndex(2);
				} else {
					lbFixed.setSelectedIndex(1);
				}
			}
		}
		if (lbStateSelect != null) {
			String val = getStateSelect();
			if (val == null) {
				lbStateSelect.setSelectedIndex(0);
			} else {
				if (val.equals("StateSelect.never")) {
					lbStateSelect.setSelectedIndex(1);
				} else if (val.equals("StateSelect.avoid")) {
					lbStateSelect.setSelectedIndex(2);
				} else if (val.equals("StateSelect.default")) {
					lbStateSelect.setSelectedIndex(3);
				} else if (val.equals("StateSelect.prefer")) {
					lbStateSelect.setSelectedIndex(4);
				} else if (val.equals("StateSelect.always")) {
					lbStateSelect.setSelectedIndex(5);
				}
			}
		}
	}

	public void setAttStart(ValueChangeEvent<String> event) {
		if (tbAttStart != null) {
			tbAttStart.setText(event.getValue());
		}
	}

	public void updateValue() {
		// String val;
		// val = getValue();
		// if (tbParaValue != null)
		// tbParaValue.setValue(val);
		//
		// val = getStart();
		// if (tbParaStart != null)
		// tbParaStart.setValue(val);

		// if (ckParaFixed != null) {
		// if ((val = getFixed()) != null) {
		// if (val.equals("true")) {
		// ckParaFixed.setValue(true);
		// } else {
		// ckParaFixed.setValue(false);
		// }
		// } else {
		// ckParaFixed.setValue(false);
		// }
		// }
		
	}

	public void setFixedVar(boolean b) {
		this.fixedVar = b;
	}

	/**
	 * @return the fixedVar
	 */
	public boolean isFixedVar() {
		return fixedVar;
	}

	/**
	 * @param tbQuantity
	 *            the tbQuantity to set
	 */
	public void setTbQuantity(TextBox tbQuantity) {
		this.tbQuantity = tbQuantity;
	}

	/**
	 * @return the tbQuantity
	 */
	public TextBox getTbQuantity() {
		return tbQuantity;
	}

	public void setLbStart(ListBox lbStart) {
		// TODO Auto-generated method stub

	}
}

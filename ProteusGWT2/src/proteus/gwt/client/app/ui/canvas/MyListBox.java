package proteus.gwt.client.app.ui.canvas;

import java.util.List;

import proteus.gwt.shared.datatransferobjects.EnumerationDTO;

import com.google.gwt.user.client.ui.ListBox;

public class MyListBox extends ListBox implements MyWidget {
	public static final int STATESELECT = 0, BOOLEAN = 1;

	public MyListBox(int type, String defaultSelection) {
		if (type == STATESELECT) {
			this.addItem("", MyCheckBox.INHERITED_STATE);
			this.addItem("never(Do not use as state at all)",
					"StateSelect.never");
			this.addItem("avoid(Use as state if can't be avoided)",
					"StateSelect.avoid");
			this.addItem("default(Use as state if appropriate)",
					"StateSelect.default");
			this.addItem("prefer(Prefer it as state over default)",
					"StateSelect.prefer");
			this.addItem("always(Use as state always)", "StateSelect.always");

		} else if (type == BOOLEAN) {
			this.addItem("", MyCheckBox.INHERITED_STATE);
			this.addItem("false", "false");
			this.addItem("true", "true");
		}
		
	}

	public MyListBox(List<EnumerationDTO> enumList) {
		if (enumList != null && enumList.size() > 0) {
			this.addItem("", MyCheckBox.INHERITED_STATE);
			for (EnumerationDTO enu : enumList) {
				this.addItem(enu.getName(), enu.getName());
			}
		}
	}

	public String getInputValue() {
		return super.getValue(super.getSelectedIndex());
	}

	@Override
	public void setInputValue(String value) {
		if (value != null) {
			for (int i = 0; i < getItemCount(); i++) {
				String v = getValue(i);
				if (v != null && v.equals(value)) {
					setSelectedIndex(i);
					break;
				}
			}
		} else {
			setSelectedIndex(0);
		}
	}

}
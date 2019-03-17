package proteus.gwt.client.app.ui.canvas;

import com.google.gwt.user.client.ui.TextBox;

public class MyTextBox extends TextBox implements MyWidget {

	
	@Override
	public String getInputValue() {
		return super.getValue();
	}

	@Override
	public void setInputValue(String value) {
		super.setValue(value);
	}
}

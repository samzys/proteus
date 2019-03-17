package proteus.gwt.client.app.ui;

/**
 * 
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class NewComponentDialog extends SimplePanel {

	private static MessageDialogUiBinder uiBinder = GWT
			.create(MessageDialogUiBinder.class);

	interface MessageDialogUiBinder extends
			UiBinder<DialogBox, NewComponentDialog> {
	}

	interface GlobalResources extends ClientBundle {
		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	public @UiField
	DialogBox box;
	public @UiField
	TextBox tbName;
	public @UiField
	TextBox tbDescription;
	public @UiField
	Button btOk;
	public @UiField
	Button btCancel;

	public @UiField
	SimpleCheckBox final_;
	public @UiField
	SimpleCheckBox encapsulated;
	public @UiField
	SimpleCheckBox partial;

	public NewComponentDialog() {
		add(uiBinder.createAndBindUi(this));
		GWT.<GlobalResources> create(GlobalResources.class).css()
				.ensureInjected();
	}

}

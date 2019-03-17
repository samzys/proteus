package proteus.gwt.client.app.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class ComponentsTreePanel extends Composite {

	private static ComponentsTreeUiBinder uiBinder = GWT
			.create(ComponentsTreeUiBinder.class);

	interface ComponentsTreeUiBinder extends
			UiBinder<Widget, ComponentsTreePanel> {
	}

	interface GlobalResources extends ClientBundle {
		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	public @UiField
	Tree classNamesTree;

	public @UiField
	TextBox searchBox;
	public @UiField
	ToggleButton filterButton;

	public @UiField
	Tree canvasComponentsTree;

	//

	public ComponentsTreePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.<GlobalResources> create(GlobalResources.class).css()
				.ensureInjected();
	}

}

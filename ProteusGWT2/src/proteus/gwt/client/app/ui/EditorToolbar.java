package proteus.gwt.client.app.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class EditorToolbar extends Composite {

	private static EditorToolbarUiBinder uiBinder = GWT
			.create(EditorToolbarUiBinder.class);

	interface GlobalResources extends ClientBundle {
		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	public interface SimulateBtnStyle extends CssResource {
		  String running(); 
		  String stopped();
	}
	
	interface EditorToolbarUiBinder extends UiBinder<Widget, EditorToolbar> {
	}

	public @UiField
	PushButton pbNewModule;

	public @UiField
	PushButton pbSave;

	public @UiField
	PushButton pbUndo;
	
	public @UiField
	PushButton pbRedo;
	
	public @UiField
	PushButton pbZoomIn;
	
	public @UiField
	PushButton pbZoomOut;
	
	public @UiField
	PushButton pbZoomReset;
	
	public @UiField
	PushButton pbRotateClockwise;
	
	public @UiField
	PushButton pbRotateAntiClockwise;
	
	public @UiField
	PushButton pbFlipHorizontal;
	
	public @UiField
	PushButton pbFlipVertical;
	
//	public @UiField
//	PushButton pbZoom;

	public @UiField
	PushButton pbSimulate;

	public @UiField
	PushButton pbSimulateResult;
	
	public @UiField SimulateBtnStyle simulateBtnStyle;
	
	public EditorToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
		// Inject global styles.
		GWT.<GlobalResources> create(GlobalResources.class).css()
				.ensureInjected();
	}
}

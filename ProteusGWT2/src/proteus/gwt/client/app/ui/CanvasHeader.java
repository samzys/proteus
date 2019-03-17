package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.ClearMessagePanelEvent;
import proteus.gwt.client.app.event.LogMessagePanelEvent;
import proteus.gwt.client.app.ui.MessagePanel.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class CanvasHeader extends Composite {

	private static CanvasHeaderUiBinder uiBinder = GWT
			.create(CanvasHeaderUiBinder.class);

	interface CanvasHeaderUiBinder extends
			UiBinder<Widget, CanvasHeader> {
	}

	public @UiField
	Image imgIcon;

	public @UiField
	Image imgDiagram;

	public @UiField
	Image imgCode;

	public @UiField
	Image imgDoc;

	public CanvasHeader() {
		initWidget(uiBinder.createAndBindUi(this));
		//bind();

	}

	private void bind() {
		imgIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				imgIcon.setStyleName("proteuscnvashead-images-selected ");
				imgDiagram.setStyleName("proteuscnvashead-images");
				imgCode.setStyleName("proteuscnvashead-images");
				imgDoc.setStyleName("proteuscnvashead-images");
				ProteusGWT.eventBus
						.fireEvent(new LogMessagePanelEvent(
								Message.GENERAL,
								"this is the General test"));
//				ProteusGWT.eventBus.fireEvent(new ClearMessagePanelEvent(
//						Message.ERROR));
			}
		});

		imgDiagram.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				imgDiagram.setStyleName("proteuscnvashead-images-selected ");
				imgIcon.setStyleName("proteuscnvashead-images");
				imgCode.setStyleName("proteuscnvashead-images");
				imgDoc.setStyleName("proteuscnvashead-images");
				ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
						Message.INFO, "this is the Info test"));
//				ProteusGWT.eventBus.fireEvent(new ClearMessagePanelEvent(
//						Message.GENERAL));
			}
		});

		imgCode.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				imgCode.setStyleName("proteuscnvashead-images-selected ");
				imgIcon.setStyleName("proteuscnvashead-images");
				imgDiagram.setStyleName("proteuscnvashead-images");
				imgDoc.setStyleName("proteuscnvashead-images");
				ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
						Message.WARNING, "this is the WARNING test"));
				ProteusGWT.eventBus.fireEvent(new ClearMessagePanelEvent(
						Message.INFO));
			}
		});

		imgDoc.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				imgDoc.setStyleName("proteuscnvashead-images-selected ");
				imgIcon.setStyleName("proteuscnvashead-images");
				imgDiagram.setStyleName("proteuscnvashead-images");
				imgCode.setStyleName("proteuscnvashead-images");
				ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
						Message.ERROR, "this is the ERROR test"));
				ProteusGWT.eventBus.fireEvent(new ClearMessagePanelEvent(
						Message.WARNING));
			}
		});
	}
}

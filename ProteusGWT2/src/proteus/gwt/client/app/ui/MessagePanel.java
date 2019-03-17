package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.util.HTMLGetter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessagePanel extends Composite {

	public enum Message {
		GENERAL, INFO, WARNING, ERROR, ALL
	}

	private static InfoMsgPanelUiBinder uiBinder = GWT
			.create(InfoMsgPanelUiBinder.class);

	interface InfoMsgPanelUiBinder extends UiBinder<Widget, MessagePanel> {
	}

	interface GlobalResources extends ClientBundle {
		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	@UiField
	HTML htmlGeneral;
	@UiField
	HTML htmlInfo;
	@UiField
	HTML htmlWarning;
	@UiField
	HTML htmlError;
	@UiField
	TabLayoutPanel tabPanel;
	@UiField
	ScrollPanel scrollGeneral;
	@UiField
	ScrollPanel scrollInfo;
	@UiField
	ScrollPanel scrollWarning;
	@UiField
	ScrollPanel scrollError;

	public MessagePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		GWT.<GlobalResources> create(GlobalResources.class).css()
				.ensureInjected();
	}

	public String logGeneral(String s) {

		s = htmlGeneral.getHTML() + s + "<br/>";

		htmlGeneral.setHTML(s);
		scrollGeneral.scrollToBottom();
		tabPanel.selectTab(0);
		return s;
	};

	public String logInfo(String s) {
		s = HTMLGetter.getTextWithColor(s, "green");
		s = htmlInfo.getHTML() + s + "<br/>";
		htmlInfo.setHTML(s);
		scrollInfo.scrollToBottom();
		tabPanel.selectTab(1);
		return s;
	};

	public String logWarning(String s) {
		s = HTMLGetter.getTextWithColor(s, "rgb(255, 153, 0)");
		s = htmlWarning.getHTML() + s + "<br/>";
		htmlWarning.setHTML(s);
		scrollWarning.scrollToBottom();
		tabPanel.selectTab(2);
		return s;
	};

	public String logError(String s) {
		s = HTMLGetter.getTextWithColor(s, "red");
		s = htmlError.getHTML() + s + "<br/>";
		htmlError.setHTML(s);
		scrollError.scrollToBottom();
		tabPanel.selectTab(3);
		return s;
	};

	public void clearInfo() {
		htmlInfo.setHTML("");
	};

	public void clearError() {
		htmlError.setHTML("");
	};

	public void clearGeneral() {
		htmlGeneral.setHTML("");
	};

	public void clearWarning() {
		htmlWarning.setHTML("");
	};

	public void clearAll() {
		htmlInfo.setHTML("");
		htmlGeneral.setHTML("");
		htmlError.setHTML("");
		htmlWarning.setHTML("");
	};

	public void resetAll(String generalLog, String infoLog, String warningLog,
			String errorLog) {
		htmlInfo.setHTML(infoLog);
		htmlGeneral.setHTML(generalLog);
		htmlError.setHTML(errorLog);
		htmlWarning.setHTML(warningLog);
	}
}

package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.event.TabTitleHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TabLink extends Composite {

	private static TabLinkUiBinder uiBinder = GWT.create(TabLinkUiBinder.class);

	public @UiField
	Image imgPrevious;
	public @UiField
	Image imgNext;
	interface TabLinkUiBinder extends UiBinder<Widget, TabLink> {
	}
    private TabTitleHandler handler;
	public TabLink(TabTitleHandler handler) {
		initWidget(uiBinder.createAndBindUi(this));
		this.handler = handler;
	}

	
	@UiHandler("imgPrevious")
	void onImgPreviousClick(ClickEvent event) {
		handler.previousTab();
	}


	
	@UiHandler("imgNext")
	void onImgNextClick(ClickEvent event) {
		handler.nextTab();
	}
}

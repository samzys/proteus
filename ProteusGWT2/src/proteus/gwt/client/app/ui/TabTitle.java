package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.event.TabTitleHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabTitle extends Composite {

	private static TabTitleUiBinder uiBinder = GWT
			.create(TabTitleUiBinder.class);

	interface TabTitleUiBinder extends UiBinder<Widget, TabTitle> {
	}

	public @UiField
	Label tabText;
	public @UiField
	Image image;
	private TabTitleHandler handler;
	private PopupPanel popupPanel;

	private String wholeText;
	
	public String getWholeText() {
		return wholeText;
	}

	public TabTitle(String text, final TabTitleHandler handler) {
		initWidget(uiBinder.createAndBindUi(this));
		this.handler = handler;
		this.wholeText=text;
		if(text.length()>9){
			text = text.substring(0,9)+"..";
		}
		tabText.setText(text);
		
		tabText.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.selected(TabTitle.this);
			}
		});
		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				handler.close(TabTitle.this);
			}
		});
		image.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				image.setUrl("resources/images/close_focus.png");

			}
		});
		image.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				image.setUrl("resources/images/close.png");
			}

		});

		image.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				image.setUrl("resources/images/close_click.png");

			}
		});

	}

	private Command closeCommand = new Command() {

		@Override
		public void execute() {
			handler.close(TabTitle.this);
			popupPanel.hide();
		}

	};
	
	private Command closeAllCommand = new Command() {

		@Override
		public void execute() {
			handler.closeAll();
			popupPanel.hide();
		}

	};
	private Command closeOthersCommand = new Command() {

		@Override
		public void execute() {
			handler.closeOthers(TabTitle.this);
			popupPanel.hide();
		}

	};

	@UiHandler("tabText")
	void onTabTextMouseDown(MouseDownEvent event) {
		if (event.getNativeEvent().getButton() == NativeEvent.BUTTON_RIGHT) {

			popupPanel = new PopupPanel(true);
			MenuBar popupMenuBar = new MenuBar(true);

			MenuItem closeItem = new MenuItem("Close", true, closeCommand);
			MenuItem closeAllItem = new MenuItem("Close All ", true,
					closeAllCommand);
			MenuItem closeOthersItem = new MenuItem("Close Others ", true,
					closeOthersCommand);
			
			popupMenuBar.addItem(closeItem);
			popupMenuBar.addItem(closeAllItem);
			popupMenuBar.addItem(closeOthersItem);

			popupMenuBar.setVisible(true);
			popupPanel.add(popupMenuBar);
			int x = event.getClientX();
			int y = event.getClientY();
			popupPanel.setPopupPosition(x, y);
			popupPanel.show();
		}
	}
}

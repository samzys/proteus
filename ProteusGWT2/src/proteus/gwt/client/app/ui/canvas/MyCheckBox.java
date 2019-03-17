package proteus.gwt.client.app.ui.canvas;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

public class MyCheckBox extends CheckBox implements MyWidget {

	private PopupPanel popPanel = new PopupPanel(true);
	public final static String TRUE_STATE = "true", FALSE_STATE = "false",
			INHERITED_STATE = "Inherited";
	private String state = INHERITED_STATE;

	private String text0 = "True: start-value is used to initialize",
			text1 = "False: start-value is only a guess-value",
			text2 = "Inherited: (False: start-value is only a guess-value)";
	private MenuItem item0, item1, item2;

	public MyCheckBox() {
		super();

		MenuBar popupMenuBar = new MenuBar(true);
		popupMenuBar.setTitle("Fixed");

		item0 = new MenuItem(
				"&nbsp;&nbsp;&nbsp;&nbsp;True: start-value is used to initialize",
				true, new Command() {

					@Override
					public void execute() {
						popPanel.hide();
						setState(TRUE_STATE);
					}
				});

		item1 = new MenuItem(
				"&nbsp;&nbsp;&nbsp;&nbsp;False: start-value is only a guess-value",
				true, new Command() {

					@Override
					public void execute() {
						popPanel.hide();
						setState(FALSE_STATE);
					}
				});
		item2 = new MenuItem(
				"<b>V</b>&nbsp;&nbsp;Inherited: (False: start-value is only a guess-value)",
				true, new Command() {

					@Override
					public void execute() {
						popPanel.hide();
						setState(INHERITED_STATE);
					}
				});

		popupMenuBar.addItem(item0);
		popupMenuBar.addItem(item1);
		popupMenuBar.addItem(item2);

		popPanel.add(popupMenuBar);

		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popPanel.setPopupPosition(getLeft(), getTop());
				popPanel.show();
				event.stopPropagation();
				event.preventDefault();
			}
		});
	}

	protected void setState(String tState) {
		state = tState;
		if (tState.equals(TRUE_STATE)) {
			setValue(true);
			item0
					.setHTML("<b>V</b>&nbsp;&nbsp;True: start-value is used to initialize");
			item1
					.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;False: start-value is only a guess-value");
			item2
					.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;Inherited: (False: start-value is only a guess-value)");
		} else if (tState.equals(FALSE_STATE)) {
			setValue(false);
			item0
					.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;True: start-value is used to initialize");
			item1
					.setHTML("<b>V</b>&nbsp;&nbsp;False: start-value is only a guess-value");
			item2
					.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;Inherited: (False: start-value is only a guess-value)");
		} else {
			setValue(false);
			item0
					.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;True: start-value is used to initialize");
			item1
					.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;False: start-value is only a guess-value");
			item2
					.setHTML("<b>V</b>&nbsp;&nbsp;Inherited: (False: start-value is only a guess-value)");
		}
	}

	protected void setMenuItemText() {
		item0.setHTML("True: start-value is used to initialize");
		item1.setHTML("False: start-value is only a guess-value");
		item2.setHTML(" Inherited: (False: start-value is only a guess-value)");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.CheckBox#setValue(java.lang.Boolean)
	 */
	@Override
	public void setValue(Boolean value) {
		super.setValue(value);
	}

	protected int getTop() {
		return getAbsoluteTop();
	}

	protected int getLeft() {
		return getAbsoluteLeft();
	}

	public String getInputValue() {
		return state;
	}

	@Override
	public void setInputValue(String value) {
		if (value != null) {
			if (value.equals(TRUE_STATE)) {
				setState(TRUE_STATE);
			} else if (value.equals(FALSE_STATE)) {
				setState(FALSE_STATE);
			} else {
				setState(INHERITED_STATE);
			}
		} else {
			setState(INHERITED_STATE);
		}
	}

}

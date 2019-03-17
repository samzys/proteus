/**
 * 
 */
package proteus.gwt.client.app.ui.util;

import proteus.gwt.client.app.util.HTMLGetter;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class FloatingStatus {

	static PopupPanel popupPanel = new PopupPanel(false);
	static HTML html = new HTML();
	static {
		popupPanel.add(html);
		popupPanel.setStyleName("floatingStatus");
		popupPanel.setPopupPosition(Window.getClientWidth() / 2, 0);
	}

	public static void showStatus(String text) {

		html.setHTML(HTMLGetter.getTextwithSize(text, "22", false));

		popupPanel.setPopupPosition(
				(Window.getClientWidth() - html.getOffsetWidth()) / 2, 0);
		popupPanel.show();
	}

	public static void showStatus() {
		showStatus("Loading...");
	}

	public static void hideStatus() {
		popupPanel.hide();
	}

	/*
	 * disappear after 'delay' milliseconds
	 */
	public static void showStatus(String text, int delay) {

		showStatus(text);
		Timer timer = new Timer() {
			public void run() {
				hideStatus();
			}
		};

		if (delay > 0) {
			timer.schedule(delay);
		}
	}

}

/**
 * 
 */
package proteus.gwt.client.app.ui.common;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class MessageDialog extends DialogBox {

	public MessageDialog(String title, String text) {
		super();
		
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel panel2 = new HorizontalPanel();
		
		PushButton closeButton = new PushButton("Close") {
			@Override
			public void onClick() {
				super.onClick();
				onConfirm();
			}
		};
		closeButton.getElement().getStyle().setProperty("marginLeft", "auto"); 
		closeButton.getElement().getStyle().setProperty("marginRight", "auto");
		
		panel2.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
		panel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel2.add(closeButton);

		panel.setCellHorizontalAlignment(panel2, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(new HTML(text));
		panel.add(panel2);
		
		setWidget(panel);
		setText(title);
		setModal(true);
	}
	
	protected void onConfirm() {
		hide();
	}
}

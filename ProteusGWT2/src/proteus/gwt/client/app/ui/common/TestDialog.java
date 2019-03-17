/**
 * 
 */
package proteus.gwt.client.app.ui.common;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public abstract class TestDialog extends DialogBox {

	 TextBox textBox = new TextBox();
	 
	public TestDialog(String title, String text) {
		super();
		
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel panel2 = new HorizontalPanel();
		
		PushButton confirmButton = new PushButton("Confirm") {
			@Override
			public void onClick() {
				super.onClick();
				onConfirm();
				TestDialog.this.hide();
			}
		};
		PushButton cancelButton = new PushButton("Cancel") {
			@Override
			public void onClick() {
				super.onClick();
				onCancel();
				TestDialog.this.hide();
			}
		};
		panel2.setCellHorizontalAlignment(confirmButton, HasHorizontalAlignment.ALIGN_CENTER);
		panel2.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_CENTER);
		panel2.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		panel2.add(confirmButton);
		panel2.add(cancelButton);

//		confirmButton.getElement().getStyle().setProperty("marginLeft", "auto"); 
//		confirmButton.getElement().getStyle().setProperty("marginRight", "auto");
//		cancelButton.getElement().getStyle().setProperty("marginLeft", "auto"); 
//		cancelButton.getElement().getStyle().setProperty("marginRight", "auto");

		panel.setCellHorizontalAlignment(panel2, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(new HTML(text));
		panel.add(textBox);
		panel.add(panel2);
		
		setWidget(panel);
		setText(title);
		setModal(true);
	}
	public String getText(){
		return textBox.getText();
	}
	protected abstract void onConfirm();
	protected abstract void onCancel();
}

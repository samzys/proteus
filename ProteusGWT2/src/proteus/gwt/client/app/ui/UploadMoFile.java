/**
 * 
 */
package proteus.gwt.client.app.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Gao Lei
 * 
 */
public class UploadMoFile extends SimplePanel {

	private static UploadMindMapViewUiBinder uiBinder = GWT
			.create(UploadMindMapViewUiBinder.class);

	interface UploadMindMapViewUiBinder extends UiBinder<Widget, UploadMoFile> {
	}

	public interface MessageStyle extends CssResource {
		String showWaitMessage();

		String showFailMessage();
	}

	public @UiField
	DialogBox box;
	public @UiField
	Label lbMessage;
	public @UiField
	FormPanel formPanel;
	public @UiField
	FileUpload fileUpload;
	public @UiField
	TextBox tbName;

	public @UiField
	TextArea taDescription;

	public @UiField
	Button bSubmit;
	public @UiField
	Button bClose;
	public @UiField
	MessageStyle messageStyle;

	private String uploadUrl;
	public UploadMoFile(String uploadUrl) {
		add(uiBinder.createAndBindUi(this));
		this.uploadUrl=uploadUrl;
	}

	@UiHandler("bSubmit")
	void onClickSubmit(ClickEvent e) {
		System.out.println("onClickSubmit in UploadMindMap");
		fileUpload.setName("file");
		tbName.setName("name");
		taDescription.setName("description");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		
		formPanel.setAction(uploadUrl);
		formPanel.setAction("uploadmofile");
		
		formPanel.submit();
	}
	

	@UiHandler("bClose")
	void onClickClose(ClickEvent e) {
		lbMessage.removeStyleName(messageStyle.showFailMessage());
		lbMessage.removeStyleName(messageStyle.showWaitMessage());

		box.hide();
	}
}

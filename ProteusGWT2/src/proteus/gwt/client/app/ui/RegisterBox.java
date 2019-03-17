package proteus.gwt.client.app.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class RegisterBox extends DialogBox 
	implements Command {
	private final Label lblError;
	private final FormPanel form;
	private final TextBox txtEmail;
	
	public RegisterBox() {
		this.addStyleName("dialog-box");
		this.setText("Register New User");
		//this.setAnimationEnabled(true);
		form = new FormPanel();
		form.setMethod(FormPanel.METHOD_POST);
    form.setAction("LoginServlet");
    
		FlowPanel dialogFPanel = new FlowPanel();
		dialogFPanel.setStyleName("login-form");
		form.setWidget(dialogFPanel);
		
		Label lblTitle = new Label("Email:");
		dialogFPanel.add(lblTitle);
		txtEmail = new TextBox();
		txtEmail.setName("email");
		dialogFPanel.add(txtEmail);
		
		lblTitle = new Label("Password:");
		dialogFPanel.add(lblTitle);
		final PasswordTextBox txtPassword = new PasswordTextBox();
		txtPassword.setName("password");
		dialogFPanel.add(txtPassword);

		lblTitle = new Label("Confirm password:");
		dialogFPanel.add(lblTitle);
		final PasswordTextBox txtPassword2 = new PasswordTextBox();
		txtPassword2.setName("password2");
		dialogFPanel.add(txtPassword2);
		
		lblError = new Label();
		lblError.addStyleName("login-error");
		lblError.setVisible(false);
		dialogFPanel.add(lblError);


		FlowPanel grpButton = new FlowPanel();
		// action trigger
		final Button cmdLogin = new Button("Register", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!txtPassword.getValue().equals(txtPassword2.getValue())) {
					setError("Password does not match!");
				} else if(!isValidEmail(txtEmail.getText())) {
					setError("Email address is not valid!");
				} else {
					form.submit();
				}
			}
		});
		grpButton.add(cmdLogin);
		
		// a close button to hide LoginBox
		final Button cmdClose = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RegisterBox.this.hide();
			}
		});
		cmdClose.addStyleName("login-close-button");
		grpButton.add(cmdClose);
		
		dialogFPanel.add(grpButton);
		
		form.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				lblError.setVisible(false);
			}
		});
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String result = event.getResults();
				result = result.substring(result.indexOf(">")+1, result.length()-6);
				if (result != null && result.isEmpty()) {
					Window.alert("Register successfully! Please login!");
				} else {
					Window.alert(result);
				}
				RegisterBox.this.hide();
			}
		});
		this.setWidget(form);
	}
	public void setError(String error) {
		lblError.setText(error);
		lblError.setVisible(true);
	}
	
	@Override
	public void execute() {
		this.center();
		txtEmail.setFocus(true);
	}
	
	public void addRegisterCompleteHandler(SubmitCompleteHandler registerCompleteHandler) {
		form.addSubmitCompleteHandler(registerCompleteHandler);
	}
	
	//FIXME not place something like this here, please
	private native boolean isValidEmail(String email) /*-{
	  var reg1 = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; // not valid
	  var reg2 = /^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/; // valid
	  return !reg1.test(email) && reg2.test(email);
	}-*/; 
}

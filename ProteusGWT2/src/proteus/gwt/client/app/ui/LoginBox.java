package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.LoginEvent;
import proteus.gwt.server.security.Cryptography;
import proteus.gwt.shared.types.Constants;
import proteus.gwt.shared.util.UserInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class LoginBox extends DialogBox implements Command {
	private final Label lblError;
//	 private final FormPanel form;
	private final TextBox txtUserl;
	private final PasswordTextBox txtPassword;

	public LoginBox() {
		this.addStyleName("dialog-box");
		this.setText("Sign In");
		 this.setAnimationEnabled(true);
		  
		FlowPanel dialogFPanel = new FlowPanel();
		dialogFPanel.setStyleName("login-form");

		Label lblTitle = new Label("UserName:");
		dialogFPanel.add(lblTitle);
		txtUserl = new TextBox();
		txtUserl.setName("username");
		dialogFPanel.add(txtUserl);

		lblTitle = new Label("Password:");
		dialogFPanel.add(lblTitle);
		txtPassword = new PasswordTextBox();
		txtPassword.setName("password");
		dialogFPanel.add(txtPassword);

		lblError = new Label();
		lblError.addStyleName("login-error");
		lblError.setVisible(false);
		dialogFPanel.add(lblError);

		FlowPanel grpButton = new FlowPanel();
		// action trigger
		final Button cmdLogin = new Button("Login", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String user = txtUserl.getText();
				String pwd = txtPassword.getText();
				if(user==null||pwd ==null||user.length()==0||pwd.length()==0) {
					setError("Username and Password can not be empty!" );
				}else {
					StringBuilder sb = new StringBuilder();
					sb.append(Constants.HEADER_USER + user+ "\n");
					sb.append(Constants.HEADER_PASS+pwd+"\n");
					ProteusGWT.eventBus.fireEvent(new LoginEvent(sb.toString()));
				}
			}
		});
		grpButton.add(cmdLogin);

		// a close button to hide LoginBox
		final Button cmdClose = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoginBox.this.hide();
			}
		});

		cmdClose.addStyleName("login-close-button");
		grpButton.add(cmdClose);

		dialogFPanel.add(grpButton);

		this.setWidget(dialogFPanel);
	}

	public void setError(String error) {
		lblError.setText(error);
		lblError.setVisible(true);
	}

	@Override
	public void execute() {
		this.center();
		txtUserl.setFocus(true);
	}

	public void addLoginCompleteHandler(
			SubmitCompleteHandler loginCompleteHandler) {
		// form.addSubmitCompleteHandler(loginCompleteHandler);
	}
}

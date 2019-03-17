package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.LoginEvent;
import proteus.gwt.client.app.event.LogoutEvent;
import proteus.gwt.shared.types.Constants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class LogoutBox  extends DialogBox implements Command{

	public LogoutBox() {
		this.addStyleName("dialog-box");
		this.setText("Sign out");
		 this.setAnimationEnabled(true);
			FlowPanel dialogFPanel = new FlowPanel();
			dialogFPanel.setStyleName("login-form");

			Label lblTitle = new Label("Are  you really want to log out?");
			dialogFPanel.add(lblTitle);
			FlowPanel grpButton = new FlowPanel();
			// action trigger
			final Button cmdLogin = new Button("Yes", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ProteusGWT.eventBus.fireEvent(new LogoutEvent());
				}
			});
			grpButton.add(cmdLogin);

			// a close button to hide LoginBox
			final Button cmdClose = new Button("Cancel", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					LogoutBox.this.hide();
				}
			});

			cmdClose.addStyleName("login-close-button");
			grpButton.add(cmdClose);

			dialogFPanel.add(grpButton);

			this.setWidget(dialogFPanel);

	}
	@Override
	public void execute() {
		this.center();
	}

}

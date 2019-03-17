package proteus.gwt.client.app.ui;

//import net.visualphysics.cycad.client.Registry;
//import net.visualphysics.cycad.client.entity.User;
//import net.visualphysics.cycad.client.remote.MessageFactory;
//import net.visualphysics.cycad.client.remote.UserRemote;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class ProfileMenu extends MenuItem
	implements SubmitCompleteHandler {
	
	private LoginBox loginBox;
	private RegisterBox registerBox;
	private MenuBar anonymousTool;
	private MenuBar userTool;
//	
	public ProfileMenu() {
		super("Anonymous", new MenuBar());
		loginBox = new LoginBox();
		registerBox = new RegisterBox();
		loginBox.addLoginCompleteHandler(this);
//		setAnonymousProfile();
	}
//
//	public void setProfile(UserRemote ui) {
//		Registry.registerObject("UserProfile", ui);
//		if (ui == null) {
//			setAnonymousProfile();
//		} else {
//			this.setText(ui.getName());
//			this.setSubMenu(getUserTool());
//		}
//	}
//	
//	private void setAnonymousProfile() {
//		this.setText("Anonymous");
//		this.setSubMenu(getAnonymousTool());
//	}
//
//	private MenuBar getUserTool() {
//		if (userTool == null)
//			setupUserTool();
//		return userTool;
//	}
//
//	private MenuBar getAnonymousTool() {
//		if (anonymousTool == null)
//			setupAnonymousTool();
//		return anonymousTool;
//	}
//	
//	private void setupUserTool() {
//		userTool = new MenuBar(true);
//		userTool.addItem("Logout", new Command() {
//			@Override
//			public void execute() {
//				ProfileMenu.this.setProfile(null);
//			}
//		});
//	}
//
//	private void setupAnonymousTool() {
//		anonymousTool = new MenuBar(true);
//		anonymousTool.addItem("Login", loginBox);
//		anonymousTool.addItem("Register", registerBox);
//	}
//
	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		String result = event.getResults();
		try {
//			User ui = MessageFactory.create(result);
//			this.setProfile(ui);
//			loginBox.hide();
		} catch (Exception e) {
//			loginBox.setError("Incorrect email or password");
		}
	}
}

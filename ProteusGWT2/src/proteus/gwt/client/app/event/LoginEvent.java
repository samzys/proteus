package proteus.gwt.client.app.event;

import proteus.gwt.shared.util.UserInfo;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();

	private String info;

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	public LoginEvent(String string) {
		this.info = string;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}

	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

}

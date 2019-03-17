package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class NewModuleEvent extends GwtEvent<NewModuleEventHandler> {
	public static Type<NewModuleEventHandler> TYPE = new Type<NewModuleEventHandler>();

	private String type;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public NewModuleEvent(String type) {
		this.type = type;
	}

	@Override
	public Type<NewModuleEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewModuleEventHandler handler) {
		handler.onNewModule(this);
	}
}

package proteus.gwt.client.app.event;

import proteus.gwt.shared.modelica.Component;

import com.google.gwt.event.shared.GwtEvent;

public class OpenComponentEvent extends GwtEvent<OpenComponentEventHandler> {
	public static Type<OpenComponentEventHandler> TYPE = new Type<OpenComponentEventHandler>();

	private Component comp;

	public OpenComponentEvent(Component component) {
		this.comp = component;
	}

	/**
	 * @return the comp
	 */
	public Component getComp() {
		return comp;
	}

	/**
	 * @param comp
	 *            the comp to set
	 */
	public void setComp(Component comp) {
		this.comp = comp;
	}

	@Override
	protected void dispatch(OpenComponentEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onOpenComponent(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OpenComponentEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

}

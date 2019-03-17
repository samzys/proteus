package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowDemoEvent extends GwtEvent<ShowDemoEventHandler> {
	public static Type<ShowDemoEventHandler> TYPE = new Type<ShowDemoEventHandler>();

	private String name;

	public ShowDemoEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Type<ShowDemoEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowDemoEventHandler handler) {
		handler.onShowDemo(this);
	}
}

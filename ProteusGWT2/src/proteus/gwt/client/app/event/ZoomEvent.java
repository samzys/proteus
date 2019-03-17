package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoomEvent extends GwtEvent<ZoomEventHandler> {
	public static Type<ZoomEventHandler> TYPE = new Type<ZoomEventHandler>();

	@Override
	public Type<ZoomEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ZoomEventHandler handler) {
		handler.onZoom(this);
	}
}
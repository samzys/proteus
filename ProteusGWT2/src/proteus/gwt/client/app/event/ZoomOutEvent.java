package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoomOutEvent extends GwtEvent<ZoomOutEventHandler> {
	public static Type<ZoomOutEventHandler> TYPE = new Type<ZoomOutEventHandler>();

	@Override
	public Type<ZoomOutEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ZoomOutEventHandler handler) {
		handler.onZoomOut(this);
	}
}
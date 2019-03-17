package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoomInEvent extends GwtEvent<ZoomInEventHandler> {
	public static Type<ZoomInEventHandler> TYPE = new Type<ZoomInEventHandler>();

	@Override
	public Type<ZoomInEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ZoomInEventHandler handler) {
		handler.onZoomIn(this);
	}
}
package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoomResetEvent extends GwtEvent<ZoomResetEventHandler> {
	public static Type<ZoomResetEventHandler> TYPE = new Type<ZoomResetEventHandler>();

	@Override
	public Type<ZoomResetEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ZoomResetEventHandler handler) {
		handler.onZoomReset(this);
	}
}
package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class RepaintCanvasEvent extends
GwtEvent<RepaintCanvasEventHandler> {

	public static Type<RepaintCanvasEventHandler> TYPE = new Type<RepaintCanvasEventHandler>();
	@Override
	protected void dispatch(RepaintCanvasEventHandler handler) {
		handler.onRepaint(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RepaintCanvasEventHandler> getAssociatedType() {
		return TYPE;
	}

}

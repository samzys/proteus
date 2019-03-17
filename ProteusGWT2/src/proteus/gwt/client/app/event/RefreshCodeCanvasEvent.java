package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class RefreshCodeCanvasEvent extends
		GwtEvent<RefreshCodeCanvasEventHandler> {
	public static Type<RefreshCodeCanvasEventHandler> TYPE = new Type<RefreshCodeCanvasEventHandler>();

	@Override
	protected void dispatch(RefreshCodeCanvasEventHandler handler) {
		handler.onRefreshCode(this);
	}

	@Override
	public Type<RefreshCodeCanvasEventHandler> getAssociatedType() {
		return TYPE;
	}

}

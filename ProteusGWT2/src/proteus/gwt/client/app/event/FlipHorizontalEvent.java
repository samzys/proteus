package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class FlipHorizontalEvent extends GwtEvent<FlipHorizontalEventHandler> {
	public static Type<FlipHorizontalEventHandler> TYPE = new Type<FlipHorizontalEventHandler>();

	@Override
	public Type<FlipHorizontalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FlipHorizontalEventHandler handler) {
		handler.onFlipHorizontal(this);
	}
}
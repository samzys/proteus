package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class FlipVerticalEvent extends GwtEvent<FlipVerticalEventHandler> {
	public static Type<FlipVerticalEventHandler> TYPE = new Type<FlipVerticalEventHandler>();

	@Override
	public Type<FlipVerticalEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FlipVerticalEventHandler handler) {
		handler.onFlipVertical(this);
	}
}
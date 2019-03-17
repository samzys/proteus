package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class RotateClockwiseEvent extends GwtEvent<RotateClockwiseEventHandler> {
	public static Type<RotateClockwiseEventHandler> TYPE = new Type<RotateClockwiseEventHandler>();

	@Override
	public Type<RotateClockwiseEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RotateClockwiseEventHandler handler) {
		handler.onRotateClockwise(this);
	}
}

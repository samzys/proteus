package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class RotateAntiClockwiseEvent extends GwtEvent<RotateAntiClockwiseEventHandler> {
	public static Type<RotateAntiClockwiseEventHandler> TYPE = new Type<RotateAntiClockwiseEventHandler>();

	@Override
	public Type<RotateAntiClockwiseEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RotateAntiClockwiseEventHandler handler) {
		handler.onRotateAntiClockwise(this);
	}
}
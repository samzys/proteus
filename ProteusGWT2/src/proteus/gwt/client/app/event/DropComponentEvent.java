package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class DropComponentEvent extends GwtEvent<DropComponentEventHandler> {
	public static Type<DropComponentEventHandler> TYPE = new Type<DropComponentEventHandler>();

	private int x;
	private int y;

	public DropComponentEvent(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public Type<DropComponentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DropComponentEventHandler handler) {
		handler.onDropComponent(this);
	}
}

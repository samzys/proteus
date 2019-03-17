package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class DragComponentEvent extends GwtEvent<DragComponentEventHandler> {
	public static Type<DragComponentEventHandler> TYPE = new Type<DragComponentEventHandler>();

	private String name;

	public DragComponentEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Type<DragComponentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DragComponentEventHandler handler) {
		handler.onDragComponent(this);
	}
}

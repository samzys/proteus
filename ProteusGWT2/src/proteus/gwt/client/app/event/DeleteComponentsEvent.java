package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class DeleteComponentsEvent extends
		GwtEvent<DeleteComponentsEventHandler> {
	public static Type<DeleteComponentsEventHandler> TYPE = new Type<DeleteComponentsEventHandler>();

	@Override
	public Type<DeleteComponentsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DeleteComponentsEventHandler handler) {
		handler.onDeleteComponents(this);
	}
}
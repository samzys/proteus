package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author leiting
 * Created Jun 19, 2011
 */
public class UndoEvent extends
		GwtEvent<UndoEventHandler> {
	
	public static Type<UndoEventHandler> TYPE = new Type<UndoEventHandler>();

	public UndoEvent() {
		super();

	}

	@Override
	public Type<UndoEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UndoEventHandler handler) {
		handler.onUndo(this);
	}
}

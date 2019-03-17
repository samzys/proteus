package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author leiting
 * Created Jun 19, 2011
 */
public class SaveEvent extends
		GwtEvent<SaveEventHandler> {
	
	public static Type<SaveEventHandler> TYPE = new Type<SaveEventHandler>();

	public SaveEvent() {
		super();

	}

	@Override
	public Type<SaveEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveEventHandler handler) {
		handler.onSave(this);
	}
}

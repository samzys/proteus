package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author leiting
 * Created Jun 19, 2011
 */
public class RedoEvent extends
		GwtEvent<RedoEventHandler> {
	
	public static Type<RedoEventHandler> TYPE = new Type<RedoEventHandler>();

	public RedoEvent() {
		super();

	}

	@Override
	public Type<RedoEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RedoEventHandler handler) {
		handler.onRedo(this);
	}
}

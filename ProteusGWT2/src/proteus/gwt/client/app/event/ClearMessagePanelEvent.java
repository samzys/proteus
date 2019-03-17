package proteus.gwt.client.app.event;

import proteus.gwt.client.app.ui.MessagePanel.Message;

import com.google.gwt.event.shared.GwtEvent;

public class ClearMessagePanelEvent extends
		GwtEvent<ClearMessagePanelEventHandler> {
	public static Type<ClearMessagePanelEventHandler> TYPE = new Type<ClearMessagePanelEventHandler>();

	private Message message;

	public ClearMessagePanelEvent(Message message) {
		super();
		this.message = message;

	}

	public Message getMessage() {
		return message;
	}

	@Override
	public Type<ClearMessagePanelEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ClearMessagePanelEventHandler handler) {
		handler.onClearMessagePanel(this);
	}
}

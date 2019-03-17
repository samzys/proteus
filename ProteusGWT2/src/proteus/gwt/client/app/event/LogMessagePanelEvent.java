package proteus.gwt.client.app.event;

import proteus.gwt.client.app.ui.MessagePanel.Message;

import com.google.gwt.event.shared.GwtEvent;

public class LogMessagePanelEvent extends GwtEvent<LogMessagePanelEventHandler> {
	public static Type<LogMessagePanelEventHandler> TYPE = new Type<LogMessagePanelEventHandler>();

	private Message message;
	private String html;

	
	public LogMessagePanelEvent(Message message, String html) {
		super();
		this.message = message;
		this.html = html;
	}

	public Message getMessage() {
		return message;
	}

	public String getHtml() {
		return html;
	}

	@Override
	public Type<LogMessagePanelEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogMessagePanelEventHandler handler) {
		handler.onLogMessagePanel(this);
	}
}

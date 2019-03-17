package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author gao lei
 * 
 */
public class LogDocumentCanvasEvent extends
		GwtEvent<LogDocumentCanvasEventHandler> {
	public static Type<LogDocumentCanvasEventHandler> TYPE = new Type<LogDocumentCanvasEventHandler>();

	private String html;

	public LogDocumentCanvasEvent(String html) {
		super();

		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	@Override
	public Type<LogDocumentCanvasEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogDocumentCanvasEventHandler handler) {
		handler.onLogDocumentCanvas(this);
	}
}

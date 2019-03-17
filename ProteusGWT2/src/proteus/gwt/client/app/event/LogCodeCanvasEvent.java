package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author gao lei
 * 
 */
public class LogCodeCanvasEvent extends GwtEvent<LogCodeCanvasEventHandler> {
	public static Type<LogCodeCanvasEventHandler> TYPE = new Type<LogCodeCanvasEventHandler>();

	private String html;

	public LogCodeCanvasEvent(String html) {
		super();

		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	@Override
	public Type<LogCodeCanvasEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogCodeCanvasEventHandler handler) {
		handler.onLogCodeCanvas(this);
	}
}

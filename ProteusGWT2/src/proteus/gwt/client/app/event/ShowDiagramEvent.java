package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowDiagramEvent extends GwtEvent<ShowDiagramEventHandler> {
	public static Type<ShowDiagramEventHandler> TYPE = new Type<ShowDiagramEventHandler>();

	private String name;

	public ShowDiagramEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Type<ShowDiagramEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowDiagramEventHandler handler) {
		handler.onShowDiaram(this);
	}
}

package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;

public class IcongraphicDTO extends JavaScriptObject {
	protected IcongraphicDTO() {
	}

	public final native String getGraphics()/*-{
		return this.graphics;
	}-*/;

	public final native String getExtent()/*-{
		return this.extent;
	}-*/;

	public final native String getPreserveAspectRatio()/*-{
		return this.preserveAspectRatio;
	}-*/;

	public final native String getGrid() /*-{
		return this.grid;
	}-*/;
}

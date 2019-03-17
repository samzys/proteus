package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class DiagramgraphicDTO extends JavaScriptObject {
	protected DiagramgraphicDTO() {
	}

	public final native JsArrayString getGraphicList() /*-{
		return this.graphicList;
	}-*/;

	public final native String getExtent() /*-{
		return this.extent;
	}-*/;

	public final native String getPreserveAspectRatio() /*-{
		return this.preserveAspectRatio;
	}-*/;

	public final native String getGrid() /*-{
		return this.grid;
	}-*/;

}

package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;

public class ConnectDTO extends JavaScriptObject {
	protected ConnectDTO() {
	};

	public final native String getValue() /*-{
		return this.value;
	}-*/;

	public final native String getStartpin() /*-{
		return this.startpin;
	}-*/;

	public final native String getEndpin() /*-{
		return this.endpin;
	}-*/;
}

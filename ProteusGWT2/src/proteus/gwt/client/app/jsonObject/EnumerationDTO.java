package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;

public class EnumerationDTO extends JavaScriptObject {
	
	protected EnumerationDTO() {
	}
	
	public final native String getName() /*-{
		return this.name;
	}-*/;
	
	public final native String getComment()  /*-{
		return this.comment;
	}-*/;
}

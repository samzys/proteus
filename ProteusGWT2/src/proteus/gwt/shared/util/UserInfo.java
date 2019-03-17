package proteus.gwt.shared.util;

import com.google.gwt.core.client.JavaScriptObject;

public class UserInfo extends JavaScriptObject {

	protected UserInfo() {
	}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getPwd() /*-{
		return this.pwd;
	}-*/;

	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	public final native void setPwd(String pwd) /*-{
		this.pwd = pwd;
	}-*/;
}

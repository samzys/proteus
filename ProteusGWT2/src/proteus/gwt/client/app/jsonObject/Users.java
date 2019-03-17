package proteus.gwt.client.app.jsonObject;


import com.google.gwt.core.client.JavaScriptObject;

public class Users extends JavaScriptObject {

	protected Users() {
	}

	public final native String getName() /*-{
		return this.name;
	}-*/;

//	@Override
	public final native int getUid() /*-{
		return this.uid;
	}-*/;

	public final native void setUid(int uid) /*-{
		this.uid = uid;
	}-*/;

	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	public final native String getPass() /*-{
		return this.pass;
	}-*/;

	public final native void setPass(String pass) /*-{
		this.pass = pass;
	}-*/;

	public final native String toJSON() /*-{
		return this.toString();
	}-*/;
}

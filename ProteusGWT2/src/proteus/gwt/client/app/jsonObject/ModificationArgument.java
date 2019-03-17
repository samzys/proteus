package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;

public class ModificationArgument  extends JavaScriptObject{
	protected ModificationArgument() {
	};

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getValue() /*-{
		return this.value;
	}-*/;

	public final native String getStart() /*-{
		return this.start;
	}-*/;

	public final native String getFixed() /*-{
		return this.fixed;
	}-*/;

	public final native String getMin() /*-{
		return this.min;
	}-*/;

	public final native String getMax() /*-{
		return this.max;
	}-*/;

	public final native String getUnit() /*-{
		return this.unit;
	}-*/;

	public final native String getDisplayUnit() /*-{
		return this.displayUnit;
	}-*/;

	public final native String getQuantity() /*-{
		return this.quantity;
	}-*/;

	public final native String getNominal() /*-{
		return this.nominal;
	}-*/;

	public final native String getStateSelect() /*-{
		return this.stateSelect;
	}-*/;
}

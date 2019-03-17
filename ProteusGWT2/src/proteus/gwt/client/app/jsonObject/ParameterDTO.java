package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class ParameterDTO extends JavaScriptObject {
	protected ParameterDTO() {
	}

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getValue() /*-{
		return this.value;
	}-*/;

	public final native boolean isInherited() /*-{
		return this.inherited;
	}-*/;

	public final native String getUnit() /*-{
		return this.unit;
	}-*/;

	public final native String getDesc() /*-{
		return this.desc;
	}-*/;
	public final native String getQuantity() /*-{
		return this.quantity;
	}-*/;

	public final native boolean isHeader() /*-{
		return this.header | false;
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

	public final native String getMax()/*-{
		return this.max;
	}-*/;

	public final native String getDisplayUnit() /*-{
		return this.displayUnit;
	}-*/;

	public final native String getNominal() /*-{
		return this.nominal;
	}-*/;

	public final native String getStateSelect() /*-{
		return this.stateSelect;
	}-*/;

	public final native String getType() /*-{
		return this.type;
	}-*/;
	
	public final native JsArray<EnumerationDTO> getEnumList() /*-{
		return this.enumList;
	}-*/;
	
	public  final native  String getAnnotationGroup() /*-{
		return this.annotationGroup;
	}-*/;
	
	public final native  String getComment() /*-{
		return this.comment;
	}-*/;
	
	public final native  String getPath() /*-{
		return this.path;
	}-*/;
	
	public final native String getAnnotationTab() /*-{
		return this.annotationTab;
	}-*/;
	
	public final native String getAnnotationInitDialog()/*-{
		return this.annotationInitDialog;
	}-*/;
	
	public final native String getVariability() /*-{
		return this.variability;
	}-*/;
	public final native String getTyping() /*-{
		return this.typing;
	}-*/;
	public final native String getCausality() /*-{
		return this.causality;
	}-*/;
	public final native String getWholename() /*-{
		return this.wholename;
	}-*/;
}

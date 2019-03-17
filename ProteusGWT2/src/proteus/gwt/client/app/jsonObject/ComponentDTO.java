package proteus.gwt.client.app.jsonObject;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

/**
 * @author gao lei
 * 
 */
public class ComponentDTO extends JavaScriptObject {

	protected ComponentDTO() {
	}

	public final native String getID() /*-{
		return this.ID;
	}-*/;

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getWholeName() /*-{
		return this.wholeName;
	}-*/;

	public final native String getExtent() /*-{
		return this.extent;
	}-*/;

	public final native String getOrigin() /*-{
		return this.origin;
	}-*/;

	public final native String getRotation() /*-{
		return this.rotation;
	}-*/;

	public final native String getRestriction() /*-{
		return this.restriction;
	}-*/;

	public final native String getDeclarationName() /*-{
		return this.declarationName;
	}-*/;

	public final native String getProtected_()/*-{
		return this.protected_;
	}-*/;

	public final native String getFinal_()/*-{
		return this.final_;
	}-*/;

	public final native String getVariability()/*-{
		return this.variability;
	}-*/;

	public final native String getTyping()/*-{
		return this.typing;
	}-*/;

	public final native String getCausality()/*-{
		return this.causality;
	}-*/;

	public final native JsArrayString getArrayFormList() /*-{
		return this.arrayFormList;
	}-*/;

	public final native JsArray<IcongraphicDTO> getIconGraphicList() /*-{
		return this.iconGraphicList;
	}-*/;

	public final native DiagramgraphicDTO getDiagramGraphic()/*-{
		return this.diagramGraphic;
	}-*/;

	public final native JsArray<ParameterDTO> getParameters() /*-{
		return this.parameters;
	}-*/;

	public final native JsArray<ConnectDTO> getConnects() /*-{
		return this.connects;
	}-*/;

	public final native JsArray<ComponentDTO> getIncludeComps() /*-{
		return this.includeComps;
	}-*/;

	public final native JsArray<ModificationArgument> getSuperIncludeArgList() /*-{
		return this.superIncludeArgList;
	}-*/;

	public final native JsArray<ComponentDTO> getExtendComps() /*-{
		return this.extendComps;
	}-*/;

	public final native String getComment() /*-{
		return this.comment;
	}-*/;

}

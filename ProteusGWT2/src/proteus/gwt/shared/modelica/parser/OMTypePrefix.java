/* Generated By:JJTree: Do not edit this line. OMTypePrefix.java */

package proteus.gwt.shared.modelica.parser;




public class OMTypePrefix extends SimpleNode {

//	@BooleanType(name = "Flow", category = CATEGORY_CODE)
	public String flow_stream;

//	@EnumType(name = "Variability", allowed = { "discrete", "parameter",
//			"constant" }, category = CATEGORY_CODE)
	public String variability;

//	@EnumType(name = "Causality", allowed = { "input", "output" }, category = CATEGORY_CODE)
	public String causality;

	public OMTypePrefix(int id) {
		super(id);
	}

	public OMTypePrefix(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toCode() {
		StringBuilder sb = new StringBuilder();
		if (flow_stream!=null) {
			sb.append(flow_stream+" ");
		}
		if (variability != null) {
			sb.append(variability + " ");
		}
		if (causality != null) {
			sb.append(causality + " ");
		}
		return sb.toString();
	}
}

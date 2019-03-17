/* Generated By:JJTree: Do not edit this line. OMFunctionCallArgs.java */

package proteus.gwt.shared.modelica.parser;

public class OMFunctionCallArgs extends SimpleNode {

	public OMFunctionArguments funcArgs;

	public OMFunctionCallArgs(int id) {
		super(id);
	}

	public OMFunctionCallArgs(ModelicaParser p, int id) {
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
		sb.append("(");
		if (funcArgs != null) {
			sb.append(funcArgs.toCode());
		}
		sb.append(")");
		return sb.toString();
	}
}

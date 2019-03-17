/* Generated By:JJTree: Do not edit this line. OMCondAttr.java */

package proteus.gwt.shared.modelica.parser;


public class OMCondAttr extends SimpleNode {

	
	public OMExpression expression;

	public OMCondAttr(int id) {
		super(id);
	}

	public OMCondAttr(ModelicaParser p, int id) {
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
		sb.append("if " + expression.toCode());
		return sb.toString();
	}
	//	
	// public OMCondAttr copy() {
	// OMCondAttr copy = new OMCondAttr(parser, id);
	// copy.expression = expression.copy();
	// return copy;
	// }
}
/* Generated By:JJTree: Do not edit this line. OMForIndex.java */

package com.infoscient.proteus.modelica.parser;

public class OMForIndex extends SimpleNode {

	public String name;

	public OMExpression inExpression;

	public OMForIndex(int id) {
		super(id);
	}

	public OMForIndex(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if (inExpression != null) {
			sb.append(" in " + inExpression.toCode());
		}
		return sb.toString();
	}
}

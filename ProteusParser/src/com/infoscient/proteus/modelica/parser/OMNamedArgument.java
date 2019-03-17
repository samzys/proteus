/* Generated By:JJTree: Do not edit this line. OMNamedArgument.java */

package com.infoscient.proteus.modelica.parser;

public class OMNamedArgument extends SimpleNode {

	public String name;

	public OMExpression expression;

	public OMNamedArgument(int id) {
		super(id);
	}

	public OMNamedArgument(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + " = " + expression.toCode());
		return sb.toString();
	}
}

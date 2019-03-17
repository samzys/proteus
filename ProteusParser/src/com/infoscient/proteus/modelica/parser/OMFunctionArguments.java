/* Generated By:JJTree: Do not edit this line. OMFunctionArguments.java */

package com.infoscient.proteus.modelica.parser;

public class OMFunctionArguments extends SimpleNode {

	public OMExpression expression;

	public OMFunctionArguments next;

	public OMForIndices forIndices;

	public OMNamedArgument[] namedArguments;

	public OMFunctionArguments(int id) {
		super(id);
	}

	public OMFunctionArguments(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		if (expression != null) {
			sb.append(expression.toCode());
			if (next != null) {
				sb.append(", " + next.toCode());
			} else if (forIndices != null) {
				sb.append("for " + forIndices.toCode());
			}
		} else if (namedArguments != null) {
			int i = 0;
			for (OMNamedArgument na : namedArguments) {
				if (i++ > 0) {
					sb.append(", ");
				}
				sb.append(na.toCode());
			}
		}
		return sb.toString();
	}
}

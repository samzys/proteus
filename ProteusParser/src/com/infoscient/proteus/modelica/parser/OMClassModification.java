/* Generated By:JJTree: Do not edit this line. OMClassModification.java */

package com.infoscient.proteus.modelica.parser;

public class OMClassModification extends SimpleNode {

	public OMArgumentList argList;

	public OMClassModification(int id) {
		super(id);
	}

	public OMClassModification(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (argList != null) {
			sb.append(argList.toCode());
		}
		sb.append(")");
		return sb.toString();
	}
	//	
	// public OMClassModification copy() {
	// OMClassModification copy = new OMClassModification(parser, id);
	// if (argList != null) {
	// copy.argList = argList.copy();
	// }
	// return copy;
	// }
}

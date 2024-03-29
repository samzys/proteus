/* Generated By:JJTree: Do not edit this line. OMLogicalTerm.java */

package com.infoscient.proteus.modelica.parser;

import java.util.LinkedList;
import java.util.List;

public class OMLogicalTerm extends SimpleNode {

	public List<OMLogicalFactor> logicalFactors = new LinkedList<OMLogicalFactor>();

	public OMLogicalTerm(int id) {
		super(id);
	}

	public OMLogicalTerm(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (OMLogicalFactor factor : logicalFactors) {
			if (i++ > 0) {
				sb.append(" and ");
			}
			sb.append(factor.toCode());
		}
		return sb.toString();
	}
}

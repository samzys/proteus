/* Generated By:JJTree: Do not edit this line. OMElementReplaceable.java */

package com.infoscient.proteus.modelica.parser;

public class OMElementReplaceable extends SimpleNode {

	public OMClassDefinition classDefinition;

	public OMComponentClause1 componentClause1;

	public OMConstrainingClause constrainingClause;

	public OMElementReplaceable(int id) {
		super(id);
	}

	public OMElementReplaceable(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
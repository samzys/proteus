/* Generated By:JJTree: Do not edit this line. OMComment.java */

package com.infoscient.proteus.modelica.parser;

import com.infoscient.proteus.types.StringType;

public class OMComment extends SimpleNode {

	@StringType(name = "String", category = CATEGORY_CODE, description = "Comment")
	public String string;

	public OMAnnotation annotation;

	public OMComment(int id) {
		super(id);
	}

	public OMComment(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		if (string.length() > 0) {
			sb.append(" \"" + string + "\"");
		}
		if (annotation != null) {
			sb.append(annotation.toCode());
		}
		return sb.toString();
	}
	//	
	// public OMComment copy() {
	// OMComment copy = new OMComment(parser, id);
	// copy.string = string;
	// copy.annotation = annotation.copy();
	// return copy;
	// }
}

/* Generated By:JJTree: Do not edit this line. OMElement.java */

package com.infoscient.proteus.modelica.parser;

import com.infoscient.proteus.types.BooleanType;
import com.infoscient.proteus.types.EnumType;

public class OMElement extends SimpleNode {

	public OMImportClause importClause;

	public OMExtendsClause extendsClause;
	
	@EnumType(name = "Protected_", allowed = {"public",
	"protected" }, category = CATEGORY_CODE)
	public String protected_;

	@BooleanType(name = "Redeclare", category = CATEGORY_CODE)
	public boolean redeclare;

	@BooleanType(name = "Final", category = CATEGORY_CODE)
	public boolean final_;

	@EnumType(name = "Typing", allowed = {"inner", "outer"}, category = CATEGORY_CODE)
	public String typing;
	
//	@BooleanType(name = "Inner", category = CATEGORY_CODE)
//	public boolean inner;
//
//	@BooleanType(name = "Outer", category = CATEGORY_CODE)
//	public boolean outer;

	@BooleanType(name = "Replaceable", category = CATEGORY_CODE)
	public boolean replaceable;

	public OMClassDefinition classDefinition;

	public OMComponentClause componentClause;

	public OMConstrainingClause constrainingClause;

	public OMComment comment;

	public OMElement(int id) {
		super(id);
	}

	public OMElement(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		if (importClause != null) {
			sb.append(importClause.toCode());
		} else if (extendsClause != null) {
			sb.append(extendsClause.toCode());
		} else {
			if (final_) {
				sb.append("final ");
			}
			if (typing!=null) {
				sb.append(typing+" ");
			}
			if (classDefinition != null) {
				sb.append(classDefinition.toCode());
			} else if (componentClause != null) {
				sb.append(componentClause.toCode());
			}
		}
		return sb.toString();
	}
}
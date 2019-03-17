package com.infoscient.proteus.modelica.calculator;

import java.util.LinkedList;
import java.util.List;

public class _Term extends SimpleNode {
	public double val;

	public List<_Primary> primaryList = new LinkedList<_Primary>();
	public _Term(int id) {
		super(id);
	}

	public _Term(Calculator p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(CalculatorVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
	public String toCode() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}


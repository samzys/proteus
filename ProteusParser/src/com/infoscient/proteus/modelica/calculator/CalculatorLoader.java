package com.infoscient.proteus.modelica.calculator;


public class CalculatorLoader implements CalculatorVisitor, CalculatorConstants{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public Object visit(_Start node, Object data) {
		//two levels for now...
		for (int i = 0; i < node.eList.size(); i++) {
			_Expressions exps = node.eList.get(i);
			exps.jjtAccept(this, data);
			node.arraySubList.add(exps.valueList.size());
			for (int j = 0; j < exps.valueList.size(); j++) {
				System.out.print(exps.valueList.get(j).val+" ");
				node.doubleList.add(exps.valueList.get(j).val);
			}
			System.out.println();
		}
		return null;
	}

	public Object visit(_Expressions node, Object data) {
		// TODO Auto-generated method stub
		for(_Expression exp: node.valueList){
			exp.jjtAccept(this, data);
		}
		return null;
	}

	public Object visit(_Expression node, Object data) {
		// TODO Auto-generated method stub
		for(_Term term: node.termList){
		term.jjtAccept(this, data);	
		}
		return null;
	}

	public Object visit(_Term node, Object data) {
		// TODO Auto-generated method stub
		for(_Primary p: node.primaryList){
			p.jjtAccept(this, data);
		}
		return null;
	}


	public Object visit(_Primary node, Object data) {
		
		return null;
	}

	public Object visit(_Variables node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(_Functions node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}


	}

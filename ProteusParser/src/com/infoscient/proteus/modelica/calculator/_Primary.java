/* Generated By:JJTree: Do not edit this line. _Primary.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=_,NODE_EXTENDS=,NODE_FACTORY= */
package com.infoscient.proteus.modelica.calculator;


public class _Primary extends SimpleNode {
	public double val;
	
  public _Primary(int id) {
    super(id);
  }

  public _Primary(Calculator p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CalculatorVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8162a31bcff9a9a9cb215e151014820c (do not edit this line) */

/* Generated By:JJTree: Do not edit this line. OMComposition.java */

package proteus.gwt.shared.modelica.parser;

import java.util.LinkedList;
import java.util.List;


public class OMComposition extends SimpleNode {

	public OMElementList elementList;

	public List<OMElementList> publicElementLists = new LinkedList<OMElementList>();

	public List<OMElementList> protectedElementLists = new LinkedList<OMElementList>();

	public List<OMEquationSection> equationSections = new LinkedList<OMEquationSection>();

	public List<OMAlgorithmSection> algorithmSections = new LinkedList<OMAlgorithmSection>();

	public OMComposition(int id) {
		super(id);
	}

	public OMComposition(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
	
	

	@Override
	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(elementList.toCode());
		for (OMElementList el : publicElementLists) {
			sb.append(el.toCode());
		}
		if(protectedElementLists!=null&&protectedElementLists.size()>0) {
			if(protectedElementLists.get(0).elements.size()>0) {
				sb.append("protected\n");
			}
		}
		for (OMElementList el : protectedElementLists) {
			sb.append(el.toCode());
		}
		for (OMEquationSection es : equationSections) {
			sb.append(es.toCode());
		}
		for (OMAlgorithmSection as : algorithmSections) {
			sb.append(as.toCode());
		}
		return sb.toString();
	}
	//	
	// public OMComposition copy() {
	// OMComposition copy = new OMComposition(parser, id);
	// copy.elementList = elementList.copy();
	// for (OMElementList el : publicElementLists) {
	// copy.publicElementLists.add(el.copy());
	// }
	// for (OMElementList el : protectedElementLists) {
	// copy.protectedElementLists.add(el.copy());
	// }
	// for (OMEquationSection es : equationSections) {
	// copy.equationSections.add(es.copy());
	// }
	// for (OMAlgorithmSection as : algorithmSections) {
	// copy.algorithmSections.add(as.copy());
	// }
	// return copy;
	// }
}

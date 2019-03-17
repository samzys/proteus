/* Generated By:JJTree: Do not edit this line. OMArgumentList.java */

package proteus.gwt.shared.modelica.parser;

import java.util.LinkedList;
import java.util.List;

public class OMArgumentList extends SimpleNode {

	public List<OMArgument> argList = new LinkedList<OMArgument>();

	public OMArgumentList(int id) {
		super(id);
	}

	public OMArgumentList(ModelicaParser p, int id) {
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
		int i = 0;
		for (OMArgument arg : argList) {
			if (i++ > 0) {
				sb.append(",\n");
			}
			sb.append(arg.toCode());
		}
		return sb.toString();
	}
	//	
	// public OMArgumentList copy() {
	// OMArgumentList copy = new OMArgumentList(parser, id);
	// for (OMArgument arg : argList) {
	// copy.argList.add(arg.copy());
	// }
	// return copy;
	// }
}

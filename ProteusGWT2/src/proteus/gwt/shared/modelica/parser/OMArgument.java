/* Generated By:JJTree: Do not edit this line. OMArgument.java */

package proteus.gwt.shared.modelica.parser;

public class OMArgument extends SimpleNode {

	public OMElementModificationOrReplaceable elementModificationOrReplaceable;

	public OMElementRedeclaration elementRedeclaration;

	public OMArgument(int id) {
		super(id);
	}

	public OMArgument(ModelicaParser p, int id) {
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
		if (elementModificationOrReplaceable != null) {
			sb.append(elementModificationOrReplaceable.toCode());
		} else if (elementRedeclaration != null) {
			sb.append(elementRedeclaration.toCode());
		}
		return sb.toString();
	}
	//	
	// public OMArgument copy() {
	// OMArgument copy = new OMArgument(parser, id);
	// copy.elementModificationOrReplaceable =
	// elementModificationOrReplaceable.copy();
	// copy.elementRedeclaration = elementRedeclaration.copy();
	// return copy;
	// }
}

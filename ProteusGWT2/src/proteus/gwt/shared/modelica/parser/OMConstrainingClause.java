/* Generated By:JJTree: Do not edit this line. OMConstrainingClause.java */

package proteus.gwt.shared.modelica.parser;

public class OMConstrainingClause extends SimpleNode {

	public String name;

	public OMClassModification classModification;

	public OMConstrainingClause(int id) {
		super(id);
	}

	public OMConstrainingClause(ModelicaParser p, int id) {
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
		sb.append("constrainedby " + name + " ");
		if (classModification != null) {
			sb.append(classModification.toCode());
		}
		return sb.toString();
	}
}

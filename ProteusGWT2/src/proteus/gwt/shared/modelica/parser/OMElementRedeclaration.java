/* Generated By:JJTree: Do not edit this line. OMElementRedeclaration.java */

package proteus.gwt.shared.modelica.parser;

public class OMElementRedeclaration extends SimpleNode {

	public boolean each;

	public boolean final_;

	public OMClassDefinition classDefinition;

	public OMComponentClause1 componentClause1;

	public OMElementReplaceable elementReplaceable;

	public OMElementRedeclaration(int id) {
		super(id);
	}

	public OMElementRedeclaration(ModelicaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(ModelicaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}

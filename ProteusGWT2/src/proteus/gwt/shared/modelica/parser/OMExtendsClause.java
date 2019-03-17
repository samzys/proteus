/* Generated By:JJTree: Do not edit this line. OMExtendsClause.java */

package proteus.gwt.shared.modelica.parser;




public class OMExtendsClause extends SimpleNode {

//	@StringType(name = "Name", category = CATEGORY_CODE)
	public String name;

	public OMClassModification classModification;

	public OMAnnotation annotation;

	public OMExtendsClause(int id) {
		super(id);
	}

	public OMExtendsClause(ModelicaParser p, int id) {
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
		sb.append("extends " + name);
		return sb.toString();
	}
}
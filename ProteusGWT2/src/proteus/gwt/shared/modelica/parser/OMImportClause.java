/* Generated By:JJTree: Do not edit this line. OMImportClause.java */

package proteus.gwt.shared.modelica.parser;




public class OMImportClause extends SimpleNode {

//	@StringType(name = "Name", category = CATEGORY_CODE)
	public String name;

//	@BooleanType(name = "Include subclasses", category = CATEGORY_CODE)
	public boolean includeSub;

//	@StringType(name = "Alias", category = CATEGORY_CODE)
	public String alias;

	public OMComment comment;

	public OMImportClause(int id) {
		super(id);
	}

	public OMImportClause(ModelicaParser p, int id) {
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
		sb.append("import ");
		if (alias != null) {
			sb.append(alias + " = ");
		}
		sb.append(name);
		if (includeSub) {
			sb.append(".*");
		}
		sb.append(comment.toCode());
		return sb.toString();
	}
}

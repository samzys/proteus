/* Generated By:JJTree: Do not edit this line. OMWhenStatement.java */

package proteus.gwt.shared.modelica.parser;

import java.util.LinkedList;
import java.util.List;

public class OMWhenStatement extends SimpleNode {

	public OMExpression whenExpression;

	public List<OMStatement> whenStatements = new LinkedList<OMStatement>();

	public List<OMExpression> elseWhenExpressions = new LinkedList<OMExpression>();

	public List<List<OMStatement>> elseWhenStatementsList = new LinkedList<List<OMStatement>>();

	public OMWhenStatement(int id) {
		super(id);
	}

	public OMWhenStatement(ModelicaParser p, int id) {
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
		sb.append("when " + whenExpression.toCode() + " then\n");
		for (OMStatement st : whenStatements) {
			sb.append(st.toCode() + ";\n");
		}
		int i = 0;
		for (OMExpression exp : elseWhenExpressions) {
			sb.append("elsewhen " + exp.toCode() + " then\n");
			List<OMStatement> list = elseWhenStatementsList.get(i++);
			if (list != null) {
				for (OMStatement st : list) {
					sb.append(st.toCode() + ";\n");
				}
			}
		}
		return sb.toString();
	}
}

package proteus.gwt.shared.modelica;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proteus.gwt.shared.modelica.ClassDef.Element;
import proteus.gwt.shared.modelica.parser.OMAnnotation;
import proteus.gwt.shared.modelica.parser.OMArgument;
import proteus.gwt.shared.modelica.parser.OMClassDefinition;
import proteus.gwt.shared.modelica.parser.OMComment;
import proteus.gwt.shared.modelica.parser.OMComponentClause;
import proteus.gwt.shared.modelica.parser.OMComponentRef;
import proteus.gwt.shared.modelica.parser.OMConnectClause;
import proteus.gwt.shared.modelica.parser.OMElement;
import proteus.gwt.shared.modelica.parser.OMEquation;
import proteus.gwt.shared.modelica.parser.OMExpression;
import proteus.gwt.shared.modelica.parser.OMExtendsClause;
import proteus.gwt.shared.modelica.parser.OMImportClause;
import proteus.gwt.shared.modelica.parser.OMModification;
import proteus.gwt.shared.modelica.parser.OMNamedArgument;
import proteus.gwt.shared.modelica.parser.SimpleNode;
import proteus.gwt.shared.types.StringProperty;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class ClassDef implements IsSerializable {

	public static class Annotation implements Serializable {
		private OMAnnotation annotationOM;

		public Map<String, SimpleNode> map = new HashMap<String, SimpleNode>();

		public Annotation(OMAnnotation annotationOM) {
			this.annotationOM = annotationOM;
		}

		public OMAnnotation getObjectModel() {
			return annotationOM;
		}
	}

	public static class Argument implements Serializable {
		private OMArgument argumentOM;

		public Modification modification;

		public String name; // read only, so only a string

		public Argument(OMArgument argumentOM) {
			this.argumentOM = argumentOM;
		}

		public OMArgument getObjectModel() {
			return argumentOM;
		}
	}

	public static class Comment implements Serializable {
		public Annotation annotation;

		private OMComment commentOM;

		public String string;

		public Comment(OMComment commentOM) {
			this.commentOM = commentOM;
		}

		public OMComment getObjectModel() {
			return commentOM;
		}
	}

	public static class ComponentClause extends ElementClause implements
			Serializable {

		private OMComponentClause compClauseOM;

		public List<ComponentDecl> declList = new LinkedList<ComponentDecl>();

		public String typeName;

		// 19-02-2012. flow/stream
		public String flow_stream;
		// 19-02-2012 input/output
		public StringProperty causality;
		// 19-02-2012 discrete/parameter/constant
		public StringProperty variability;

		public ComponentClause(OMComponentClause compClauseOM) {
			this.compClauseOM = compClauseOM;
		}

		public OMComponentClause getObjectModel() {
			return compClauseOM;
		}
	}

	public static class ComponentDecl implements Serializable {
		public List<Integer> arrarysubList = new LinkedList<Integer>();
		public List<String> arrarysubscrpts = new LinkedList<String>();
		public String booleanVariable; // 11 Nov, 2010 sam
		public Comment comment;
		public Modification modification;
		public boolean notSign = false; // 11 Nov, 2010 sam

		public List<Double> valList = new LinkedList<Double>();
		public StringProperty varName;
	}

	public static class ComponentRef implements IsSerializable {
		public ComponentRef componentRef;

		private OMComponentRef componentRefOM;

		public String ident;

		public ComponentRef(OMComponentRef componentRefOM) {
			this.componentRefOM = componentRefOM;
		}

		public OMComponentRef getObjectModel() {
			return componentRefOM;
		}
	}

	@Deprecated
	public static class CompositionClause implements Serializable {
		// public String AcessControl;
		// default, public, protected

	}

	public static class ConnectClause implements IsSerializable {
		private OMConnectClause connectClauseOM;

		public ComponentRef dest;

		public ComponentRef src;

		public ConnectClause(OMConnectClause connectClauseOM) {
			this.connectClauseOM = connectClauseOM;
		}

		public OMConnectClause getObjectModel() {
			return connectClauseOM;
		}
	}

	public static class Element implements IsSerializable {
		public ClassDef classDef;
		public ComponentClause compClause;

		public boolean display;// ??

		private OMElement elementOM;

		public ExtendsClause extendsClause;

		public ImportClause importClause;

		// 19-02-2012 sam
		public StringProperty redeclareProperty;
		public StringProperty replaceableProperty;
		public StringProperty finalProperty;
		// 19-02-2012 sam. inner/outer
		public StringProperty typingProperty;
		public StringProperty protectedProperty;

		public Element(OMElement elementOM) {
			this.elementOM = elementOM;
		}

		public OMElement getObjectModel() {
			return elementOM;
		}
	}

	@Deprecated
	public static class ElementClause extends CompositionClause implements
			Serializable {
		// public ClassDef classDef;
		// public String redeclare;
		// public String replaceable;
		// 19-02-2012 sam
		// public String finalProperty;
		// 19-02-2012 sam. inner/outer
		// public String typingProperty;
	}

	public static class Equation implements IsSerializable {
		// @sam 22 Apr, 2011
		public Comment comment;
		public ConnectClause connectClause;

		private OMEquation equationOM;

		public Equation(OMEquation equationOM) {
			this.equationOM = equationOM;
		}

		public OMEquation getObjectModel() {
			return equationOM;
		}
	}

	public static class Expression implements IsSerializable {
		private OMExpression expressionOM;

		public StringProperty value;

		public Expression(OMExpression expressionOM) {
			this.expressionOM = expressionOM;
		}

		public OMExpression getObjectModel() {
			return expressionOM;
		}
	}

	public static class ExtendsClause extends CompositionClause implements
			Serializable {
		public List<Argument> argList;

		private OMExtendsClause extendsClauseOM;

		public String name;

		public ExtendsClause(OMExtendsClause extendsClauseOM) {
			this.extendsClauseOM = extendsClauseOM;
		}

		public OMExtendsClause getObjectModel() {
			return extendsClauseOM;
		}
	}

	public static class ImportClause extends CompositionClause implements
			Serializable {
		public String alias;

		private OMImportClause importClauseOM;

		public boolean includeSub;

		public String name;

		public ImportClause(OMImportClause importClauseOM) {
			this.importClauseOM = importClauseOM;
		}

		public OMImportClause getObjectModel() {
			return importClauseOM;
		}
	}

	public static class Modification implements Serializable {
		public List<Argument> arguments = new LinkedList<Argument>();

		public Expression expression;

		private OMModification modificationOM;

		public Modification(OMModification modificationOM) {
			this.modificationOM = modificationOM;
		}

		public Argument getArg(String name) {
			// 18 Jan ,2011 Sam
			if (arguments == null)
				return null;
			for (Argument arg : arguments) {
				if (arg.name != null) {
					if (arg.name.equals(name)) {
						return arg;
					}
				}
			}
			return null;
		}

		public OMModification getObjectModel() {
			return modificationOM;
		}

		public void putArg(String name, Argument arg) {
			Argument[] args = arguments.toArray(new Argument[0]);
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals(name)) {
					arguments.set(i, arg);
					return;
				}
			}
			arguments.add(arg);
		}
	}

	public static class NamedArgument implements IsSerializable {
		public Expression expression;

		public String name; // read only, so only a string

		private OMNamedArgument namedArgumentOM;

		public NamedArgument(OMNamedArgument namedArgumentOM) {
			this.name = namedArgumentOM.name;
			this.namedArgumentOM = namedArgumentOM;
			this.expression = new Expression(namedArgumentOM.expression);
		}

		public OMNamedArgument getObjectModel() {
			return namedArgumentOM;
		}
	}

	private OMClassDefinition classDefOM;

	// store component clauser in this list
	public List<ComponentClause> compList = new LinkedList<ComponentClause>();

	public StringProperty description;

	public List<Element> elements = new LinkedList<Element>();

	public boolean encapsulated;

	public List<Equation> equations = new LinkedList<Equation>();

	public boolean final_;

	public StringProperty name; // shared by all selctions to store the IDENT

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(StringProperty name) {
		this.name = name;
	}

	public boolean partial;

	public String restriction;

	public String type;

	public ClassDef() {
	}

	public ClassDef(OMClassDefinition classDefOM) {
		this.classDefOM = classDefOM;
	}

	public void addElement(Element element) {
		classDefOM.classSpecifier.composition.elementList.elements.add(element
				.getObjectModel());
		elements.add(element);
		if (element.compClause != null) {
			compList.add(element.compClause);
		}
	}

	public void addProtectedElement(Element element) {
		classDefOM.classSpecifier.composition.protectedElementLists.get(0).elements
				.add(element.getObjectModel());
		//TODO
		elements.add(element);
		if (element.compClause != null) {
			compList.add(element.compClause);
		}

	}

	public void addEquation(Equation equation) {
		classDefOM.classSpecifier.composition.equationSections.get(0).equations
				.add(equation.getObjectModel());
		equations.add(equation);
	}

	/**
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}

	public void removeElement(Element element) {
		if (element.compClause != null) {
			compList.remove(element.compClause);
		}
		classDefOM.classSpecifier.composition.elementList.elements
				.remove(element.getObjectModel());
		elements.remove(element);
	}

	public void Change2ProtectedElement(Element element) {
		if (classDefOM.classSpecifier.composition.elementList.elements
				.contains(element.getObjectModel())) {
			classDefOM.classSpecifier.composition.elementList.elements
					.remove(element.getObjectModel());
			classDefOM.classSpecifier.composition.protectedElementLists.get(0).elements
					.add(element.getObjectModel());
		} else if (classDefOM.classSpecifier.composition.protectedElementLists
				.get(0).elements.contains(element.getObjectModel())) {
			// do nothing here.
		} else {
			// should return error here.
		}
	}

	public void Change2PublicElement(Element element) {
		if (classDefOM.classSpecifier.composition.protectedElementLists.get(0).elements
				.contains(element.getObjectModel())) {
			classDefOM.classSpecifier.composition.protectedElementLists.get(0).elements
					.remove(element.getObjectModel());
			classDefOM.classSpecifier.composition.elementList.elements
					.add(element.getObjectModel());
		} else if (classDefOM.classSpecifier.composition.elementList.elements
				.contains(element.getObjectModel())) {
			// do nothing here.
		} else {
			// should return error here.
		}
	}

	public void removeEquation(Equation equation) {
		classDefOM.classSpecifier.composition.equationSections.get(0).equations
				.remove(equation.getObjectModel());
		equations.remove(equation);
	}

	public String toCode() {
		return classDefOM.toCode();
	}

}

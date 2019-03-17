package com.infoscient.proteus.modelica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infoscient.proteus.Constants;
import com.infoscient.proteus.modelica.Component.ComplexComponent;
import com.infoscient.proteus.modelica.Component.Parameter;
import com.infoscient.proteus.modelica.Component.Result;
import com.infoscient.proteus.modelica.parser.OMAnnotation;
import com.infoscient.proteus.modelica.parser.OMArgument;
import com.infoscient.proteus.modelica.parser.OMClassDefinition;
import com.infoscient.proteus.modelica.parser.OMComment;
import com.infoscient.proteus.modelica.parser.OMComponentClause;
import com.infoscient.proteus.modelica.parser.OMComponentRef;
import com.infoscient.proteus.modelica.parser.OMConnectClause;
import com.infoscient.proteus.modelica.parser.OMElement;
import com.infoscient.proteus.modelica.parser.OMEquation;
import com.infoscient.proteus.modelica.parser.OMExpression;
import com.infoscient.proteus.modelica.parser.OMExtendsClause;
import com.infoscient.proteus.modelica.parser.OMImportClause;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.parser.OMNamedArgument;
import com.infoscient.proteus.modelica.parser.SimpleNode;
import com.infoscient.proteus.modelica.runtime.SymbolTable.Scope;
import com.infoscient.proteus.modelica.types.MPrimitive;
import com.infoscient.proteus.types.BooleanProperty;
import com.infoscient.proteus.types.EnumProperty;
import com.infoscient.proteus.types.Property;
import com.infoscient.proteus.types.PropertyContainer;
import com.infoscient.proteus.types.StringProperty;
import com.infoscient.proteus.types.StringType;

public abstract class ClassDef implements Constants, PropertyContainer,
		Serializable {
	public String restriction;
	// class declaration name
	public StringProperty name; //shared by all selctions to store the IDENT
	// used in short class
	public String refName; // 21 Nov, 2010 for resolving type parameters //shared by selction 1 and 3

	public String[] arraySubs; //for selection 1 short class
	// 21 Nov, 2010 for resolviing type
	public MPrimitive primitiveType;
	// parameters // for Real Number Arg, keep it here
	public List<Argument> argList; //shared by selection 1 and 4 class_modification. 
	// for class speficier extends clause only
	public boolean extends_;

	public boolean shortClass;

	@StringType(name = "Type", category = CATEGORY_CODE, description = "Fully qualified name", readonly = true)
	public String type;

	public StringProperty description; //shared by selection 0 and 4 to store string comment

	public Comment classComment; //shared by selection 1, 2, 3

	public BooleanProperty final_;

	public BooleanProperty encapsulated;

	public BooleanProperty partial;

	// @sam 4 Oct, 2010. for reading the connector component with base_prefix
	// and classmodification
	public String type_prefix;

	public boolean editable = true;
	// store extends classes of this component
	public List<StringProperty> references = new ArrayList<StringProperty>();
	// storing all elements, including extends, import, and comp
	public List<Element> elements = new LinkedList<Element>();
	// to store all annotations inside component exclude componentclause annotation, connection clause annotation  @sam
	public List<Annotation> annotations = new LinkedList<Annotation>();

	public List<Equation> equations = new LinkedList<Equation>();
	// 2nd phase of parsing, for storing parameters
	public List<Parameter> paramList = new LinkedList<Parameter>();
	// 2nd phase of parsing for storing result parameters
	public List<Result> resultList = new LinkedList<Result>();

//	public Connector[] _connectors;

	//	2nd phase of parsing, for storing parameters
	public List<Parameter> _paramList = new LinkedList<Parameter>();
	// 2nd phase of parsing for storing result parameters
	public List<Result> _resultList = new LinkedList<Result>();
	// store extends information inside this list @sam (add modification later
	// on)
	public List<ComplexComponent> _ccList = new LinkedList<ComplexComponent>();

	public List<ExtendsClause> extendsList = new LinkedList<ExtendsClause>();
	// store imports clause inisde this list
	public List<ImportClause> importList = new LinkedList<ImportClause>();
	// store component clauser in this list
	public List<ComponentClause> compList = new LinkedList<ComponentClause>();
	// store parent and children classes def
	public List<ClassDef> childrenList = new LinkedList<ClassDef>();
	// future work store everything inside at the frist place

	public String extendsName; // 09 Oct 05 lt

	public String sourceFilePath; // 09 Oct 15 lt

	public Scope scope; // 09 Nov 13 lt

	private Property[] containedProperties;

	private OMClassDefinition classDefOM;
	

	public ClassDef(OMClassDefinition classDefOM) {
		this.classDefOM = classDefOM;
	}

	public OMClassDefinition getObjectModel() {
		return classDefOM;
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	// get function value , sam
	// a list of functions which can be resolved
	public Object getValue(Object... args) {
		// resovle for 1
		List<Double> doubleList = new LinkedList<Double>();
		double[] input;
		for (Object o : args) {
			if (o instanceof Double) {
				doubleList.add((Double) o);
			}
		}

		String s = type.substring(type.lastIndexOf(".") + 1);
		if (s == null)
			return null;
		if (doubleList.size() < 1)
			return null;
		double v = doubleList.get(0);
		double v2 = 0.0;
		if (doubleList.size() > 1) {
			v2 = doubleList.get(1);
		}
		if (s.equals("sin")) {
			return Math.sin(v);
		} else if (s.equals("cos")) {
			return Math.cos(v);
		} else if (s.equals("tan")) {
			return Math.tan(v);
		} else if (s.equals("asin")) {
			return Math.asin(v);
		} else if (s.equals("acos")) {
			return Math.acos(v);
		} else if (s.equals("atan")) {
			return Math.atan(v);
		} else if (s.equals("atan2")) {// taken 2 parameters
			if (doubleList.size() < 2)
				return null;
			return Math.atan2(v, v2);
		} else if (s.equals("atan3")) {
			return null;// not able to resolve
		} else if (s.equals("sinh")) {
			return Math.sinh(v);
		} else if (s.equals("cosh")) {
			return Math.cosh(v);
		} else if (s.equals("tanh")) {
			return Math.tanh(v);
		} else if (s.equals("asinh")) {
			return Math.log(v + Math.sqrt(v * v + 1));
		} else if (s.equals("acosh")) {
			if (v < 1)
				return null;
			return Math.log(v + Math.sqrt(v * v - 1));
		} else if (s.equals("exp")) {
			return Math.exp(v);
		} else if (s.equals("log")) {
			return Math.log(v);
		} else if (s.equals("log10")) {
			return Math.log10(v);
		}

		return null;
	}

	// 09 Nov 18 lt
	public String getPackageName() {
		int pos = type.lastIndexOf('.');
		if (pos == -1) {
			return "";
		} else {
			return type.substring(0, pos);
		}
	}

//	public Icon getIcon() {
//		return icon;
//	}
//
//	public void setIcon(Icon icon) {
//		this.icon = icon;
//	}
//
//	public Icon getDiagram() {
//		return diagram;
//	}
//
//	public void setDiagram(Icon diagram) {
//		this.diagram = diagram;
//	}

	public void addElement(Element element) {
		classDefOM.classSpecifier.composition.elementList.elements.add(element
				.getObjectModel());
		elements.add(element);
		if (element.compClause != null) {
			compList.add(element.compClause);
		}
	}

	public void removeElement(Element element) {
		classDefOM.classSpecifier.composition.elementList.elements
				.remove(element.getObjectModel());
		elements.remove(element);
	}

	public void addEquation(Equation equation) {
		classDefOM.classSpecifier.composition.equationSections.get(0).equations
				.add(equation.getObjectModel());
		equations.add(equation);
	}

	public void removeEquation(Equation equation) {
		classDefOM.classSpecifier.composition.equationSections.get(0).equations
				.remove(equation.getObjectModel());
		equations.remove(equation);
	}

	public Property[] getContainedProperties() {
		if (containedProperties == null) {
			List<Property> list = new LinkedList<Property>();
			list.add(name);
			list.add(description);
			list.add(final_);
			list.add(encapsulated);
			list.add(partial);
			containedProperties = list.toArray(new Property[0]);
		}
		return containedProperties;
	}

	public String toCode() {
		return classDefOM.toCode();
	}

	public static class ImportClause extends CompositionClause implements
			Serializable {
		public StringProperty name;

		public BooleanProperty includeSub;

		public StringProperty alias;

		private OMImportClause importClauseOM;

		public ImportClause(OMImportClause importClauseOM) {
			this.importClauseOM = importClauseOM;
		}

		public OMImportClause getObjectModel() {
			return importClauseOM;
		}
	}

	public static class ExtendsClause extends CompositionClause implements
			Serializable {
		public StringProperty name;

		public List<Argument> argList;

		private OMExtendsClause extendsClauseOM;

		public ExtendsClause(OMExtendsClause extendsClauseOM) {
			this.extendsClauseOM = extendsClauseOM;
		}

		public OMExtendsClause getObjectModel() {
			return extendsClauseOM;
		}
	}

	public static class Element implements Serializable {
		public boolean display;
		public ImportClause importClause;

		public ExtendsClause extendsClause;

		public BooleanProperty replaceable;

		public BooleanProperty redeclare;

		public ComponentClause compClause;

		public ClassDef classDef;

		private OMElement elementOM;

		public Element(OMElement elementOM) {
			this.elementOM = elementOM;
		}

		public OMElement getObjectModel() {
			return elementOM;
		}
	}

	public static class ComponentDecl implements Serializable {
		public StringProperty varName;
		public List<String> arrarysubscrpts = new LinkedList<String>();
		public Modification modification;
		public boolean notSign = false; // 11 Nov, 2010 sam
		public String booleanVariable; // 11 Nov, 2010 sam
		public Comment comment;

		//@@sam comment@@ this two fields are for testing by sam only. please do not use 
		public List<Double> valList = new LinkedList<Double>();
		public List<Integer> arrarysubList = new LinkedList<Integer>();

	}

	public static class CompositionClause implements Serializable {
		public EnumProperty protected_;
		//default, public, protected
	}

	public static class ElementClause extends CompositionClause implements
			Serializable {
		@Deprecated
		public BooleanProperty replaceable;
		@Deprecated
		public BooleanProperty redeclare;
		public BooleanProperty final_;
		public EnumProperty typing;
		public ClassDef classDef;
	}

	public static class ComponentClause extends ElementClause implements
			Serializable {

		public EnumProperty flow_stream;

		public EnumProperty variability;

		public EnumProperty causality;

		public StringProperty typeName;

		public List<ComponentDecl> declList = new LinkedList<ComponentDecl>();

		//15 June, 2011, added by sam. 
		public List<String> arrarysubscrpts = new LinkedList<String>();
		
		private OMComponentClause compClauseOM;

		public ComponentClause(OMComponentClause compClauseOM) {
			this.compClauseOM = compClauseOM;
		}

		public OMComponentClause getObjectModel() {
			return compClauseOM;
		}
	}

	public static class Comment implements Serializable {
		public StringProperty string;

		public Annotation annotation;

		private OMComment commentOM;

		public Comment(OMComment commentOM) {
			this.commentOM = commentOM;
		}

		public OMComment getObjectModel() {
			return commentOM;
		}
	}

	public static class Annotation implements Serializable {
		public Map<String, SimpleNode> map = new HashMap<String, SimpleNode>();

		private OMAnnotation annotationOM;

		public Annotation(OMAnnotation annotationOM) {
			this.annotationOM = annotationOM;
		}

		public OMAnnotation getObjectModel() {
			return annotationOM;
		}
	}

	public static class Equation implements Serializable {
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

	public static class ConnectClause implements Serializable {
		public ComponentRef src;

		public ComponentRef dest;

		private OMConnectClause connectClauseOM;

		public ConnectClause(OMConnectClause connectClauseOM) {
			this.connectClauseOM = connectClauseOM;
		}

		public OMConnectClause getObjectModel() {
			return connectClauseOM;
		}
	}

	public static class ComponentRef implements Serializable {
		public StringProperty ident;

		public ComponentRef componentRef;

		private OMComponentRef componentRefOM;

		public ComponentRef(OMComponentRef componentRefOM) {
			this.componentRefOM = componentRefOM;
		}

		public OMComponentRef getObjectModel() {
			return componentRefOM;
		}
	}

	public static class Expression implements Serializable {
		public StringProperty value;

		private OMExpression expressionOM;

		public Expression(OMExpression expressionOM) {
			this.expressionOM = expressionOM;
		}

		public OMExpression getObjectModel() {
			return expressionOM;
		}
	}

	public static class Argument implements Serializable {
		public String name; // read only, so only a string

		public Modification modification;

		private OMArgument argumentOM;

		public Argument(OMArgument argumentOM) {
			this.argumentOM = argumentOM;
		}

		public OMArgument getObjectModel() {
			return argumentOM;
		}
	}

	public static class NamedArgument implements Serializable {
		public String name; // read only, so only a string

		public Expression expression;

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

	public static class Modification implements Serializable {
		public Expression expression;

		public List<Argument> arguments = new LinkedList<Argument>();

		private OMModification modificationOM;

		public Modification(OMModification modificationOM) {
			this.modificationOM = modificationOM;
		}

		public OMModification getObjectModel() {
			return modificationOM;
		}

		public Argument getArg(String name) {
//18 Jan ,2011 Sam			
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
}
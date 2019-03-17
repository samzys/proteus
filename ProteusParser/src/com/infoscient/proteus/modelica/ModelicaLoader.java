package com.infoscient.proteus.modelica;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import com.infoscient.proteus.Constants;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.Argument;
import com.infoscient.proteus.modelica.ClassDef.Comment;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.ComponentRef;
import com.infoscient.proteus.modelica.ClassDef.ConnectClause;
import com.infoscient.proteus.modelica.ClassDef.Element;
import com.infoscient.proteus.modelica.ClassDef.Equation;
import com.infoscient.proteus.modelica.ClassDef.Expression;
import com.infoscient.proteus.modelica.ClassDef.ExtendsClause;
import com.infoscient.proteus.modelica.ClassDef.ImportClause;
import com.infoscient.proteus.modelica.ClassDef.Modification;
import com.infoscient.proteus.modelica.ModelicaLoader.NodeEvent.Action;
import com.infoscient.proteus.modelica.parser.ModelicaParser;
import com.infoscient.proteus.modelica.parser.ModelicaParserConstants;
import com.infoscient.proteus.modelica.parser.OMAlgorithmSection;
import com.infoscient.proteus.modelica.parser.OMAnnotation;
import com.infoscient.proteus.modelica.parser.OMArgument;
import com.infoscient.proteus.modelica.parser.OMArgumentList;
import com.infoscient.proteus.modelica.parser.OMArithmeticExpression;
import com.infoscient.proteus.modelica.parser.OMClassDefinition;
import com.infoscient.proteus.modelica.parser.OMClassModification;
import com.infoscient.proteus.modelica.parser.OMClassSpecifier;
import com.infoscient.proteus.modelica.parser.OMComment;
import com.infoscient.proteus.modelica.parser.OMComponentClause;
import com.infoscient.proteus.modelica.parser.OMComponentClause1;
import com.infoscient.proteus.modelica.parser.OMComponentDecl;
import com.infoscient.proteus.modelica.parser.OMComponentDecl1;
import com.infoscient.proteus.modelica.parser.OMComponentList;
import com.infoscient.proteus.modelica.parser.OMComponentRef;
import com.infoscient.proteus.modelica.parser.OMComposition;
import com.infoscient.proteus.modelica.parser.OMCondAttr;
import com.infoscient.proteus.modelica.parser.OMConnectClause;
import com.infoscient.proteus.modelica.parser.OMConstrainingClause;
import com.infoscient.proteus.modelica.parser.OMDeclaration;
import com.infoscient.proteus.modelica.parser.OMElement;
import com.infoscient.proteus.modelica.parser.OMElementList;
import com.infoscient.proteus.modelica.parser.OMElementModification;
import com.infoscient.proteus.modelica.parser.OMElementModificationOrReplaceable;
import com.infoscient.proteus.modelica.parser.OMElementRedeclaration;
import com.infoscient.proteus.modelica.parser.OMElementReplaceable;
import com.infoscient.proteus.modelica.parser.OMEnumList;
import com.infoscient.proteus.modelica.parser.OMEnumLiteral;
import com.infoscient.proteus.modelica.parser.OMEquation;
import com.infoscient.proteus.modelica.parser.OMEquationSection;
import com.infoscient.proteus.modelica.parser.OMExpression;
import com.infoscient.proteus.modelica.parser.OMExpression.OMExpressionPtr;
import com.infoscient.proteus.modelica.parser.OMExtendsClause;
import com.infoscient.proteus.modelica.parser.OMExternalFuncCall;
import com.infoscient.proteus.modelica.parser.OMFactor;
import com.infoscient.proteus.modelica.parser.OMForEquation;
import com.infoscient.proteus.modelica.parser.OMForIndex;
import com.infoscient.proteus.modelica.parser.OMForIndices;
import com.infoscient.proteus.modelica.parser.OMForStatement;
import com.infoscient.proteus.modelica.parser.OMFunctionArguments;
import com.infoscient.proteus.modelica.parser.OMFunctionCallArgs;
import com.infoscient.proteus.modelica.parser.OMIfEquation;
import com.infoscient.proteus.modelica.parser.OMIfStatement;
import com.infoscient.proteus.modelica.parser.OMImportClause;
import com.infoscient.proteus.modelica.parser.OMLogicalExpression;
import com.infoscient.proteus.modelica.parser.OMLogicalFactor;
import com.infoscient.proteus.modelica.parser.OMLogicalTerm;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.parser.OMNamedArgument;
import com.infoscient.proteus.modelica.parser.OMPrimary;
import com.infoscient.proteus.modelica.parser.OMRelation;
import com.infoscient.proteus.modelica.parser.OMSimpleExpression;
import com.infoscient.proteus.modelica.parser.OMStatement;
import com.infoscient.proteus.modelica.parser.OMStoredDefinition;
import com.infoscient.proteus.modelica.parser.OMSubscript;
import com.infoscient.proteus.modelica.parser.OMTerm;
import com.infoscient.proteus.modelica.parser.OMTypePrefix;
import com.infoscient.proteus.modelica.parser.OMWhenEquation;
import com.infoscient.proteus.modelica.parser.OMWhenStatement;
import com.infoscient.proteus.modelica.parser.OMWhileStatement;
import com.infoscient.proteus.modelica.parser.ParseException;
import com.infoscient.proteus.modelica.parser.SimpleNode;
import com.infoscient.proteus.modelica.runtime.SymbolTable.Scope;
import com.infoscient.proteus.modelica.types.MEnumeration;
import com.infoscient.proteus.types.BooleanProperty;
import com.infoscient.proteus.types.EnumProperty;
import com.infoscient.proteus.types.PropertyFactory;
import com.infoscient.proteus.types.StringProperty;

//import com.infoscient.proteus.ui.Library;

public class ModelicaLoader extends ModelicaParserVisitorAdapter implements
		ModelicaParserConstants {

	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public ClassDef[] visit(OMStoredDefinition node, Object data) {
		List<ClassDef> classDefs = new LinkedList<ClassDef>();
		for (OMClassDefinition ocd : node.classDefinitions) {
			Object[] d = new Object[3];
			d[0] = node.name; // name
			d[1] = (String) data; // path
			d[2] = Scope.getInvisibleScope(); // scope
			ClassDef cd = (ClassDef) ocd.jjtAccept(this, d);
			if (cd != null) {
				classDefs.add(cd);
			}
		}
		ClassDef[] array = classDefs.toArray(new ClassDef[0]);
		return array;
	}

	public ClassDef visit(OMClassDefinition node, Object data) {
		Object[] d = (Object[]) data;
		String withinName = (String) d[0];
		String path = (String) d[1];
		Scope lastScope = (Scope) d[2];

		ClassDef classDef;
		// if (node.restriction.equals(Constants.TYPE_PACKAGE)) {
		// classDef = new Library.Category(node);
		// } else {
		classDef = new Component(node);
		// }
		classDef.type = withinName == null || withinName.length() <= 0 ? ""
				: withinName + ".";
		classDef.final_ = (BooleanProperty) PropertyFactory.createProperty(
				"final_", node);
		classDef.encapsulated = (BooleanProperty) PropertyFactory
				.createProperty("encapsulated", node);
		classDef.partial = (BooleanProperty) PropertyFactory.createProperty(
				"partial", node);
		classDef.restriction = node.restriction;

		classDef.sourceFilePath = path;
		classDef.scope = new Scope(lastScope);

		classDef = (ClassDef) node.classSpecifier.jjtAccept(this, classDef);

		// set the extendsName for drawing the Icon using extend clause
		// 12 Jan , 2011 Sam
		for (ExtendsClause ext : classDef.extendsList) {
			// to specify short class using extends clause
			// short class definition.
			if (classDef.extendsList.size() == 1) {
				if (ext != null) {
					classDef.refName = (String) ext.name.get();
					classDef.extendsName = classDef.refName;
				}
			}
		}

		return classDef;
	}

	public Object visit(OMClassSpecifier node, Object data) {
		ClassDef classDef = (ClassDef) data;
		classDef.name = (StringProperty) PropertyFactory.createProperty("name",
				node);
		classDef.type += (String) classDef.name.get();
		classDef.description = (StringProperty) PropertyFactory.createProperty(
				"description", node);

		switch (node.selection) {
		case 0:
			node.composition.jjtAccept(this, classDef);
			break;
		case 1:
			node.typePrefix.jjtAccept(this, classDef);
			classDef.refName = node.refName;
			Comment c = (Comment) node.comment.jjtAccept(this, classDef);
			// solve Modelica.Blocks.Interfaces.RealOutput annotation problem.
			if (c != null && c.annotation != null) {
				classDef.annotations.add(c.annotation);
			}
			if (node.arraySubs != null) {
				List<String> li = new LinkedList<String>();
				int i = 0;
				for (OMSubscript s : node.arraySubs) {
					li.add((String) s.jjtAccept(this, null));
				}
				classDef.arraySubs = li.toArray(new String[0]);
			}
			if (node.classModification != null) {
				classDef.argList = (List<Argument>) node.classModification
						.jjtAccept(this, classDef);
			}
			// add it in extend List for resolving

			classDef.shortClass = true;// specify short class tag
			break;
		case 2:
			classDef.refName = "enumeration";
			MEnumeration me = new MEnumeration();
			me.enumContent = (Map<String, String>) node.omEnumlist.jjtAccept(
					this, data);
			me.comment = node.comment.string;
			classDef.primitiveType = me;
			break;
		case 3:
			classDef.refName = "der";
			break;
		case 4:
			classDef.extends_ = true;
			classDef.shortClass = true;
			classDef.refName = (String) classDef.name.get();

			if (node.classModification != null)
				classDef.argList = (List<Argument>) node.classModification
						.jjtAccept(this, classDef); // only one
			// classsmodification is
			// allowed at class
			// specifier section
			node.composition.jjtAccept(this, classDef);// return null, all data
			// set inside classDef
			break;
		default:
			break;
		}

		return classDef;
	}

	public Object visit(OMEnumList node, Object data) {
		ClassDef cd = (ClassDef) data;
		String typeName = cd.type;
		Map enumList = new ArrayMap();
		for (OMEnumLiteral e : node.enumList) {
			String name = e.name;
			if (e.name.startsWith("'") || e.name.startsWith("\"")) {
				name = name.substring(1, name.length() - 1);
			}
			enumList.put(typeName + "." + name, e.comment.string);
			// enumList.add(new EnumPair(name, e.comment.string));
		}
		return enumList;
	}

	public Object visit(OMEnumLiteral node, Object data) {
		return null;
	}

	public Object visit(OMComposition node, Object data) {
		ClassDef classDef = (ClassDef) data;

		Object[] d = new Object[2];
		d[0] = classDef;
		d[1] = null;
		node.elementList.jjtAccept(this, d);

		d[1] = "public";
		for (OMElementList el : node.publicElementLists) {
			el.jjtAccept(this, d);
		}

		d[1] = "protected";
		for (OMElementList el : node.protectedElementLists) {
			// @Jan 6, 2011 Sam
			el.jjtAccept(this, d);
		}
		for (OMEquationSection es : node.equationSections) {
			es.jjtAccept(this, classDef);
		}
		for (OMAlgorithmSection as : node.algorithmSections) {
			as.jjtAccept(this, classDef);
		}

		if (node.annotation != null) {
			Annotation a = (Annotation) node.annotation.jjtAccept(this,
					classDef);
			if (a != null)
				classDef.annotations.add(a);
		}
		return null;
	}

	public Object visit(OMExternalFuncCall node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.componentRef != null) {
			sb.append(node.componentRef.jjtAccept(this, null) + " = ");
		}
		sb.append(node.funcName);
		sb.append("(");
		if (node.expList != null) {
			int i = 0;
			for (OMExpression exp : node.expList) {
				if (i++ > 0) {
					sb.append(", ");
				}
				sb.append(exp.jjtAccept(this, null));
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public Object visit(OMElementList node, Object data) {
		ClassDef classDef = null;
		if (data instanceof ClassDef) {
			classDef = (ClassDef) data;
		} else if (data instanceof Object[]) {
			Object[] d = (Object[]) data;
			classDef = (ClassDef) d[0];
		}

		for (OMElement ome : node.elements) {
			Element e = (Element) ome.jjtAccept(this, data);
			if (e != null) {
				// disable 11 Jan, 2011 sam.
				classDef.elements.add(e);
			}
		}

		for (OMAnnotation oma : node.annotations) {
			Annotation a = (Annotation) oma.jjtAccept(this, classDef);
			if (a != null) {
				classDef.annotations.add(a);
			}
		}
		return null;
	}

	public Element visit(OMElement node, Object data) {
		ClassDef classDef = null;
		String acc = null;
		if (data instanceof ClassDef) {
			classDef = (ClassDef) data;
		} else if (data instanceof Object[]) {
			Object d[] = (Object[]) data;
			classDef = (ClassDef) d[0];
			acc = (String) d[1];
		}

		node.protected_ = acc;

		Element element = null;
		if (node.importClause != null) {
			element = new Element(node);
			ImportClause importClause = (ImportClause) node.importClause
					.jjtAccept(this, classDef);
			// @sam 5 Dec, 2010
			importClause.protected_ = (EnumProperty) PropertyFactory
					.createProperty("protected_", node);
			classDef.importList.add(importClause);
			element.importClause = importClause;
		} else if (node.extendsClause != null) {
			element = new Element(node);
			ExtendsClause extendsClause = (ExtendsClause) node.extendsClause
					.jjtAccept(this, classDef);// sam 11, Nov, 2010
			// @sam add extends name for looking up
			extendsClause.protected_ = (EnumProperty) PropertyFactory
					.createProperty("protected_", node);
			classDef.extendsList.add(extendsClause);
			element.extendsClause = extendsClause;
		} else if (node.classDefinition != null) {
			element = new Element(node);
			element.replaceable = (BooleanProperty) PropertyFactory
					.createProperty("replaceable", node);

			Object[] d = new Object[3];
			d[0] = classDef.type;
			d[1] = classDef.sourceFilePath;
			d[2] = classDef.scope;
			ClassDef cd = (ClassDef) node.classDefinition.jjtAccept(this, d);
			// Enum
			classDef.childrenList.add(cd);
		} else if (node.componentClause != null) {
			element = new Element(node);
			element.replaceable = (BooleanProperty) PropertyFactory
					.createProperty("replaceable", node);
			element.redeclare = (BooleanProperty) PropertyFactory
					.createProperty("redeclare", node);
			// ####use componentClause instead of Element to simplify the later
			// parsing ####//
			ComponentClause compCluase = (ComponentClause) node.componentClause
					.jjtAccept(this, classDef);
			compCluase.protected_ = (EnumProperty) PropertyFactory
					.createProperty("protected_", node);
			compCluase.replaceable = (BooleanProperty) PropertyFactory
					.createProperty("replaceable", node);
			compCluase.redeclare = (BooleanProperty) PropertyFactory
					.createProperty("redeclare", node);
			compCluase.final_ = (BooleanProperty) PropertyFactory
					.createProperty("final_", node);
			compCluase.typing = (EnumProperty) PropertyFactory.createProperty(
					"typing", node);
			if (classDef != null) {
				classDef.compList.add(compCluase);
			}
			element.compClause = compCluase;
		}
		return element;
	}

	public ImportClause visit(OMImportClause node, Object data) {
		ImportClause importClause = new ImportClause(node);
		if (node.alias != null) {
			importClause.alias = (StringProperty) PropertyFactory
					.createProperty("alias", node);
		}
		importClause.name = (StringProperty) PropertyFactory.createProperty(
				"name", node);
		importClause.includeSub = (BooleanProperty) PropertyFactory
				.createProperty("includeSub", node);
		return importClause;
	}

	public Object visit(OMExtendsClause node, Object data) {
		ExtendsClause extendsClause = new ExtendsClause(node);
		extendsClause.name = (StringProperty) PropertyFactory.createProperty(
				"name", node);
		// 10 Nov, 2010 Sam
		if (node.classModification != null) {
			extendsClause.argList = (List<Argument>) node.classModification
					.jjtAccept(this, data);
		}
		if (node.annotation != null) {
			node.annotation.jjtAccept(this, data);
		}
		return extendsClause;
	}

	public Object visit(OMConstrainingClause node, Object data) {
		return null;
	}

	public ComponentClause visit(OMComponentClause node, Object data) {
		ComponentClause compClause = new ComponentClause(node);

		NodeEvent root;
		compClause.typeName = (StringProperty) PropertyFactory.createProperty(
				"typeName", node);
		// 15 June, 2011 added by sam.
		if (node.arraySubscripts != null) {
			for (OMSubscript oms : node.arraySubscripts) {
				compClause.arrarysubscrpts.add((String) oms.jjtAccept(
						ModelicaLoader.this, this));
			}
		}
		root = nodeEvent = new NodeEvent("typePrefix", OMTypePrefix.class);
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMTypePrefix tp = (OMTypePrefix) obj;
				ComponentClause cc = (ComponentClause) data;
				cc.flow_stream = (EnumProperty) PropertyFactory.createProperty(
						"flow_stream", tp);
				cc.variability = (EnumProperty) PropertyFactory.createProperty(
						"variability", tp);
				cc.causality = (EnumProperty) PropertyFactory.createProperty(
						"causality", tp);
				return true;
			}
		};

		appendEvent(new NodeEvent("componentList", OMComponentList.class));
		nodeEvent.action = new Action() {

			public boolean execute(Object obj, NodeEvent ne, Object data) {
				ComponentClause cc = (ComponentClause) data;
				OMComponentList omcL = (OMComponentList) obj;
				for (OMComponentDecl cd : omcL.componentDecls) {
					ComponentDecl compDecl = new ComponentDecl();
					OMDeclaration d = cd.decl;
					compDecl.varName = (StringProperty) PropertyFactory
							.createProperty("name", d);
					if (d.arraySubscripts != null) {
						for (OMSubscript oms : d.arraySubscripts) {
							compDecl.arrarysubscrpts.add((String) oms
									.jjtAccept(ModelicaLoader.this, this));
						}
					} else {
						// defalut value for double testing only:
						// TODO???
						compDecl.arrarysubList.add(1);
						compDecl.valList.add(0.0);
					}
					if (d.modification != null) {
						OMModification omm = d.modification;
						compDecl.modification = (Modification) omm.jjtAccept(
								ModelicaLoader.this, null);
					}
					// solve for conditional attribute
					if (cd.condAttr != null) {
						OMCondAttr oca = cd.condAttr;
						if (oca != null) {
							String exp = ((String) oca.jjtAccept(
									ModelicaLoader.this, null)).trim();
							String[] s = exp.split(" ");
							if (s.length > 0) {
								for (int i = 0; i < s.length; i++) {
									if (s.length == 3
											&& s[1].equalsIgnoreCase("not")) {
										compDecl.notSign = true;
									}
									compDecl.booleanVariable = s[s.length - 1];
								}
							}
						}
					}// end of conditional attribute
						// start solving for comment
					OMComment c = cd.comment;
					compDecl.comment = (Comment) c.jjtAccept(
							ModelicaLoader.this, null);
					cc.declList.add(compDecl);
				}// end of for loop
				return true;
			}
		};

		// tobe deleted
		// chainEvent(new NodeEvent("componentDecls", List.class));
		// chainEvent(new NodeEvent("comment", OMComment.class));
		// nodeEvent.action = new Action() {
		// public boolean execute(Object obj, NodeEvent ne, Object data) {
		// OMComment c = (OMComment) obj;
		// Comment cm = (Comment) c.jjtAccept(ModelicaLoader.this, null);
		// ComponentClause cc = (ComponentClause) data;
		// cc.comments.add(cm);
		// return true;
		// }
		// };
		// // 11 Nov, 2010 Sam. solving for connector dispaly
		// appendEvent(new NodeEvent("condAttr", OMCondAttr.class));
		// nodeEvent.action = new Action() {
		// public boolean execute(Object obj, NodeEvent ne, Object data) {
		// OMCondAttr oca = (OMCondAttr) obj;
		// ComponentClause cc = (ComponentClause) data;
		// if (oca != null) {
		// String exp = ((String) oca.jjtAccept(ModelicaLoader.this,
		// null)).trim();
		// String[] s = exp.split(" ");
		// if (s.length > 0) {
		// for (int i = 0; i < s.length; i++) {
		// if (s.length == 3 && s[1].equalsIgnoreCase("not")) {
		// cc.notSign = true;
		// }
		// cc.booleanVariable = s[s.length - 1];
		// }
		// }
		// return true;
		// }
		// return false;
		// }
		//
		// };
		// appendEvent(new NodeEvent("decl", OMDeclaration.class));
		// nodeEvent.action = new Action() {
		// public boolean execute(Object obj, NodeEvent nodeEvent, Object data)
		// {
		// OMDeclaration d = (OMDeclaration) obj;
		// ComponentClause cc = (ComponentClause) data;
		//
		// // tobe removed
		// cc.varNames.add((StringProperty) PropertyFactory
		// .createProperty("name", d));
		// cc.modifications.add(null);
		// return true;
		// }
		// };
		// chainEvent(new NodeEvent("modification", OMModification.class));
		// nodeEvent.action = new Action() {
		// public boolean execute(Object obj, NodeEvent nodeEvent, Object data)
		// {
		// OMModification m = (OMModification) obj;
		// ComponentClause cc = (ComponentClause) data;
		// Modification md = (Modification) m.jjtAccept(
		// ModelicaLoader.this, null);
		// cc.modifications.set(cc.modifications.size() - 1, md);
		// return false;
		// }
		// };
		walkTree(node, root, compClause);

		return compClause;
	}

	public Object visit(OMTypePrefix node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.flow_stream != null) {
			sb.append(node.flow_stream + " ");
		}
		if (node.variability != null) {
			sb.append(node.variability + " ");
		}
		if (node.causality != null) {
			sb.append(node.causality + " ");
		}

		// @sam 4 Oct, 2010. for reading the connector component with
		// base_prefix and classmodification
		ClassDef classDef = (ClassDef) data;
		if (classDef != null) {
			classDef.type_prefix = sb.toString();
		}
		return sb.toString();
	}

	public Object visit(OMComponentList node, Object data) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (OMComponentDecl cd : node.componentDecls) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append(cd.jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Object visit(OMComponentDecl node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.decl.jjtAccept(this, null));

		sb.append(node.comment.jjtAccept(this, null));
		return sb.toString();
	}

	public Object visit(OMCondAttr node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("if " + node.expression.jjtAccept(this, null));
		return sb.toString();
	}

	public Object visit(OMDeclaration node, Object data) {
		ComponentClause cc = (ComponentClause) data;

		StringBuilder sb = new StringBuilder();
		sb.append(node.name + " ");
		if (node.arraySubscripts != null) {
			sb.append("[");
			int i = 0;
			for (OMSubscript s : node.arraySubscripts) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(s.jjtAccept(this, null));
			}
			sb.append("] ");
		}
		if (node.modification != null) {
			sb.append(node.modification.jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Object visit(final OMModification node, Object data) {
		Modification md = null;
		if (node.classModification != null) {
			md = new Modification(node);
			if (node.classModification.argList != null) {
				List<OMArgument> omal = node.classModification.argList.argList;
				for (int i = 0; i < omal.size(); i++) {
					OMArgument a = omal.get(i);
					Argument arg = (Argument) a.jjtAccept(ModelicaLoader.this,
							data); // 10 Nov, 2010 sam
					// a.value = new StringProperty("Value",
					// Constants.CATEGORY_CODE,
					// "value", false) {
					// public Object custom_get() {
					// return omal.get(index).toCode();
					// }
					//
					// public void custom_set(Object value) {
					// String s = (String) value;
					// ModelicaParser parser = new ModelicaParser(
					// new StringReader(s));
					// try {
					// OMArgument noa = parser.argument();
					// omal.set(index, noa);
					// } catch (ParseException e) {
					// node.expression = null;
					// e.printStackTrace();
					// }
					// }
					// };
					md.arguments.add(arg);
				}
			}
		}
		if (node.expression != null) {
			if (md == null) {
				md = new Modification(node);
			}
			/*
			 * // Over-ride getObjectModel because the custom set method //
			 * replaces the object model, which is not accessible from // an
			 * Expression object New class def // for // each invocation??
			 * md.expression = new Expression(null) { public OMExpression
			 * getObjectModel() { return node.expression; } };
			 * md.expression.value = new StringProperty("Value",
			 * Constants.CATEGORY_CODE, "value", false) { public Object
			 * custom_get() { return node.expression != null ?
			 * node.expression.toCode() : ""; }
			 * 
			 * public void custom_set(Object value) { String s = (String) value;
			 * ModelicaParser parser = new ModelicaParser( new StringReader(s));
			 * try { node.expression = parser.expression(); } catch
			 * (ParseException e) { node.expression = null; e.printStackTrace();
			 * } } };
			 */
			if (!(node.expression instanceof OMExpressionPtr)) {
				node.expression = new OMExpressionPtr(node.expression);
			}
			md.expression = (Expression) node.expression.jjtAccept(
					ModelicaLoader.this, data); // 11 Nov, 2010 sam
		}
		return md;
	}

	public Object visit(OMClassModification node, Object data) {
		// StringBuilder sb = new StringBuilder();
		// sb.append("(" + node.argList.jjtAccept(this, data) + ")");
		// return sb.toString();
		return node.argList.jjtAccept(this, data);
	}

	public Object visit(OMArgumentList node, Object data) {
		List<Argument> argList = new LinkedList<Argument>();
		// StringBuilder sb = new StringBuilder();
		// int i = 0;
		for (OMArgument arg : node.argList) {
			// if (i++ > 0) {
			// sb.append(", ");
			// }
			// sb.append(arg.jjtAccept(this, data));
			argList.add((Argument) arg.jjtAccept(this, data));
		}
		return argList;
		// return sb.toString();
	}

	public Object visit(OMArgument node, Object data) {
		Argument arg = new Argument(node);
		if (node.elementModificationOrReplaceable != null) {
			OMElementModificationOrReplaceable emor = node.elementModificationOrReplaceable;
			if (emor.elementModification != null) {
				OMElementModification em = emor.elementModification;
				arg.name = em.componentRef.name;
				if (em.modification != null) {
					arg.modification = (Modification) em.modification
							.jjtAccept(ModelicaLoader.this, data); // 10 Nov,
					// 2010 Sam
				}
			} else if (emor.elementReplaceable != null) {
				// TODO: Handle case
			}
		} else if (node.elementRedeclaration != null) {
			// TODO: Handle case
		}
		return arg;
	}

	public Object visit(OMElementModificationOrReplaceable node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.each) {
			sb.append("each ");
		}
		if (node.final_) {
			sb.append("final ");
		}
		if (node.elementModification != null) {
			sb.append(node.elementModification.jjtAccept(this, null));
		} else if (node.elementReplaceable != null) {
			sb.append(node.elementReplaceable.jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Object visit(OMElementModification node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.componentRef.jjtAccept(this, null));
		if (node.modification != null) {
			sb.append(node.modification.jjtAccept(this, null));
		}
		sb.append(node.stringComment);
		return sb.toString();
	}

	public Object visit(OMElementRedeclaration node, Object data) {
		return null;
	}

	public Object visit(OMElementReplaceable node, Object data) {
		return null;
	}

	public Object visit(OMComponentClause1 node, Object data) {
		return null;
	}

	public Object visit(OMComponentDecl1 node, Object data) {
		return null;
	}

	public Object visit(OMEquationSection node, Object data) {
		ClassDef classDef = (ClassDef) data;

		for (OMEquation e : node.equations) {
			classDef.equations.add((Equation) e.jjtAccept(this, null));
		}

		for (OMAnnotation oma : node.annotations) {
			Annotation a = (Annotation) oma.jjtAccept(this, classDef);
			if (a != null) {
				classDef.annotations.add(a);
			}
		}

		return null;
	}

	public Object visit(OMAlgorithmSection node, Object data) {
		ClassDef classDef = data == null ? null : (ClassDef) data;
		StringBuilder sb = new StringBuilder();
		if (node.initial) {
			sb.append("initial ");
		}
		sb.append("algorithm\n");
		for (OMStatement s : node.statements) {
			sb.append(s.jjtAccept(this, null) + ";\n");
		}
		for (OMAnnotation a : node.annotations) {
			sb.append(a.jjtAccept(this, null) + ";\n");
		}
		for (OMAnnotation oma : node.annotations) {
			Annotation a = (Annotation) oma.jjtAccept(this, classDef);
			if (a != null) {
				classDef.annotations.add(a);
			}
		}
		return sb.toString();
	}

	public Equation visit(OMEquation node, Object data) {
		Equation equation = new Equation(node);
		if (node.connectClause != null) {
			equation.connectClause = new ConnectClause(node.connectClause);
			equation.connectClause.src = (ComponentRef) node.connectClause.src
					.jjtAccept(this, null);
			equation.connectClause.dest = (ComponentRef) node.connectClause.dest
					.jjtAccept(this, null);
		}
		equation.comment = (Comment) node.comment.jjtAccept(
				ModelicaLoader.this, null);

		return equation;
	}

	public Object visit(OMStatement node, Object data) {
		return null;
	}

	public Object visit(OMIfEquation node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("if " + node.ifExpression.jjtAccept(this, null) + " then\n");
		for (OMEquation eq : node.ifEquations) {
			sb.append(eq.jjtAccept(this, null) + ";\n");
		}
		int i = 0;
		for (OMExpression exp : node.elseIfExpressions) {
			sb.append("elseif " + exp.jjtAccept(this, null) + " then\n");
			List<OMEquation> eqs = node.elseIfEquationsList.get(i++);
			if (eqs != null) {
				for (OMEquation eq : eqs) {
					sb.append(eq.jjtAccept(this, null) + ";\n");
				}
			}
		}
		if (node.else_) {
			sb.append("else\n");
			for (OMEquation eq : node.elseEquations) {
				sb.append(eq.jjtAccept(this, null) + ";\n");
			}
		}
		sb.append("end if\n");
		return sb.toString();
	}

	public Object visit(OMIfStatement node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("if " + node.ifExpression.jjtAccept(this, null) + " then\n");
		for (OMStatement st : node.ifStatements) {
			sb.append(st.jjtAccept(this, null) + ";\n");
		}
		int i = 0;
		for (OMExpression exp : node.elseIfExpressions) {
			sb.append("elseif " + exp.jjtAccept(this, null) + " then\n");
			List<OMStatement> sts = node.elseIfStatementsList.get(i++);
			if (sts != null) {
				for (OMStatement st : sts) {
					sb.append(st.jjtAccept(this, null) + ";\n");
				}
			}
		}
		if (node.else_) {
			sb.append("else\n");
			for (OMStatement st : node.elseStatements) {
				sb.append(st.jjtAccept(this, null) + ";\n");
			}
		}
		sb.append("end if\n");
		return sb.toString();
	}

	public Object visit(OMForEquation node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("for " + node.forIndices.jjtAccept(this, null) + " loop\n");
		for (OMEquation eq : node.equations) {
			sb.append(eq.jjtAccept(this, null) + ";\n");
		}
		sb.append("end for\n");
		return sb.toString();
	}

	public Object visit(OMForStatement node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("for " + node.forIndices.jjtAccept(this, null) + "loop\n");
		for (OMStatement st : node.statements) {
			sb.append(st.jjtAccept(this, null) + ";\n");
		}
		sb.append("end for\n");
		return sb.toString();
	}

	public Object visit(OMForIndices node, Object data) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (OMForIndex fi : node.forIndices) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append(fi.jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Object visit(OMForIndex node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.name);
		if (node.inExpression != null) {
			sb.append(" in " + node.inExpression.jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Object visit(OMWhileStatement node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("while " + node.whileExpression.jjtAccept(this, null)
				+ "then\n");
		for (OMStatement s : node.statements) {
			sb.append(s.jjtAccept(this, null) + ";\n");
		}
		sb.append("end while\n");
		return sb.toString();
	}

	public Object visit(OMWhenEquation node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("when " + node.whenExpression.jjtAccept(this, null)
				+ " then\n");
		for (OMEquation eq : node.whenEquations) {
			sb.append(eq.jjtAccept(this, null) + ";\n");
		}
		int i = 0;
		for (OMExpression exp : node.elseWhenExpressions) {
			sb.append("elsewhen " + exp.jjtAccept(this, null) + " then\n");
			List<OMEquation> list = node.elseWhenEquationsList.get(i++);
			if (list != null) {
				for (OMEquation eq : list) {
					sb.append(eq.jjtAccept(this, null) + ";\n");
				}
			}
		}
		return sb.toString();
	}

	public Object visit(OMWhenStatement node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("when " + node.whenExpression.jjtAccept(this, null)
				+ " then\n");
		for (OMStatement st : node.whenStatements) {
			sb.append(st.jjtAccept(this, null) + ";\n");
		}
		int i = 0;
		for (OMExpression exp : node.elseWhenExpressions) {
			sb.append("elsewhen " + exp.jjtAccept(this, null) + " then\n");
			List<OMStatement> list = node.elseWhenStatementsList.get(i++);
			if (list != null) {
				for (OMStatement st : list) {
					sb.append(st.jjtAccept(this, null) + ";\n");
				}
			}
		}
		return sb.toString();
	}

	public Object visit(OMConnectClause node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("connect (" + node.src.jjtAccept(this, null) + ", "
				+ node.dest.jjtAccept(this, null) + ")");
		return sb.toString();
	}

	public Object visit(OMExpression node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (!(node instanceof OMExpressionPtr)) {
			if (node.simpleExpression != null) {
				return sb.append(node.simpleExpression.toCode());
			}
			return null; // 10 Nov, 2010 Sam
			// throw new IllegalArgumentException(
			// "Expecting node to be of class: OMExpressionPtr");
		}
		final OMExpressionPtr ptr = (OMExpressionPtr) node;
		Expression exp = new Expression(node);
		exp.value = new StringProperty("Value", Constants.CATEGORY_CODE,
				"value", false) {
			public Object custom_get() {
				return ptr != null ? ptr.toCode() : "";
			}

			public void custom_set(Object value) {
				String s = (String) value;
				ModelicaParser parser = new ModelicaParser(new StringReader(s));
				try {
					ptr.set(parser.expression());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};
		if (node.simpleExpression != null) { // 10 Nov, 2010 sam
			node.simpleExpression.jjtAccept(this, data);
		}
		return exp;
	}

	public Object visit(OMSimpleExpression node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.logicalExpression.jjtAccept(this, data));// 10 Nov, 2010
		// sam
		if (node.logicalExpression1 != null) {
			sb.append(" : " + node.logicalExpression1.jjtAccept(this, data));
			if (node.logicalExpression2 != null) {
				sb.append(" : " + node.logicalExpression2.jjtAccept(this, data));
			}
		}
		return sb.toString();
	}

	public Object visit(OMLogicalExpression node, Object data) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (OMLogicalTerm term : node.logicalTerms) {
			if (i++ > 0) {
				sb.append(" or ");
			}
			sb.append(term.jjtAccept(this, data));// 10 Nov, 2010, Sam
		}
		return sb.toString();
	}

	public Object visit(OMLogicalTerm node, Object data) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (OMLogicalFactor factor : node.logicalFactors) {
			if (i++ > 0) {
				sb.append(" and ");
			}
			sb.append(factor.jjtAccept(this, data));
		}
		return sb.toString();
	}

	public Object visit(OMLogicalFactor node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.not) {
			sb.append("not ");
		}
		sb.append(node.relation.jjtAccept(this, data));
		return sb.toString();
	}

	public Object visit(OMRelation node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.arithmeticExpression1 != null) {// 10 Nov, 2010 sam
			node.arithmeticExpression1.jjtAccept(this, data);
		}
		sb.append(node.arithmeticExpression1);
		if (node.relOp != null) {
			sb.append(" " + node.relOp + " ");
			sb.append(node.arithmeticExpression2);
		}
		return sb.toString();
	}

	public Object visit(OMArithmeticExpression node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.prefixOp != null) {
			sb.append(node.prefixOp);
		}
		sb.append(node.term.jjtAccept(this, data));
		int i = 0;
		for (String op : node.addOps) {
			sb.append(" " + op + " ");
			sb.append(node.rhsTerms.get(i++).jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Object visit(OMTerm node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.factor.jjtAccept(this, data));
		int i = 0;
		for (String op : node.mulOps) {
			sb.append(" " + op + " ");
			sb.append(node.rhsFactors.get(i++).jjtAccept(this, data));
		}
		// System.err.println(node.doubleValue);
		return sb.toString();
	}

	public Object visit(OMFactor node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.primary.jjtAccept(this, data));

		if (node.op != null) {
			sb.append(" " + node.op + " ");
			sb.append(node.rhsPrimary.jjtAccept(this, data));
		}

		return sb.toString();
	}

	public Object visit(OMPrimary node, Object data) {
		StringBuilder sb = new StringBuilder();
		ClassDef cd = (ClassDef) data;
		if (node.image != null) {
			sb.append(node.image);
		} else if (node.funcName != null) {
			sb.append(node.funcName + node.funcCallArgs.jjtAccept(this, null));
		} else if (node.componentRef != null) {
			sb.append(node.componentRef.jjtAccept(this, null));
		} else if (node.outputExpressionList != null) {
			sb.append("(");
			int i = 0;
			for (OMExpression exp : node.outputExpressionList) {
				if (i++ > 0) {
					sb.append(", ");
				}
				sb.append(exp.jjtAccept(this, null));
			}
			sb.append(")");
		} else if (node.expressionLists.size() > 0) {
			sb.append("[");
			int i = 0;
			for (List<OMExpression> list : node.expressionLists) {
				if (i++ > 0) {
					sb.append("; ");
				}
				int j = 0;
				for (OMExpression exp : list) {
					if (j++ > 0) {
						sb.append(", ");
					}
					sb.append(exp.jjtAccept(this, null));
				}
			}
			sb.append("]");
		} else if (node.funcArgs != null) {
			sb.append("{" + node.funcArgs.jjtAccept(this, null) + "}");
		}
		return sb.toString();
	}

	public Object visit(OMComponentRef node, Object data) {
		ComponentRef componentRef = new ComponentRef(node);
		componentRef.ident = (StringProperty) PropertyFactory.createProperty(
				"name", node);
		// TODO Handle arrays
		if (node.next != null) {
			componentRef.componentRef = (ComponentRef) node.next.jjtAccept(
					this, null);
		}
		return componentRef;
	}

	public Object visit(OMFunctionCallArgs node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (node.funcArgs != null) {
			sb.append(node.funcArgs.jjtAccept(this, null));
		}
		sb.append(")");
		return sb.toString();
	}

	public Object visit(OMFunctionArguments node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.expression != null) {
			sb.append(node.expression.jjtAccept(this, null));
			if (node.next != null) {
				sb.append(", " + node.next.jjtAccept(this, null));
			} else if (node.forIndices != null) {
				sb.append("for " + node.forIndices.jjtAccept(this, null));
			}
		} else if (node.namedArguments != null) {
			int i = 0;
			for (OMNamedArgument na : node.namedArguments) {
				if (i++ > 0) {
					sb.append(", ");
				}
				sb.append(na.jjtAccept(this, null));
			}
		}
		return sb.toString();
	}

	public Object visit(OMNamedArgument node, Object data) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.name + " = " + node.expression.jjtAccept(this, null));
		return sb.toString();
	}

	public Object visit(OMSubscript node, Object data) {
		StringBuilder sb = new StringBuilder();
		if (node.sep) {
			sb.append(" : ");
		} else {
			sb.append(node.expression.jjtAccept(this, null));
		}
		return sb.toString();
	}

	public Comment visit(OMComment node, Object data) {
		ClassDef classDef = (ClassDef) data;

		Comment comment = new Comment(node);
		comment.string = (StringProperty) PropertyFactory.createProperty(
				"string", node);
		if (node.annotation != null) {
			Annotation a = new Annotation(node.annotation);
			if (node.annotation.classModification != null
					&& node.annotation.classModification.argList != null
					&& node.annotation.classModification.argList.argList != null) {
				for (OMArgument arg : node.annotation.classModification.argList.argList) {
					AnnotationParser.parseConAnnotation(arg, a);
				}
				comment.annotation = a;
			}
		}
		return comment;
	}

	// This thing needs cleaning up
	// Rather than hardcoding locations of annotation blocks, there should be
	// some sort of auto-loading, where each nested block is inspected and
	// finally the value is stored as a StringProperty. E.g.
	// Documentation(Icon(...)) should be put in annotations map as
	// ("Documentation.Icon", StringProperty)
	public Annotation visit(OMAnnotation node, Object data) {
		Annotation annotation = new Annotation(node);
		NodeEvent root;
		Stack<NodeEvent> nestack = new Stack<NodeEvent>();
		root = nodeEvent = new NodeEvent("classModification",
				OMClassModification.class);
		chainEvent(new NodeEvent("argList", OMArgumentList.class));
		chainEvent(new NodeEvent("argList", List.class));
		chainEvent(new NodeEvent("elementModificationOrReplaceable",
				OMElementModificationOrReplaceable.class));
		chainEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("Documentation");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		// Documentation
		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		chainEvent(new NodeEvent("classModification", OMClassModification.class));
		chainEvent(new NodeEvent("argList", OMArgumentList.class));
		chainEvent(new NodeEvent("argList", List.class));
		chainEvent(new NodeEvent("elementModificationOrReplaceable",
				OMElementModificationOrReplaceable.class));

		// Info
		chainEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("info");
			}
		};

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		chainEvent(new NodeEvent("expression", OMExpression.class));
		chainEvent(new NodeEvent("simpleExpression", OMSimpleExpression.class));
		chainEvent(new NodeEvent("logicalExpression", OMLogicalExpression.class));
		chainEvent(new NodeEvent("logicalTerms", List.class));
		chainEvent(new NodeEvent("logicalFactors", List.class));
		chainEvent(new NodeEvent("relation", OMRelation.class));
		chainEvent(new NodeEvent("arithmeticExpression1",
				OMArithmeticExpression.class));
		chainEvent(new NodeEvent("term", OMTerm.class));
		chainEvent(new NodeEvent("factor", OMFactor.class));
		chainEvent(new NodeEvent("primary", OMPrimary.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMPrimary p = (OMPrimary) obj;
				Annotation a = (Annotation) data;
				a.map.put("info", p);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// Placement
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("Placement");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;

				AnnotationParser.parsePlacement(m, a);
				// a.map.put("Placement", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// Coordsys
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("Coordsys");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				a.map.put("Coordsys", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// Icon
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("Icon");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				AnnotationParser.parseIcon(m, a, "Icon");
				// a.map.put("Icon", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// Diagram
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("Diagram");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				AnnotationParser.parseDiagram(m, a, "Diagram");
				a.map.put("Diagram", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// RunConfigurations
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("RunConfigurations");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				a.map.put("RunConfigurations", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// LibFiles
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("LibFiles");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				a.map.put("LibFiles", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// PrimitiveGeometry
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("PrimitiveGeometry");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				a.map.put("PrimitiveGeometry", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// LibFiles
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				if (em.componentRef.name.equals("LinkMap")) {
					System.err.println("LinkMap");
				}
				return em.componentRef.name.equals("LinkMap");
			}
		};

		// Push node event #1
		nestack.push(nodeEvent);

		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMModification m = (OMModification) obj;
				Annotation a = (Annotation) data;
				a.map.put("LinkMap", m);
				return false;
			}
		};

		// Pop node event #1
		nodeEvent = nestack.pop();

		// Extent
		appendEvent(new NodeEvent("elementModification",
				OMElementModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				OMElementModification em = (OMElementModification) obj;
				return em.componentRef.name.equals("extent");
			}
		};
		chainEvent(new NodeEvent("componentRef", OMComponentRef.class));
		appendEvent(new NodeEvent("modification", OMModification.class));
		nodeEvent.action = new Action() {
			public boolean execute(Object obj, NodeEvent nodeEvent, Object data) {
				final OMModification m = (OMModification) obj;
				Annotation ann = (Annotation) data;
				if (m.expression != null) {
					ann.map.put("extent", m);
				}
				return false;
			}
		};

		walkTree(node, root, annotation);
		return annotation;
	}

	private NodeEvent nodeEvent;

	private void chainEvent(NodeEvent e) {
		nodeEvent.childEvent = e;
		nodeEvent = e;
	}

	private void appendEvent(NodeEvent e) {
		nodeEvent.nextEvent = e;
		nodeEvent = e;
	}

	private static void walkTree(Object obj, NodeEvent nodeEvent, Object data) {
		try {
			Field f = obj.getClass().getField(nodeEvent.fieldName);
			if (nodeEvent.fieldClass.isAssignableFrom(f.getType())) {
				Object fdObj = f.get(obj);
				if (fdObj != null
						&& (nodeEvent.action == null || nodeEvent.action
								.execute(fdObj, nodeEvent, data))) {
					if (nodeEvent.fieldClass.equals(Class
							.forName("java.util.List"))) {
						List list = (List) fdObj;
						for (Object item : list) {
							if (nodeEvent.childEvent != null) {
								walkTree(item, nodeEvent.childEvent, data);
							}
						}
					} else if (nodeEvent.fieldClass.isArray()) {
						Object[] array = (Object[]) fdObj;
						for (Object item : array) {
							if (nodeEvent.childEvent != null) {
								walkTree(item, nodeEvent.childEvent, data);
							}
						}
					} else {
						if (fdObj != null) {
							if (nodeEvent.childEvent != null) {
								walkTree(fdObj, nodeEvent.childEvent, data);
							}
						}
					}
				} else if (nodeEvent.nonAction != null) {
					nodeEvent.nonAction.execute(f.get(obj), nodeEvent, data);
				}
				if (nodeEvent.nextEvent != null) {
					walkTree(obj, nodeEvent.nextEvent, data);
				}
			}
		} catch (Exception e) {
			// Terminate walk
			e.printStackTrace();
		}
	}

	public static class NodeEvent {
		public String fieldName;

		public Class fieldClass;

		public NodeEvent nextEvent;

		public NodeEvent childEvent;

		public Action action;

		public Action nonAction;

		public NodeEvent(String fieldName, Class fieldClass) {
			this.fieldName = fieldName;
			this.fieldClass = fieldClass;
		}

		public static interface Action {
			/**
			 * 
			 * @param obj
			 * @param ne
			 * @param data
			 * @return true if we should continue walking the tree, false
			 *         otherwise
			 */
			public boolean execute(Object obj, NodeEvent ne, Object data);
		}
	}
}

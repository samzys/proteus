package proteus.gwt.shared.modelica;

import java.util.LinkedList;
import java.util.List;

import proteus.gwt.shared.modelica.ClassDef.Annotation;
import proteus.gwt.shared.modelica.ClassDef.Argument;
import proteus.gwt.shared.modelica.ClassDef.Comment;
import proteus.gwt.shared.modelica.ClassDef.ComponentClause;
import proteus.gwt.shared.modelica.ClassDef.ComponentDecl;
import proteus.gwt.shared.modelica.ClassDef.ComponentRef;
import proteus.gwt.shared.modelica.ClassDef.ConnectClause;
import proteus.gwt.shared.modelica.ClassDef.Element;
import proteus.gwt.shared.modelica.ClassDef.Equation;
import proteus.gwt.shared.modelica.ClassDef.Expression;
import proteus.gwt.shared.modelica.ClassDef.Modification;
import proteus.gwt.shared.modelica.parser.ModelicaParserConstants;
import proteus.gwt.shared.modelica.parser.OMAlgorithmSection;
import proteus.gwt.shared.modelica.parser.OMAnnotation;
import proteus.gwt.shared.modelica.parser.OMArgument;
import proteus.gwt.shared.modelica.parser.OMClassDefinition;
import proteus.gwt.shared.modelica.parser.OMClassSpecifier;
import proteus.gwt.shared.modelica.parser.OMComment;
import proteus.gwt.shared.modelica.parser.OMComponentClause;
import proteus.gwt.shared.modelica.parser.OMComponentDecl;
import proteus.gwt.shared.modelica.parser.OMComponentList;
import proteus.gwt.shared.modelica.parser.OMComposition;
import proteus.gwt.shared.modelica.parser.OMDeclaration;
import proteus.gwt.shared.modelica.parser.OMElement;
import proteus.gwt.shared.modelica.parser.OMElementList;
import proteus.gwt.shared.modelica.parser.OMElementModification;
import proteus.gwt.shared.modelica.parser.OMElementModificationOrReplaceable;
import proteus.gwt.shared.modelica.parser.OMEquation;
import proteus.gwt.shared.modelica.parser.OMEquationSection;
import proteus.gwt.shared.modelica.parser.OMModification;
import proteus.gwt.shared.modelica.parser.OMStoredDefinition;
import proteus.gwt.shared.modelica.parser.OMSubscript;
import proteus.gwt.shared.modelica.parser.OMTypePrefix;
import proteus.gwt.shared.modelica.parser.SimpleNode;
import proteus.gwt.shared.modelica.parser.OMExpression.OMExpressionPtr;
import proteus.gwt.shared.types.StringProperty;

public class ModelicaLoader extends ModelicaParserVisitorAdapter implements
		ModelicaParserConstants {

	private NodeEvent nodeEvent;

	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public ClassDef[] visit(OMStoredDefinition node, Object data) {
		List<ClassDef> classDefs = new LinkedList<ClassDef>();
		for (OMClassDefinition ocd : node.classDefinitions) {
			Object[] d = new Object[3];
			d[0] = node.name; // name
			// d[1] = (String) data; // path
			// d[2] = Scope.getInvisibleScope(); // scope
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
		// String path = (String) d[1];

		ClassDef classDef;
		// if (node.restriction.equals(Constants.TYPE_PACKAGE)) {
		// classDef = new Library.Category(node);
		// } else {
		classDef = new Component(node);
		// }
		classDef.type = withinName == null || withinName.length() <= 0 ? ""
				: withinName + ".";
		classDef.final_ = false;
		classDef.encapsulated = false;
		classDef.partial = false;
		classDef.restriction = node.restriction;

		// classDef.sourceFilePath = path;
		// classDef.scope = new Scope(lastScope);

		classDef = (ClassDef) node.classSpecifier.jjtAccept(this, classDef);

		// // set the extendsName for drawing the Icon using extend clause
		// //12 Jan , 2011 Sam
		// for (ExtendsClause ext : classDef.extendsList) {
		// // to specify short class using extends clause
		// // short class definition.
		// if (classDef.extendsList.size() == 1) {
		// if (ext != null) {
		// classDef.refName = (String) ext.name.get();
		// classDef.extendsName = classDef.refName;
		// }
		// }
		// }
		return classDef;
	}

	public Object visit(final OMClassSpecifier node, Object data) {
		ClassDef classDef = (ClassDef) data;
		// @1 May, 2011.
		classDef.name = new StringProperty(node.name) {
			public void setValue(String value) {
				super.setValue(value);
				node.name = value;
			}
		};
		classDef.type += classDef.name.getValue();
		classDef.description = new StringProperty(node.description) {
			public void setValue(String value) {
				super.setValue(value);
				node.description = value;
			}
		};

		switch (node.selection) {
		case 0:
			node.composition.jjtAccept(this, classDef);
			break;
		case 1:
			node.typePrefix.jjtAccept(this, classDef);
			// classDef.refName = node.refName;
			node.comment.jjtAccept(this, classDef);
			if (node.arraySubs != null) {
				List<String> li = new LinkedList<String>();
				int i = 0;
				for (OMSubscript s : node.arraySubs) {
					li.add((String) s.jjtAccept(this, null));
				}
				// classDef.arraySubs = li.toArray(new String[0]);
			}
			if (node.classModification != null) {
				// classDef.argList = (List<Argument>) node.classModification
				// .jjtAccept(this, classDef);
			}
			// add it in extend List for resolving
			// classDef.shortClass = true;// specify short class tag
			break;
		case 2:
			// classDef.refName = "enumeration";
			// MEnumeration me = new MEnumeration();
			// me.enumContent = (Map<String, String>) node.omEnumlist.jjtAccept(
			// this, data);
			// me.comment = node.comment.string;
			// classDef.primitiveType = me;
			break;
		case 3:
			// classDef.refName = "der";
			break;
		case 4:
			// classDef.extends_ = true;
			// classDef.shortClass = true;
			// classDef.refName = (String) classDef.name.get();

			if (node.classModification != null)
				// classDef.argList = (List<Argument>) node.classModification
				// .jjtAccept(this, classDef); // only one
				// classsmodification is
				// allowed at class
				// specifier section
				node.composition.jjtAccept(this, classDef);// return null, all
			// data
			// set inside classDef
			break;
		default:
			break;
		}

		return classDef;
	}

	public Element visit(final OMElement node, Object data) {
		ClassDef classDef = null;
		if (data instanceof ClassDef) {
			classDef = (ClassDef) data;
		}
		Element element = null;
		if (node.importClause != null) {
			element = new Element(node);
		} else if (node.componentClause != null) {
			element = new Element(node);
			ComponentClause compCluase = (ComponentClause) node.componentClause
					.jjtAccept(this, classDef);
			element.compClause = compCluase;
			
			element.finalProperty = new StringProperty(node.final_) {
				public void setValue(String value) {
					super.setValue(value);
					node.final_ = value;
				}
			};
			element.redeclareProperty = new StringProperty(node.redeclare) {
				public void setValue(String value) {
					super.setValue(value);
					node.redeclare= value;
				}
			};
			element.typingProperty = new StringProperty(node.typing) {
				public void setValue(String value) {
					super.setValue(value);
					node.typing = value;
				}
			};
			element.replaceableProperty = new StringProperty(node.replaceable) {
				public void setValue(String value) {
					super.setValue(value);
					node.replaceable = value;
				}
			};
			element.protectedProperty = new StringProperty(node.protected_) {
				public void setValue(String value) {
					super.setValue(value);
					node.protected_ = value;
				}
			};
		}

		return element;
	}

	public ComponentClause visit(final OMComponentClause node, Object data) {
		ComponentClause cc = new ComponentClause(node);

		OMComponentList omcL = (OMComponentList) node.componentList;
		for (OMComponentDecl cd : omcL.componentDecls) {
			ComponentDecl compDecl = new ComponentDecl();
			final OMDeclaration omd = cd.getDecl();
			compDecl.varName = new StringProperty(omd.name) {
				public void setValue(String value) {
					super.setValue(value);
					omd.name = value;
				}
			};
			OMDeclaration d = cd.decl;
			if (d.modification != null) {
				OMModification omm = d.modification;
				compDecl.modification = (Modification) omm.jjtAccept(
						ModelicaLoader.this, null);
			}
			OMComment c = cd.comment;
			compDecl.comment = (Comment) c.jjtAccept(ModelicaLoader.this, null);
			cc.declList.add(compDecl);
		}
		if (node.typePrefix != null) {
			cc.flow_stream = node.typePrefix.flow_stream;
			cc.causality = new StringProperty(node.typePrefix.causality) {
				@Override
				public void setValue(String value) {
					super.setValue(value);
					node.typePrefix.causality = value;
				}
			};
			cc.variability = new StringProperty(node.typePrefix.variability) {
				@Override
				public void setValue(String value) {
					super.setValue(value);
					node.typePrefix.variability = value;
				}
			};
		}
		return cc;
	}

	public Object visit(final OMModification node, Object data) {
		Modification md = new Modification(node);

		if (node.classModification != null) {
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

			if (!(node.expression instanceof OMExpressionPtr)) {
				node.expression = new OMExpressionPtr(node.expression);
			}
			md.expression = (Expression) node.expression.jjtAccept(
					ModelicaLoader.this, data); // 11 Nov, 2010 sam
		}
		return md;
	}

	public Object visit(OMComposition node, Object data) {
		ClassDef classDef = (ClassDef) data;
		Object[] d = new Object[2];
		d[0] = classDef;
		d[1] = "public";
		node.elementList.jjtAccept(this, d);

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
		return null;
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

	public Object visit(OMEquationSection node, Object data) {
		ClassDef classDef = (ClassDef) data;
		for (OMEquation e : node.equations) {
			classDef.equations.add((Equation) e.jjtAccept(this, null));
		}
		return null;
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

	public Comment visit(OMComment node, Object data) {
		ClassDef classDef = (ClassDef) data;

		Comment comment = new Comment(node);
		comment.string = node.string;

		if (node.annotation != null) {
			Annotation ann = (Annotation) node.annotation.jjtAccept(this, null);
			comment.annotation = ann;
			// if (classDef != null)
			// classDef.annotations.add(ann);
		}
		return comment;
	}

	public Annotation visit(OMAnnotation node, Object data) {
		// @sam.
		Annotation annotation = new Annotation(node);
		try {
			for (OMArgument arg : node.classModification.argList.argList) {
				AnnotationParser.parseConAnnotation(arg, annotation);
			}
		} catch (NullPointerException e) {
			// do nothing just return;
			System.err.println("Null Pointer in Annotation Parser!");
		}

		return annotation;
	}

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

			if (nodeEvent.action == null
					|| nodeEvent.action.execute(obj, nodeEvent, data)) {
				if (nodeEvent.fieldClass.equals(List.class)) {
					List list = (List) obj;
					for (Object item : list) {
						if (nodeEvent.childEvent != null) {
							walkTree(item, nodeEvent.childEvent, data);
						}
					}

				} else if (nodeEvent.fieldClass.isArray()) {

				} else {
					if (nodeEvent.childEvent != null) {
						walkTree(obj, nodeEvent.childEvent, data);
					}
				}

				if (nodeEvent.nextEvent != null) {
					walkTree(obj, nodeEvent.nextEvent, data);
				}
			}
			// Field f = obj.getClass().getField(nodeEvent.fieldName);
			// if (nodeEvent.fieldClass.isAssignableFrom(f.getType())) {
			// Object fdObj = f.get(obj);
			// if (fdObj != null
			// && (nodeEvent.action == null || nodeEvent.action
			// .execute(fdObj, nodeEvent, data))) {
			// if (nodeEvent.fieldClass.equals(Class
			// .forName("java.util.List"))) {
			// List list = (List) fdObj;
			// for (Object item : list) {
			// if (nodeEvent.childEvent != null) {
			// walkTree(item, nodeEvent.childEvent, data);
			// }
			// }
			// } else if (nodeEvent.fieldClass.isArray()) {
			// Object[] array = (Object[]) fdObj;
			// for (Object item : array) {
			// if (nodeEvent.childEvent != null) {
			// walkTree(item, nodeEvent.childEvent, data);
			// }
			// }
			// } else {
			// if (fdObj != null) {
			// if (nodeEvent.childEvent != null) {
			// walkTree(fdObj, nodeEvent.childEvent, data);
			// }
			// }
			// }
			// } else if (nodeEvent.nonAction != null) {
			// nodeEvent.nonAction.execute(f.get(obj), nodeEvent, data);
			// }
			// if (nodeEvent.nextEvent != null) {
			// walkTree(obj, nodeEvent.nextEvent, data);
			// }
			// }
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

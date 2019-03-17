package com.infoscient.proteus.modelica;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.infoscient.proteus.Constants;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.Argument;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.Expression;
import com.infoscient.proteus.modelica.ClassDef.ExtendsClause;
import com.infoscient.proteus.modelica.ClassDef.ImportClause;
import com.infoscient.proteus.modelica.ClassDef.Modification;
import com.infoscient.proteus.modelica.Component.ComplexComponent;
import com.infoscient.proteus.modelica.Component.Parameter;
import com.infoscient.proteus.modelica.Component.Result;
import com.infoscient.proteus.modelica.calculator.CalculatorExp;
import com.infoscient.proteus.modelica.calculator._Start;
import com.infoscient.proteus.modelica.parser.OMExpression;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.types.MBoolean;
import com.infoscient.proteus.modelica.types.MEnumeration;
import com.infoscient.proteus.modelica.types.MInteger;
import com.infoscient.proteus.modelica.types.MPrimitive;
import com.infoscient.proteus.modelica.types.MReal;
import com.infoscient.proteus.modelica.types.MStateSelect;
import com.infoscient.proteus.modelica.types.MString;

public class RefResolver implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Add primitive types
	public static final Set<String> primitiveSet = new HashSet<String>();
	public static CalculatorExp ct = new CalculatorExp();

	static {
		primitiveSet.add("Real");
		primitiveSet.add("Integer");
		primitiveSet.add("Boolean");
		primitiveSet.add("String");
		primitiveSet.add("StateSelect");
		primitiveSet.add("enumeration");
	}

	public static Set<String> scopedRefSet = new HashSet<String>();

	private static TreeNode libRoot;
	private static FileOutputStream out;
	private static PrintStream ps;
	private static BufferedWriter bw;
	private static BufferedWriter bw1;

	private static MPrimitive resolveArgs(List<Argument> args, MPrimitive mp) {
		if (args != null) {
			for (Argument arg : args) {
				String argName = arg.name;
				Modification md = arg.modification;
				Expression exp = md == null ? null : md.expression;
				String updatedValue = (exp == null) ? null : (String) exp.value
						.get();

				// remove the double quta sign""
				if (updatedValue.startsWith("\"")
						&& updatedValue.endsWith("\"")) {
					updatedValue = updatedValue.substring(1, updatedValue
							.length() - 1);
				}
				mp.updateFields(argName, updatedValue);
			}
		}
		return mp;
	}

	private static void resolveType(ClassDef cd, String cdRef) {
		if (!cdRef.startsWith(Constants.MODELICA_LIB_PREFIX)) {
			int i = cd.type.lastIndexOf(".");
			if (i > 0)
				cdRef = cd.type.substring(0, i + 1) + cdRef;
		}// look up inside the file only
		ClassDef compClassDef = RefResolver.forName(cdRef);
		if (compClassDef != null) {
			if (compClassDef.primitiveType != null
					&& compClassDef.primitiveType instanceof MPrimitive) { // if
				// the depth is only 1
				cd.primitiveType = compClassDef.primitiveType;
			} else {// solve for depth more than 1;
				resolveType(cd, compClassDef.refName);
			}
		}
	}

	public static String checkDoubleType(List<Parameter> paramList,
			String defaultValue) {
		if (defaultValue.length() > 0)
			try {
				Double.parseDouble(defaultValue);
			} catch (Exception e) {
				for (Parameter p : paramList) {
					if (p.name.equals(defaultValue)) {
						defaultValue = p.defaultValue;
						checkDoubleType(paramList, defaultValue);
						break;
					}
				}
			}

		return defaultValue;
	}

	public static boolean resolveRefs(TreeNode libRoot) {
		// a new way to resovle reference
//		try {
//			bw = new BufferedWriter(new FileWriter(
//					new File("ComponentList.txt"), true));
//			bw1 = new BufferedWriter(new FileWriter(
//					new File("PackageList.txt"), true));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		RefResolver.libRoot = libRoot;
		nameLookup1(libRoot);
		nameLookup2(libRoot);
		//
		nameLookup3(libRoot);
		// generateImageIcon(libRoot);
//		try {
//			bw.close();
//			bw1.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// System.err.println("Write successfully");
		return true;
	}


	public static void _nameLookup3(ClassDef cd){
		
		// 19 Jan, 2011 sam lookup for connectors
		// cd._connectors = connectorLookup(cd);
		for (ComponentClause comp : cd.compList) {
			// ignore those with protected tag
			if (comp.protected_.get()!=null&&comp.protected_.get().equals("protected")) {
				// protected component declaration not need to be visiable by
				// children.
				continue;
			}

			String compName = (String) comp.typeName.get();

			if (!isComplexType(cd, comp)) {
				// all primitive types;
				String p = checkPrimitiveType(cd, comp);
				if (p == null) {
					System.err.println("Alert...." + cd.type + "#"
							+ comp.typeName.get());
				}
			}
		}
	}

	private static void nameLookup3(TreeNode node) {
		// for declration
		ClassDef cd = getClassDef(node);

//		primitive type check up
		_nameLookup3(cd);
		// solve childrean
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode childNode = node.getChildAt(i);
			nameLookup3(childNode);
		}
	}

	private static List<ComplexComponent> ccLookup(ClassDef cd) {

		return null;
	}

	private static Connector[] connectorLookup(ClassDef cd) {
		List<Connector> connList = new LinkedList<Connector>();

		for (ComponentClause comp : cd.compList) {
			String compName = (String) comp.typeName.get();
			if (isPrimitive(compName))
				continue;
			if (!isComplexType(cd, comp))
				continue;

			ClassDef compCD = getClassDef(compName);

			for (ComponentDecl compdecl : comp.declList) {
				if (isConnector(compCD)) {
					connList.add(new Connector(compdecl.varName, compCD, cd));
				}
				// else if (compCD._connectors != null
				// && compCD._connectors.length > 0) {
				// connList.add(new Connector(compdecl.varName, compCD, cd));
				// }
			}
		}
		for (ExtendsClause ext : cd.extendsList) {
			if (isPrimitive((String) ext.name.get()))
				continue;
			ClassDef superCD = getClassDef((String) ext.name.get());
			connList.addAll(Arrays.asList(connectorLookup(superCD)));
		}
		return connList.toArray(new Connector[0]);
	}

	private static boolean isPrimitive(String name) {
		if (primitiveSet.contains(name))
			return true;
		return false;
	}

	private static boolean isConnector(ClassDef compCD) {
		if (compCD.restriction.equals(Constants.TYPE_CONNECTOR)) {
			return true;
		}
		return false;
	}

	private static boolean isComplexType(ClassDef cd, ComponentClause comp) {
		String typeName = (String) comp.typeName.get();
		if (primitiveSet.contains(typeName)) {
			return false;
		}
		ClassDef c = getClassDef(typeName);
		if (c.restriction.equals(Constants.TYPE_TYPE)) {
			return false;
		}
		return true;
	}

	private static MPrimitive getPrimitiveType(String ptype) {
		if (ptype == null)
			return null;
		if (ptype.equals("Real")) {
			MReal m = new MReal();
			return m;
		} else if (ptype.equals("Boolean")) {
			return new MBoolean();
		} else if (ptype.equals("Integer")) {
			return new MInteger();
		} else if (ptype.equals("String")) {
			return new MString();
		} else if (ptype.equals("StateSelect")) {
			return new MStateSelect();
		} else if (ptype.equals("enumeration")) {
			return new MEnumeration();
		} else
			return null;

	}

	private static String getModificationUnit(ClassDef cd,
			ComponentClause comp, ComponentDecl compdecl) {
		String value = null;
		if (compdecl.modification == null)
			return null;

		if (primitiveSet.contains(comp.typeName.get())) {
			List<Argument> argList = compdecl.modification.arguments;
			if (argList != null && argList.size() > 0) {
				for (Argument a : argList) {
					if (a.name.equals("unit")) {
						value = (String) a.modification.expression.value.get();
						return value;
					}// only check start for now
				}
			}// end of checking arguments

		} else {
			// check if it is complex component.

		}

		if (compdecl.modification.expression != null) {
			value = (String) compdecl.modification.expression.value.get();
			ComponentDecl decl = getCompDeclbyName(cd, value);
			// if(decl!=null){
			// return getModificationUnit(cd, decl);
			// }

		}// end of checking expression
		return null;
	}

	private static ComponentDecl getCompDeclbyName(ClassDef cd, String value) {
		// check inside the classdef first
		for (ComponentClause comp : cd.compList) {
			for (ComponentDecl decl : comp.declList) {
				if (decl.varName.get().equals(value)) {
					return decl;
				}
			}
		}
		return null;
	}

	private static _Start withVariable(ClassDef cd, String modification) {
		Map<String, DoubleRecord> s = CalculatorExp
				.simpleVariables(modification);

		if (modification == null) {
			// no equal modification found;
			return null;
		}

		// replace the variables
		if (s != null && s.size() > 0) {
			modification = variableLookup(cd, s, modification);
		}
		if (modification == null) {

			System.err.println("not solved by calculator");
			return null;
		}

		_Start sList = CalculatorExp.simpleNumberCal(modification);
		return sList;
	}

	private static void writeOutput(BufferedWriter bbw, String modification) {
		try {
			bbw.append(modification + "\n");
			bbw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String functionLookup(ClassDef cd,
			Map<String, DoubleRecord> s, String modification) {
		for (Map.Entry<String, DoubleRecord> e : s.entrySet()) {
			_Start sList = withVariable(cd, e.getValue().valList.get(0)
					.toString());
			if (sList == null)
				return modification;
			System.out.println(sList.doubleList.get(0));

			if (isModelicaPrefix(e.getKey())) {
				ClassDef fcd = getClassDef(e.getKey());

				String d = fcd.getValue(sList.doubleList.get(0)).toString();
				String orgString = e.getKey() + "("
						+ e.getValue().valList.get(0) + ")";
				modification = modification.replace(orgString, d);
				System.out.println(modification);
				return modification;
			} else {
				// not modeilica prefix, look up inside
				System.err.println(modification);
				return modification;
			}
		}

		return null;
	}

	private static String variableLookup(ClassDef cd,
			Map<String, DoubleRecord> s, String modification) {
		if (modification.contains("if") || modification.contains("then")) {
			return null;
		}

		for (Map.Entry<String, DoubleRecord> e : s.entrySet()) {
			String mod = null;

			if (isModelicaPrefix(e.getKey())) {
				String key = e.getKey();
				mod = getClassVal(key, modification);

			} else {
				mod = DeclExtLookup(cd, e.getKey(), modification);
			}

			if (mod == null || mod.equals(modification)) {
				return null;
			}
			modification = mod;
		}// eof map

		return modification;
	}

	private static String getClassVal(String key, String modification) {
		StringTokenizer st = new StringTokenizer(key, ".");

		String token = null;
		TreeNode node = libRoot;
		TreeNode pnode = node;
		while (node != null && st.hasMoreTokens()) {
			token = st.nextToken();
			pnode = node;
			node = checkInLowerScope(node, token);
		}
		ClassDef cd = getClassDef(pnode);
		for (ComponentClause cc : cd.compList) {
			for (ComponentDecl compdecl : cc.declList) {
				if (compdecl.varName.get().equals(token)) {
					// deal with one first;
					for (Double d : compdecl.valList) {
						modification = modification.replace(key, d.toString());
						// System.out.println(modification);
					}// for valList;
					return modification;
				}// end if
			}// end compdecl
		}// eof parameter

		return null;
	}

	private static String DeclExtLookup(ClassDef cd, String s,
			String modification) {

		for (ComponentClause cc : cd.compList) {
			for (ComponentDecl compdecl : cc.declList) {
				if (compdecl.varName.get().equals(s)) {
					// deal with one first;
					for (Double d : compdecl.valList) {
						modification = modification.replace(s, d.toString());
						// System.out.println(modification);
					}// for valList;
					return modification;
				}// end if
			}// end compdecl
		}// eof parameter

		for (ExtendsClause ext : cd.extendsList) {
			String n = DeclExtLookup(getClassDef((String) ext.name.get()), s,
					modification);
			if (n != null)
				return n;
		}
		return null;
	}

	private static String getModificationValue(ComponentDecl compdecl) {
		String value = null;
		List<Argument> argList = compdecl.modification.arguments;
		if (argList != null && argList.size() > 0) {
			for (Argument a : argList) {
				if (a.name.equals("start")) {
					value = (String) a.modification.expression.value.get();
					return value;
				}// only check start for now
			}
		}// end of checking arguments

		if (compdecl.modification.expression != null) {
			value = (String) compdecl.modification.expression.value.get();
			return value;
		}// end of checking expression
		return null;
	}

	private static void nameLookup2(TreeNode node) {
		// solve the componenent reference;
		ClassDef cd = getClassDef(node);
		for (ComponentClause comp : cd.compList) {
			String compName = (String) comp.typeName.get();
			compName = compNameLookup(compName, node);
			comp.typeName.set(compName);
			isModelicaPrefix(compName);
		}
		// solve childrean
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode childNode = node.getChildAt(i);
			nameLookup2(childNode);
		}
	}

	private static boolean nameLookup1(TreeNode node) {
		ClassDef cd = getClassDef(node);
		// solve extends clause
		boolean setExt = false; 
		for (ExtendsClause ext : cd.extendsList) {
			String extName = (String) ext.name.get();
			extName = extNameLookup(extName, node);
			ext.name.set(extName);
			if (!setExt) {
				cd.extendsName = extName;
				setExt = true;
			}
			if (cd.extendsList.size() == 1) {
				cd.refName = cd.extendsName;
			}
		}

		if (cd.shortClass) {
			String ref = shortNameLookup(cd.refName, node);
			cd.refName = ref;
			isModelicaPrefix(cd, ref);
			if (cd.extends_)
				cd.extendsName = ref;
		}

		// solve childrean
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode childNode = node.getChildAt(i);
			nameLookup1(childNode);
		}
		return true;
	}

	private static String checkPrimitiveType(ClassDef cd, ComponentClause comp) {
		String compName = (String) comp.typeName.get();

		if (primitiveSet.contains(compName)) {
			// check unit,.... solve for declarations:
			String ptype = compName;
			MPrimitive m = getPrimitiveType(ptype);
			storeParameters(compName, cd, comp, m);
			return compName;
		} else {
			ClassDef cd_ = getClassDef(compName);
			Stack<List<Argument>> stack = new Stack<List<Argument>>();
			if (cd_.argList != null && cd_.argList.size() > 0) {
				stack.push(cd_.argList);
			}
			if (cd_.restriction.equals(Constants.TYPE_TYPE)) {
				String name = cd_.refName;
				while (name != null && !primitiveSet.contains(name)) {
					cd_ = getClassDef(name);
					if (cd_ == null) {
						System.err.println("Cannot find exception");
						break;
					}
					if (cd_.argList != null && cd_.argList.size() > 0) {
						stack.push(cd_.argList);
					}
					name = cd_.refName;
				}
				// solve the stack.
				MPrimitive mp = getPrimitiveType(name);
				while (!stack.empty()) {
					List<Argument> listA = stack.pop();
					mp = resolveArgs(listA, mp);
				}
				storeParameters(name, cd, comp, mp);

				return name;
			} else {
				return null;
			}
		}
	}

	private static void storeParameters(String compName, ClassDef cd,
			ComponentClause comp, MPrimitive m) {
//		if (cd.type
//				.equals("Modelica.Mechanics.MultiBody.Examples.Loops.Utilities.Cylinder_analytic_CAD")) {
//			// System.out.println();
//		}
		for (ComponentDecl compdecl : comp.declList) {
			String decName = (String) compdecl.varName.get();
			String wholename = (String) comp.typeName.get();
			String desc = (String) compdecl.comment.string.get();
			// String start= null;
			String defaultValue = null;

			if (compdecl.modification != null) {
				Expression exp = compdecl.modification.expression;
				defaultValue = (exp == null) ? null : (String) exp.value.get();

				List<Argument> argList = compdecl.modification.arguments;
				if (argList != null && argList.size() > 0) {
					m = resolveArgs(compdecl.modification.arguments, m);
				}
			}

			String annotationGroup = null;
			String annotationTab = null;
			String annotationInitDialog = null;

			if (compdecl.comment != null) {
				Annotation a = compdecl.comment.annotation;
				if (a != null) {
					Map annMap = a.map;
					if (annMap != null) {
						OMModification omm = (OMModification) annMap
								.get("Dialog.group");
						OMModification omTab = (OMModification) annMap
								.get("Dialog.tab");
						OMModification omInit = (OMModification) annMap
								.get("Dialog.__Dymola_initialDialog");

						if (omm != null) {
							OMExpression exp = omm.expression;
							annotationGroup = (exp == null) ? null : exp
									.toCode();
						}
						if (omTab != null) {
							OMExpression exp = omTab.expression;
							annotationTab = (exp == null) ? null : exp.toCode();
						}
						if (omInit != null) {
							OMExpression exp = omInit.expression;
							annotationInitDialog = (exp == null) ? null : exp
									.toCode();
						}
					}
				}
			}

			String typing = (comp.typing !=null)?(String) comp.typing.get():null;
			String varibility = (comp.variability!=null)?(String) comp.variability.get():null;
			String causality = (comp.causality!=null)?(String) comp.causality.get():null;
			
			if (comp.variability.get() != null
					&& comp.variability.get().equals("parameter")) {
				// for primitive type only expression is allowed
				// System.out.println(cd.type + "#" + compName + "#" + decName
				// + "#" + m.getUnit() + "#" + desc);
				cd._paramList.add(new Parameter(decName, compName, desc,
						defaultValue, m, true, annotationGroup, annotationTab,
						wholename, annotationInitDialog, typing, causality,varibility));
			} else {
				cd._paramList.add(new Parameter(decName, compName, desc,
						defaultValue, m, false, annotationGroup, annotationTab,
						wholename, annotationInitDialog, typing, causality,varibility));

				/**not used now Apr 16, 2012**/
				cd._resultList.add(new Result(decName, compName, defaultValue,
						m));
			}
		}
	}

	public static boolean isModelicaPrefix(String s) {
		return isModelicaPrefix(null, s);
	}

	private static boolean isModelicaPrefix(ClassDef cd, String s) {

		if (!s.startsWith(Constants.MODELICA_LIB_PREFIX)
				&& !primitiveSet.contains(s)) {
			if (cd != null)
				System.err.println("Extends Clause Mapping not found "
						+ cd.type + "#" + s);
			return false;
		}
		return true;
	}

	private static String shortNameLookup(String extName, TreeNode node) {
		TreeNode parentNode = node.getParent();

		TreeNode inode;
		if (!primitiveSet.contains(extName)
				&& !extName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
			StringTokenizer st = new StringTokenizer(extName, ".");
			String firstToken = st.nextToken();
			String s = checkCompOSelf(node, firstToken);
			if (isFound(s)) {
				inode = getNode(s);
				while (inode != null && st.hasMoreTokens()) {
					s = checkComponent(inode, st.nextToken());
					if (isFound(s)) {
						inode = getNode(s);
					}
				}
				if (isFound(s)) {
					extName = s;
					return extName;
				}
			}
			// check import clause
			s = checkImportClause(node, firstToken);
			if (isFound(s)) {
				inode = getNode(s);
				while (inode != null && st.hasMoreTokens()) {
					inode = checkInLowerScope(inode, st.nextToken());
				}
				if (inode != null) {
					extName = getClassDef(inode).type;
					if (isFound(extName))
						return extName;
				}
			}
			// check public element from parents
			// continue check parents
			inode = parentNode.getParent();

			while (inode != null) {
				s = checkComponent(inode, firstToken);

				if (isFound(s)) {
					inode = getNode(s);
					while (inode != null && st.hasMoreTokens()) {
						inode = checkInLowerScope(inode, st.nextToken());
					}
					if (inode != null) {
						extName = getClassDef(inode).type;
						if (isFound(extName))
							return extName;
					}
				}
				inode = inode.getParent();
			}
			// // check result
			// isModelicaPrefix(cd, extName);
		}
		return extName;
	}

	private static String checkCompOSelf(TreeNode node, String extName) {
		TreeNode parentNode = node.getParent();
		int j = parentNode.getIndex(node);
		ClassDef cd = getClassDef(parentNode);
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			if (i == j)
				continue;// skip itself
			node = parentNode.getChildAt(i);

			ClassDef childCd = getClassDef(node);
			String type = childCd.type;
			if (getLastToken(type).equals(extName)) {
				return type;
			}
		}// finish checking parent componets
		// check extends component
		for (ExtendsClause ex : cd.extendsList) {
			TreeNode tnode = getNode((String) ex.name.get());
			if (tnode == null)
				continue;
			extName = checkComponent(tnode, extName);
			if (isFound(extName))
				return extName;
		}
		return extName;
	}

	private static String compNameLookup(String compName, TreeNode node) {
		TreeNode inode;
		if (!primitiveSet.contains(compName)
				&& !compName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
			StringTokenizer st = new StringTokenizer(compName, ".");
			String firstToken = st.nextToken();
			String s = checkComponent(node, firstToken);
			if (isFound(s)) {
				inode = getNode(s);
				while (inode != null && st.hasMoreTokens()) {
					s = checkComponent(inode, st.nextToken());
					if (isFound(s)) {
						inode = getNode(s);
					}
				}
				if (isFound(s)) {
					compName = s;
					return compName;
				}
			}
			// check import clause
			s = checkImportClause(node, firstToken);
			if (isFound(s)) {
				inode = getNode(s);
				while (inode != null && st.hasMoreTokens()) {
					inode = checkInLowerScope(inode, st.nextToken());
				}
				if (inode != null) {
					compName = getClassDef(inode).type;
					if (isFound(compName))
						return compName;
				}
			}
			// check public element from parents
			// continue check parents
			inode = node.getParent();

			while (inode != null) {
				s = checkComponent(inode, firstToken);
				// TreeNode tnode = checkAmongElements(inode, compName);
				if (isFound(s)) {
					inode = getNode(s);
					while (inode != null && st.hasMoreTokens()) {
						inode = checkInLowerScope(inode, st.nextToken());
					}
					if (inode != null) {
						compName = getClassDef(inode).type;
						if (isFound(compName))
							return compName;
					}
				}
				inode = inode.getParent();
			}
		} else {
			// check primitive type;

		}
		return compName;
	}

	private static boolean isFound(String compName) {
		if (compName == null)
			return false;
		if (compName.startsWith(Constants.MODELICA_LIB_PREFIX))
			return true;
		else
			return false;
	}

	private static String checkComponent(TreeNode cnode, String compName) {
		ClassDef parent = getClassDef(cnode);

		// check children name
		boolean found = false;
		while (parent.shortClass) {
			if (!parent.refName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
				parent.refName = shortNameLookup(parent.refName, cnode);
			}
			cnode = getNode(parent.refName);
			// here might be enter INf loop @Sam
			if (cnode != null) {
				parent = getClassDef(cnode);
			} else
				break;
		}
		TreeNode node = getNode(parent.type);
		if (node == null)
			return compName;
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode inode = node.getChildAt(i);

			String type = getClassDef(inode).type;
			if (getLastToken(type).equals(compName)) {
				return type;
			}
		}
		// check component declaration
		for (ComponentClause comp : parent.compList) {
			for (ComponentDecl compDecl : comp.declList) {
				String s = (String) compDecl.varName.get();
				if (compName.equals(s)) {
					found = true;
					System.out
							.println("Value get from component Declaration Name..to be solved");
					return (String) comp.typeName.get();
				}
			}
		}
		// check extend clause
		for (ExtendsClause ext : parent.extendsList) {
			String exName = (String) ext.name.get();
			// here could introduce loop sam
			if (!exName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
				exName = extNameLookup(exName, node);
				ext.name.set(exName);
			}
			TreeNode tnode = getNode(exName);
			if (tnode == null)
				continue;
			compName = checkComponent(tnode, compName);
			if (isFound(compName))
				return compName;
		}
		return compName;
	}

	private static String extNameLookup(String extName, TreeNode node) {

		// if the extend type is predifined type then ignore
		if (!primitiveSet.contains(extName)
				&& !extName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
			// check import clause;
			extName = checkImportClause(node, extName);
			if (isFound(extName)) {
				return extName;
			}

			// @sam 5 Dec, 2010. simple Name lookup, check local, check
			// upper scope
			// solve for complex extend with dot notation
			StringTokenizer st = new StringTokenizer(extName, ".");
			TreeNode inode = checkInUpperScope(node, st.nextToken());
			while (inode != null && st.hasMoreTokens()) {
				inode = checkInLowerScope(inode, st.nextToken());
			}
			if (inode != null)
				extName = getClassDef(inode).type;
			if (isFound(extName))
				return extName;

		}
		return extName;
	}

	private static String checkImportClause(TreeNode node, String extName) {
		ClassDef cd = getClassDef(node);

		for (ImportClause imp : cd.importList) {
			String importName = (String) imp.name.get();
			if (getLastToken(importName).equals(extName)) {
				extName = importName;
				return extName;
			}

			// check if alias name= extends name;
			if (imp.alias != null) {
				String alias = (String) imp.alias.get();
				if (extName.equals(alias)) {
					extName = importName;
					return extName;
				}
				// check whether the alias is used in the extend
				// clause, e.g. D.Interfaces.SISO where D is the alias
				// name for an import clause
				if (extName.contains(".")) {
					int Dotindex = extName.indexOf(".");
					String key = extName.substring(0, Dotindex);
					String reminder = extName.substring(Dotindex);
					if (key.equals(alias)) {

						extName = (String) imp.name.get() + reminder;
						return extName;
					}
				}
			}// end of check alias

			// check includsub
			Boolean b = (Boolean) imp.includeSub.get();
			if (b) {
				TreeNode inode = getNode(importName);
				if (inode != null) {
					inode = checkInLowerScope(inode, extName);
					if (inode != null) {
						extName = getClassDef(inode).type;
						return extName;
					}
				}
			}

		}// end of checking import clause

		TreeNode parentNode = node.getParent();
		if (parentNode != null)
			extName = checkImportClause(parentNode, extName);
		return extName;
	}

	public static ClassDef getClassDef(String s) {
		if (primitiveSet.contains(s)) {
			System.err.println("primitive type No ClassDef " + s);
		}
		TreeNode node = getNode(s);
		if (node != null) {
			return getClassDef(node);
		}
		System.err.println("classDef can not found " + s);
		return null;
	}

	private static TreeNode getNode(String string) {
		if (string == null)
			return null;
		if (!string.startsWith(Constants.MODELICA_LIB_PREFIX))
			return null;
		StringTokenizer st = new StringTokenizer(string, ".");
		TreeNode node = libRoot;
		while (node != null && st.hasMoreTokens()) {
			String token = st.nextToken();
			node = checkInLowerScope(node, token);
		}
		return node;
	}

	private static TreeNode checkInLowerScope(TreeNode currNode, String name) {

		ClassDef cd = getClassDef(currNode);
		// TODO did not check recursive short class here
		if (cd.shortClass) {
			currNode = getNode(cd.refName);
			if (currNode != null) {
				cd = getClassDef(currNode);
			}
		}
		for (int i = 0; i < currNode.getChildCount(); i++) {
			TreeNode Node = currNode.getChildAt(i);
			ClassDef cdf = getClassDef(Node);
			if (getLastToken(cdf.type).equals(name)) {
				name = cdf.type;
				return Node;
			}
		}
		// check extends clause
		for (ExtendsClause ex : cd.extendsList) {
			TreeNode tnode = getNode((String) ex.name.get());
			if (tnode == null)
				continue;
			tnode = checkInLowerScope(tnode, name);
			if (tnode != null)
				return tnode;
		}
		return null;
	}

	public static ClassDef getClassDef(TreeNode node) {
		Object obj = ((DefaultMutableTreeNode) node).getUserObject();
		return (ClassDef) obj;
	}

	private static TreeNode checkInUpperScope(TreeNode currNode, String extName) {
		for (int i = 0; i < currNode.getChildCount(); i++) {
			// exempt self node
			TreeNode siblingNode = currNode.getChildAt(i);
			ClassDef siblingDef = getClassDef(siblingNode);
			String nodeType = siblingDef.type;

			if (getLastToken(nodeType).equals(extName)) {
				extName = siblingDef.type;
				return siblingNode;
			}
		} // end of searching the sibling node

		TreeNode node = currNode.getParent();
		if (node != null) {
			return checkInUpperScope(node, extName);
		} else
			return null;
	}

	private static String getLastToken(String nodeType) {

		int index = nodeType.lastIndexOf(".");
		if (index < 0) {
			index = -1;
		}
		return nodeType.substring(index + 1);
	}

	// rightnow used for extend look up only
	public static boolean simpleNameLookup(TreeNode node, String name) {
		// for composited lookup not handle here
		if (name.contains("."))
			return false;
		return false;
	}

	public static ClassDef forName(String name) {
		if (name == null) {
			return null;
		}
		// return typeMap.get(name);
		return getClassDef(name);
	}
}

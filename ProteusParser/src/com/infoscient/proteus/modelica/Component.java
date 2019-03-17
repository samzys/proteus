package com.infoscient.proteus.modelica;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

//import com.infoscient.ObjectRegistry;
import com.infoscient.proteus.Constants;
import com.infoscient.proteus.Utils; //import com.infoscient.proteus.e3d.LinkMap;
//import com.infoscient.proteus.e3d.geom.Geometry;
//import com.infoscient.proteus.e3d.geom.PrimitiveGeometry;
import com.infoscient.proteus.modelica.Placement.Transformation;
import com.infoscient.proteus.modelica.parser.ModelicaParser;
import com.infoscient.proteus.modelica.parser.OMArgument;
import com.infoscient.proteus.modelica.parser.OMClassDefinition;
import com.infoscient.proteus.modelica.parser.OMComponentClause;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.parser.ParseException;
import com.infoscient.proteus.modelica.types.ClassModificationBackedStringProperty;
import com.infoscient.proteus.modelica.types.ExpressionBackedStringProperty;
import com.infoscient.proteus.modelica.types.MPrimitive;
import com.infoscient.proteus.modelica.types.MReal;
import com.infoscient.proteus.types.StringProperty; //import com.infoscient.proteus.ui.Library;
//import com.infoscient.proteus.ui.RunConfigProperties;
//import com.infoscient.proteus.ui.Library.Category;
//import com.infoscient.proteus.ui.canvas.Icon;
//import com.infoscient.proteus.ui.canvas.Icon.ModelicaImpl;
import com.infoscient.proteus.ui.util.MiscUtils;

public class Component extends ClassDef implements Transferable {
	// private static ImageIcon defaultIcon = new ImageIcon(Category.class
	// .getResource("resources/default_comp.png"));

	public static DataFlavor componentFlavor;

	protected StringProperty runConfigurations;

	protected StringProperty libFiles;

	private StringProperty varNameProperty; // 09 Nov 16 : lt

	private Component[] components; // 09 Nov 16 : lt

	private Connector[] connectors;

	private Parameter[] parameters;

	private List<Parameter> modifiers = new LinkedList<Parameter>();

	// Nov 5, Sam
	private Map<String, Object> parametervalue = new HashMap<String, Object>();

	private Result[] results;

	private Animation[] animationParams;

	private List<File> libFilesList;

	// private List<RunConfigProperties> runConfigPropsList;

	static {
		try {
			componentFlavor = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType + ";class="
							+ Component.class.getName());
		} catch (ClassNotFoundException e) {
			// Should never happen
			componentFlavor = null;
		}
	}

	public Component(OMClassDefinition classDefOM) {
		super(classDefOM);
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return this;
		}
		throw new UnsupportedFlavorException(flavor);
	}

	/*
	 * descend into super classDefs to find icon annotation if this classDef
	 * doesn't contain annotation
	 */

	// public Icon getIcon() {
	// return getIcon(false);
	// }

	// public List<StringProperty> getIcons() {
	//
	// ClassDef classDef = this;
	// List<StringProperty> IcongraphicsList = new LinkedList<StringProperty>();
	// int[] extent = null;
	//
	//
	// while (// icon == null &&
	// classDef != null) {
	// List<Annotation> anns = classDef.annotations;
	// for (Annotation ann : anns) {
	// OMModification m = (OMModification) ann.map
	// .get("Icon.graphics");
	// if (m != null) {
	// // System.out.println(m.toCode());
	// StringProperty strProp = new ExpressionBackedStringProperty(
	// m, "Icon.graphics", Constants.CATEGORY_CODE,
	// "Icon.graphics", false);
	// IcongraphicsList.add(strProp);
	//					
	// }
	//
	// // find the extend of this icon
	// m = (OMModification) ann.map
	// .get("Icon.coordinateSystem.extent");
	// if (m != null && m.expression != null) {
	// if (extent == null)
	// extent = MiscUtils.parseExtent(m.expression.toCode());
	// }
	// }
	//
	// if (classDef.extendsName != null) {
	// classDef = RefResolver.forName(classDef.extendsName);
	// } else {
	// classDef = null;
	// }
	// }// end of while for loading the Icon graphic information.
	// if (IcongraphicsList.size() >= 1) {
	// icon = new Icon.ModelicaImpl(IcongraphicsList
	// .toArray(new StringProperty[0]), null, this, false);
	// }
	// if (icon != null && extent != null) {
	// if (icon instanceof ModelicaImpl)
	// ((Icon.ModelicaImpl) icon).setExtent(extent);
	// }
	//
	// // Couldn't find icon in annotation block
	// if (icon == null) {
	// icon = new Icon.DefaultImpl((String) name.get());
	// }
	// return IcongraphicsList;
	// }

	// public Icon getIcon(boolean libIcon) {
	//
	// ClassDef classDef = this;
	// List<StringProperty> IcongraphicsList = new LinkedList<StringProperty>();
	// int[] extent = null;
	//
	// while (// icon == null &&
	// classDef != null) {
	// List<Annotation> anns = classDef.annotations;
	// for (Annotation ann : anns) {
	// OMModification m = (OMModification) ann.map
	// .get("Icon.graphics");
	// if (m != null) {
	// // System.out.println(m.toCode());
	// StringProperty strProp = new ExpressionBackedStringProperty(
	// m, "Icon.graphics", Constants.CATEGORY_CODE,
	// "Icon.graphics", false);
	// IcongraphicsList.add(strProp);
	// }
	//
	// // find the extend of this icon
	// m = (OMModification) ann.map
	// .get("Icon.coordinateSystem.extent");
	// if (m != null && m.expression != null) {
	// if (extent == null)
	// extent = MiscUtils.parseExtent(m.expression.toCode());
	// }
	// }
	//
	// if (classDef.extendsName != null) {
	// classDef = RefResolver.forName(classDef.extendsName);
	// } else {
	// classDef = null;
	// }
	// }// end of while for loading the Icon graphic information.
	// if (IcongraphicsList.size() >= 1) {
	// icon = new Icon.ModelicaImpl(IcongraphicsList
	// .toArray(new StringProperty[0]), null, this, libIcon);
	// }
	// if (icon != null && extent != null) {
	// if (icon instanceof ModelicaImpl)
	// ((Icon.ModelicaImpl) icon).setExtent(extent);
	// }
	//
	// // Couldn't find icon in annotation block
	// if (icon == null) {
	// icon = new Icon.DefaultImpl((String) name.get());
	// }
	// return icon;
	// }

	private boolean diagramCreated;

	// modified by @GL 1 Dec. 2010
	// public Icon getDiagram() {
	// ClassDef classDef = this;
	// //System.out.println("*********************getDiagram in");
	// if (!diagramCreated) {
	//
	// ArrayList<StringProperty> annList = new ArrayList<StringProperty>();
	// ArrayList<String> strList = new ArrayList<String>();
	//
	// DiagramPaser paser = new DiagramPaser();
	// paser.addAllAnnotations(classDef, annList, null, null, strList,false);
	//			
	// StringProperty[] strProps = new StringProperty[annList.size()];
	// String[] strs = new String[strList.size()];
	//
	// for (int i = 0; i < annList.size(); i++) {
	// strProps[i] = annList.get(i);
	// }
	// System.out.println("***************************************");
	// for (int i = 0; i < annList.size(); i++) {
	// strs[i] = strList.get(i);
	// System.out.println(strs[i]);
	//
	// }
	// System.out.println("***************************************");
	// // List<Annotation> anns = classDef.annotations;
	// // for (int i = 0; i < anns.size(); i++) {
	// // Annotation ann = anns.get(i);
	// //
	// // OMModification m = (OMModification) ann.map
	// // .get("Diagram.graphics");
	// // if (m != null) {
	// // StringProperty strProp = new ExpressionBackedStringProperty(
	// // m, "Diagram.graphics", Constants.CATEGORY_CODE,
	// // "Diagram.graphics", false);
	// //
	// // // strProps[i] = strProp;
	// // isDiagram = true;
	// // break;
	// // }
	// // }
	//
	// diagram = new Icon.ModelicaImpl(strProps, null, this, strs);
	// diagram.setIconSize(400, 400);
	// diagram.isDiagram = paser.isDiagram;
	// diagramCreated = true;
	//
	// if (classDef.extendsName != null) {
	// classDef = RefResolver.forName(classDef.extendsName);
	// } else {
	// classDef = null;
	// }
	//
	// }
	// return diagram;
	// }

	private boolean geometryCreated;

	// public Geometry getGeometry() {
	// if (!geometryCreated) {
	// geometryCreated = true;
	// for (Annotation ann : annotations) {
	// OMModification gm;
	// if ((gm = (OMModification) ann.map.get("PrimitiveGeometry")) != null) {
	// try {
	// StringProperty strProp = new ClassModificationBackedStringProperty(
	// gm, "PrimitiveGeometry",
	// Constants.CATEGORY_CODE,
	// "3D geometry made up of 'primitives'", false);
	// // create geometry
	// geometry = new PrimitiveGeometry(strProp);
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	// return geometry;
	// }
	//
	// public void setGeometry(Geometry geometry) {
	// geometryCreated = true;
	// this.geometry = geometry;
	// }
	//
	// private LinkMap linkMap;
	//
	// private boolean linkMapCreated;
	//
	// public LinkMap getLinkMap() {
	// if (!linkMapCreated) {
	// linkMapCreated = true;
	// for (Annotation ann : annotations) {
	// OMModification gm;
	// if ((gm = (OMModification) ann.map.get("LinkMap")) != null) {
	// try {
	// StringProperty strProp = new ClassModificationBackedStringProperty(
	// gm,
	// "LinkMap",
	// Constants.CATEGORY_CODE,
	// "Mapping between plot variables and their corresponding 3D parts",
	// false);
	// // create linkMap
	// linkMap = new LinkMap(strProp);
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// }
	// }
	// System.out.println("gm = " + gm);
	// }
	// }
	// return linkMap;
	// }
	//
	// protected List<RunConfigProperties> getRunConfigPropsList() {
	// if (runConfigPropsList == null) {
	// runConfigPropsList = new LinkedList<RunConfigProperties>();
	// for (Annotation ann : annotations) {
	// final OMModification m = (OMModification) ann.map
	// .get("RunConfigurations");
	// if (m != null) {
	// try {
	// runConfigurations = new ClassModificationBackedStringProperty(
	// m, "RunConfigurations",
	// Constants.CATEGORY_CODE, "RunConfigurations",
	// false);
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// if (runConfigurations != null) {
	// String s = (String) runConfigurations.get();
	// ModelicaParser parser = new ModelicaParser(new StringReader(s));
	// try {
	// OMModification m = parser.modification();
	// if (m.classModification != null
	// && m.classModification.argList != null) {
	// for (OMArgument arg : m.classModification.argList.argList) {
	// OMModification m2 =
	// arg.elementModificationOrReplaceable.elementModification.modification;
	// List<String> strProps = new ArrayList<String>();
	// for (OMArgument arg2 : m2.classModification.argList.argList) {
	// String strProp = arg2.toCode();
	// strProps.add(strProp);
	// }
	// RunConfigProperties runConfigProp = new RunConfigProperties();
	// runConfigProp.fromArray(strProps
	// .toArray(new String[0]));
	// runConfigPropsList.add(runConfigProp);
	// }
	// }
	// } catch (com.infoscient.proteus.ParseException e) {
	// e.printStackTrace();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// return runConfigPropsList;
	// }
	//
	// public RunConfigProperties getRunConfiguration(int index) {
	// return getRunConfigPropsList().get(index);
	// }
	//
	// public void setRunConfiguration(int index,
	// RunConfigProperties runConfigProps) {
	// getRunConfigPropsList().set(index, runConfigProps);
	// runConfigurationChanged();
	// }
	//
	// public void addRunConfiguration(RunConfigProperties runConfigProps) {
	// getRunConfigPropsList().add(runConfigProps);
	// runConfigurationChanged();
	// }
	//
	// public void removeRunConfiguration(RunConfigProperties runConfigProps) {
	// getRunConfigPropsList().remove(runConfigProps);
	// runConfigurationChanged();
	// }
	//
	// public void runConfigurationChanged() {
	// System.out.println("run config changed" + runConfigurations);
	// if (runConfigurations == null) {
	// return;
	// }
	// RunConfigProperties[] runConfigs = getRunConfigurations();
	// System.out.println(runConfigs.length);
	// String[] runConfigCodes = new String[runConfigs.length];
	// for (int i = 0; i < runConfigs.length; i++) {
	// runConfigCodes[i] = "experiment("
	// + Utils.join(runConfigs[i].toArray(), ",") + ")";
	// }
	// String s = "(" + Utils.join(runConfigCodes, ",") + ")";
	// System.out.println(s);
	// runConfigurations.set(s);
	// }
	//
	// public RunConfigProperties[] getRunConfigurations() {
	// return getRunConfigPropsList().toArray(new RunConfigProperties[0]);
	// }

	protected List<File> getLibFilesList() {
		if (libFilesList == null) {
			libFilesList = new LinkedList<File>();
			for (Annotation ann : annotations) {
				OMModification m = (OMModification) ann.map.get("LibFiles");
				if (m != null) {
					try {
						libFiles = new ExpressionBackedStringProperty(m,
								"LibFiles", Constants.CATEGORY_CODE,
								"LibFiles", false);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			}
			if (libFiles != null) {
				String s = (String) libFiles.get();
				ModelicaParser parser = new ModelicaParser(new StringReader(s));
				try {
					OMModification m = parser.modification();
					if (m.expression != null) {
						String exp = m.expression.toCode().trim();
						if (exp.startsWith("(")) {
							exp = exp.substring(1);
						} else {
							throw new ParseException(
									"Expected string: (<filenames>)");
						}
						if (exp.endsWith(")")) {
							exp = exp.substring(0, exp.length() - 1);
						} else {
							throw new ParseException(
									"Expected string: (<filenames>)");
						}
						String[] fileNameStrs = exp.split(",");
						for (String nameStr : fileNameStrs) {
							nameStr = nameStr.trim();
							if (nameStr.startsWith("\"")) {
								nameStr = nameStr.substring(1);
							} else {
								throw new ParseException(
										"Expected string: \"<filename>\"");
							}
							if (nameStr.endsWith("\"")) {
								nameStr = nameStr.substring(0,
										nameStr.length() - 1);
							} else {
								throw new ParseException(
										"Expected string: \"<filename>\"");
							}
							if (nameStr.length() > 0) {
								File libFile = new File(nameStr);
								libFilesList.add(libFile);
							}
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return libFilesList;
	}

	public File getLibFile(int index) {
		return getLibFilesList().get(index);
	}

	public void setLibFile(int index, File libFile) {
		getLibFilesList().set(index, libFile);
		libFilesChanged();
	}

	public void addLibFile(File libFile) {
		getLibFilesList().add(libFile);
		libFilesChanged();
	}

	public void removeLibFile(File libFile) {
		getLibFilesList().remove(libFile);
		libFilesChanged();
	}

	public void libFilesChanged() {
		if (libFiles == null) {
			return;
		}
		File[] files = getLibFiles();
		String[] fileNameStr;
		if (files.length > 0) {
			fileNameStr = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				fileNameStr[i] = "\""
						+ files[i].getAbsolutePath().replace('\\', '/') + "\"";
			}
		} else { // needed coz omc compiler barfs on empty list
			fileNameStr = new String[] { "\"\"" };
		}
		String s = "(" + Utils.join(fileNameStr, ",") + ")";
		libFiles.set(s);
	}

	public File[] getLibFiles() {
		return getLibFilesList().toArray(new File[0]);
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { componentFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(componentFlavor);
	}

	public String toString() {
		return name.get() + " [" + type + "]";
	}

	// public Connector[] getConnectors(StringProperty nameProperty) {
	// // if (connectors != null) {
	// // for (Connector c : connectors) {
	// // if(!c.name.get().equals(nameProperty.get())){
	// // connectors = findConnectors(this, nameProperty);
	// // break;
	// // }
	// // }
	// // } else {
	// System.out.println(nameProperty.get());
	// connectors = findConnectors(this, nameProperty);
	// // }
	// // System.out.println("[" + name.get() + "] "
	// // + (String) nameProperty.get() + " has connectors: "
	// // + connectors.length + "@Component, GetConnectors Class");
	// return connectors;
	// }

	public Parameter[] getParameters() {
		if (_paramList != null && _paramList.size() > 0) {
			parameters = _paramList.toArray(new Parameter[0]);
		}

//		// return parameters;
//		if (parameters == null) {
//			// System.out.println(type);
//			// if (true || parameters == null) {
//			List<Parameter> paramList = new LinkedList<Parameter>();
//			List<Result> resultList = new LinkedList<Result>();
//			List<Animation> animationList = new LinkedList<Animation>();
//			List<Initialization> initList = new LinkedList<Initialization>();
//			List<Advanced> advancedList = new LinkedList<Advanced>();
//
//			findParametersAndResults(this, paramList, resultList,
//					animationList, initList, advancedList);
//			parameters = paramList.toArray(new Parameter[0]);
//			if (results == null) {
//				results = resultList.toArray(new Result[0]);
//			}
//			if (animationParams == null) {
//				animationParams = animationList.toArray(new Animation[0]);
//			}
//			// @sam Nov, 5
//			// resolveParameters();
//		}
		return parameters;
	}

	private void resolveParameters() {

		// for (Parameter para : parameters) {
		// String s = recursiveLoad(para.defaultValue);
		// // System.out.println(s + "@Component.resolveParameters");
		// }
	}

	private String recursiveLoad(String key) {
		// @sam, Nov 5. only simple equal sign added.
		// currently only handle very simple expressions a+b
		String[] delim = { "(", ")", "+", "-", "*", "/" };
		String[] temp;
		int index, operator = 0;
		if ((index = key.indexOf("+")) != -1) {
			temp = key.split("+");
			operator = 0;
		} else if ((index = key.indexOf("-")) != -1) {
			temp = key.split("-");
			operator = 1;
		} else if ((index = key.indexOf("*")) != -1) {
			temp = key.split("*");
			operator = 2;
		} else if ((index = key.indexOf("/")) != -1) {
			temp = key.split("/");
			operator = 3;
		}

		if (parametervalue.containsKey(key)) {
			if (parametervalue.get(key) instanceof String) {
				return recursiveLoad((String) parametervalue.get(key));
			}
		}
		return key;
	}

	// public Animation[] getAnimationParams() {
	// if (animationParams == null) {
	// List<Parameter> paramList = new LinkedList<Parameter>();
	// List<Result> resultList = new LinkedList<Result>();
	// List<Animation> animationList = new LinkedList<Animation>();
	// List<Initialization> initList = new LinkedList<Initialization>();
	// List<Advanced> advancedList = new LinkedList<Advanced>();
	//
	// findParametersAndResults(this, paramList, resultList,
	// animationList, initList, advancedList);
	// animationParams = animationList.toArray(new Animation[0]);
	// if (results == null) {
	// results = resultList.toArray(new Result[0]);
	// }
	// if (parameters == null) {
	// parameters = paramList.toArray(new Parameter[0]);
	// }
	// }
	// return animationParams;
	// }

	public Parameter[] getModifiers() {
		return modifiers.toArray(new Parameter[0]);
	}

	public void addModifier(Parameter param) {
		modifiers.add(param);
	}

	public void removeModifier(Parameter param) {
		modifiers.remove(param);
	}

	//
	// public Result[] getResults() {
	// if (results == null) {
	// List<Parameter> paramList = new LinkedList<Parameter>();
	// List<Result> resultList = new LinkedList<Result>();
	// List<Animation> animationList = new LinkedList<Animation>();
	// List<Initialization> initList = new LinkedList<Initialization>();
	// List<Advanced> advancedList = new LinkedList<Advanced>();
	//
	// findParametersAndResults(this, paramList, resultList,
	// animationList, initList, advancedList);
	// results = resultList.toArray(new Result[0]);
	// if (parameters == null) {
	// parameters = resultList.toArray(new Parameter[0]);
	// }
	// if (animationParams == null) {
	// animationParams = animationList.toArray(new Animation[0]);
	// }
	// }
	// return results;
	// }

	/*
	 * Note: Connectors and parameters can't be found while loading the
	 * component as the names haven't yet been resolved. Hence, these find*
	 * methods should be called after the construction phase is over (for e.g.
	 * by using lazy evaluation - find them only when the corresponding get*
	 * method is called)
	 */
	// private Connector[] findConnectors(ClassDef classDef,
	// StringProperty nameProperty) {
	// Stack<String> parentNameStack = new Stack<String>();
	// Stack<Annotation[]> annotationStack = new Stack<Annotation[]>();
	//
	// annotationStack.push(classDef.annotations.toArray(new Annotation[0]));
	// return findPins(classDef, new Stack<String>(), annotationStack,
	// nameProperty);
	// // return findConnectors(classDef, new Stack<String>(),
	// // new Stack<Annotation[]>(), nameProperty);
	// }

	// private Connector[] findPins(ClassDef classDef,
	// Stack<String> parentNameStack,
	// Stack<Annotation[]> annotationsStack, StringProperty compNameProp) {
	// List<Connector> connList = new LinkedList<Connector>();
	//	
	// for (ComponentClause cc : classDef.compList) {
	// // 11 Nov, 2010 Sam
	// // find the boolean variable determine whether to display this
	// // pin
	// // boolean display = true;
	//		
	// if (cc.declList.size() > 0) {
	// List<Annotation> anlist = new LinkedList<Annotation>();
	// for (ComponentDecl compdecl : cc.declList) {
	// Comment c = compdecl.comment;
	// if (c.annotation != null) {
	// anlist.add(c.annotation);
	// }
	// }
	// if (anlist.size() > 0) {
	// annotationsStack.push(anlist.toArray(new Annotation[0]));
	// }
	// }
	//
	// String compName = (String) cc.typeName.get();
	// if (RefResolver.primitiveSet.contains(compName)) {
	// continue;
	// }
	// ClassDef compClassDef = RefResolver.forName(compName);
	//			
	// if (compClassDef != null) {
	// if (compClassDef.restriction.equals(Constants.TYPE_CONNECTOR)
	// || compClassDef.restriction
	// .equals(Constants.TYPE_EXPANDABLECONNECTOR)) {
	// for (ComponentDecl compdecl : cc.declList) {
	// StringProperty p = compdecl.varName;
	// String prefix = null;
	// if (!parentNameStack.isEmpty()) {
	// StringBuilder sb = new StringBuilder();
	// for (String pn : parentNameStack) {
	// sb.append(pn + ".");
	// }
	// prefix = sb.toString();
	// }
	// Transformation transformation = new Transformation();
	// for (Annotation[] ans : annotationsStack) {
	// for (Annotation an : ans) {
	// OMModification m = (OMModification) an.map
	// .get("Placement.transformation.origin");
	// if (m != null) {
	// transformation.origin = MiscUtils
	// .parseOrigin(m.expression.toCode());
	// } else {
	// transformation.origin = new int[2];
	// }
	// m = (OMModification) an.map
	// .get("Placement.transformation.extent");
	// if (m != null) {
	// transformation.extent = MiscUtils
	// .parseExtent(m.expression.toCode());
	// }
	// m = (OMModification) an.map
	// .get("Placement.transformation.rotation");
	// if (m != null) {
	// transformation.rotation = Integer
	// .parseInt(m.expression.toCode());
	// } else {
	// transformation.rotation = -1;
	// }
	// }
	// }
	// System.out.println(p.get()+", "+prefix);
	// if(compdecl.booleanVariable!=null){
	// System.out.println();
	// }
	// connList.add(new Connector(p, prefix, transformation,
	// compClassDef, this, compdecl.booleanVariable));
	// }
	// }
	// }
	// }// end of checking component clause
	//
	// for (ExtendsClause extc : classDef.extendsList) {
	// String extendsName = (String) extc.name.get();
	// ClassDef superClassDef = RefResolver.forName(extendsName);
	// if (superClassDef == null) continue;
	//			
	// if (superClassDef.restriction.equals(Constants.TYPE_CONNECTOR)) {
	// String prefix = null;
	// if (!parentNameStack.isEmpty()) {
	// StringBuilder sb = new StringBuilder();
	// for (String pn : parentNameStack) {
	// sb.append(pn + ".");
	// }
	// prefix = sb.toString();
	// }
	// Transformation transformation = new Transformation();
	// for (Annotation[] ans : annotationsStack) {
	// for (Annotation an : ans) {
	// OMModification m = (OMModification) an.map
	// .get("Placement.transformation.origin");
	// if (m != null) {
	// transformation.origin = MiscUtils
	// .parseOrigin(m.expression.toCode());
	// } else {
	// transformation.origin = new int[2];
	// }
	// m = (OMModification) an.map
	// .get("Placement.transformation.extent");
	// if (m != null) {
	// transformation.extent = MiscUtils
	// .parseExtent(m.expression.toCode());
	// }
	// m = (OMModification) an.map
	// .get("Placement.transformation.rotation");
	// if (m != null) {
	// transformation.rotation = Integer
	// .parseInt(m.expression.toCode());
	// } else {
	// transformation.rotation = -1;
	// }
	// }
	// }
	// connList.add(new Connector(compNameProp, prefix,
	// transformation, superClassDef, this, null));
	// //test
	// connList.addAll(Arrays.asList(findPins(superClassDef,
	// parentNameStack, annotationsStack, superClassDef.name)));
	// //if connector
	// } else {
	// if (superClassDef.annotations.size() > 0) {
	// annotationsStack.push(superClassDef.annotations
	// .toArray(new Annotation[0]));
	// }
	// connList.addAll(Arrays.asList(findPins(superClassDef,
	// parentNameStack, annotationsStack, null)));
	// }
	// }// end of checking extendsClause
	//
	// return connList.toArray(new Connector[0]);
	// }
	//
	private void findParametersAndResults(ClassDef classDef,
			List<Parameter> paramList, List<Result> resultList,
			List<Animation> animationList, List<Initialization> initList,
			List<Advanced> advancedList) {
		for (ExtendsClause exc : classDef.extendsList) {
			String extendsName = (String) exc.name.get();
			ClassDef superClassDef = RefResolver.forName(extendsName);
			if (superClassDef != null) {
				if (superClassDef.restriction.equals(Constants.TYPE_TYPE)) {
					// XXX What string prop object should we use when
					// the class itself is a type?
					String type = superClassDef.type;
					paramList.add(new Parameter(null, type, "", null,
							superClassDef.primitiveType, true));
				} else {
					findParametersAndResults(superClassDef, paramList,
							resultList, animationList, initList, advancedList);
				}
				// here update the content for the modifier.
				if (exc.argList != null) {
					for (Argument arg : exc.argList) {
						String argName = arg.name;
						Modification md = arg.modification;
						Expression exp = md == null ? null : md.expression;

						String updatedValue = (exp == null) ? ""
								: (String) exp.value.get();
						if (paramList.size() > 0 && argName != null) {
							for (Parameter p : paramList) {
								if (argName.equalsIgnoreCase(p.name)) {
									if (p.primitiveType != null
											&& p.primitiveType instanceof MReal) {
										updatedValue = RefResolver
												.checkDoubleType(paramList,
														updatedValue);
									}
									p.defaultValue = updatedValue;
									break;
								}
							}
						}
					}
				}
			}

		}// check extendsClause

		// check component clause
		for (ComponentClause comp : classDef.compList) {

			String variability = (String) comp.variability.get();
			String compName = (String) comp.typeName.get();
			String causality = (String) comp.causality.get();
			if ((variability != null && variability.equals("parameter"))
					|| (causality != null && causality.equals("input"))) {

				if (RefResolver.primitiveSet.contains(compName)) {
					// If primitive type
					for (ComponentDecl compdecl : comp.declList) {

						StringProperty prop;
						prop = compdecl.varName;
						String name = (String) prop.get();
						prop = compdecl.comment.string;
						String description = (String) (prop == null ? "" : prop
								.get());
						Modification md = compdecl.modification;
						Expression exp = md == null ? null : md.expression;

						String defaultValue = (exp == null) ? ""
								: (String) exp.value.get();

						// @sam added for 3d animaiton
						Comment comment = compdecl.comment;
						addTabParameters(comment, name, compName, description,
								defaultValue, animationList, initList,
								advancedList);

						parametervalue.put(name, defaultValue);

						paramList.add(new Parameter(name, compName,
								description, defaultValue, null, true));
					}
				} else { // If complex type

					ClassDef compClassDef = RefResolver.forName(compName);
					if (compClassDef != null) {

						if (compClassDef.restriction
								.equals(Constants.TYPE_TYPE)) {
							String type = compClassDef.type;

							for (ComponentDecl compDecl : comp.declList) {
								StringProperty prop;
								prop = compDecl.varName;
								String name = (String) prop.get();
								prop = compDecl.comment.string;
								String description = (String) (prop == null ? ""
										: prop.get());
								Modification md = compDecl.modification;
								if (md != null && md.arguments.size() > 0) {// selection
									// 1
									for (Argument arg : md.arguments) {
										String argName = arg.name;
										if (argName.equals("start")) {
											md = arg.modification;
											break;
										}
									}
								}

								Expression exp = md == null ? null
										: md.expression;
								String defaultValue = exp == null ? ""
										: (String) exp.value.get();
								// @sam added for 3d animaiton
								Comment comment = compDecl.comment;
								addTabParameters(comment, name, compName,
										description, defaultValue,
										animationList, initList, advancedList);
								parametervalue.put(name, defaultValue);
								for (String s : compDecl.arrarysubscrpts) {
									// System.out.println(s);
								}
								paramList.add(new Parameter(name, type,
										description, defaultValue,
										compClassDef.primitiveType, true));
							}
						}
					}
				}

			}// end of parameter
			else { // variability null, hence "result" variable
				// Almost a copy of the code in the if section above,
				// could be refactored
				// @sam Nov 2, Visualizer Component goes here as well
				if (RefResolver.primitiveSet.contains(compName)) { // If
					// primitive
					// type
					for (ComponentDecl compdecl : comp.declList) {
						StringProperty prop;
						prop = compdecl.varName;
						String name = (String) prop.get();
						prop = compdecl.comment.string;
						String description = (String) (prop == null ? "" : prop
								.get());
						// @sam added for 3d animaiton
						Comment comment = compdecl.comment;
						addTabParameters(comment, name, compName, description,
								"", animationList, initList, advancedList);

						resultList.add(new Result(name, compName, description,
								null));
					}// end of checking component decl
				}// if primitive set
				else { // If complex type

					ClassDef compClassDef = RefResolver.forName(compName);
					if (compClassDef != null) {

						if (compClassDef.restriction
								.equals(Constants.TYPE_TYPE)) {// type
							OMComponentClause omComp = comp.getObjectModel();

							String tempVarName = omComp.toCode();

							for (ComponentDecl compdecl : comp.declList) {
								StringProperty prop;
								prop = compdecl.varName;
								String name = (String) prop.get();
								prop = compdecl.comment.string;

								int index = tempVarName.indexOf("[");
								int index2 = tempVarName.indexOf("]");

								if (index != -1 && index2 != -1) {
									name += tempVarName.substring(index,
											index2 + 1);
									tempVarName = tempVarName
											.substring(index2 + 1);
								}
								String description = (String) (prop == null ? ""
										: prop.get());
								// @sam added for 3d animaiton
								Comment comment = compdecl.comment;
								addTabParameters(comment, name, compName,
										description, "", animationList,
										initList, advancedList);
								resultList
										.add(new Result(name, compName,
												description,
												compClassDef.primitiveType));
							}
						} else {// @sam visualizers shape goes here
							if ((compName)
									.indexOf("Visualizers.Advanced.Shape") != -1) {
								// System.out.println(compName + " in "
								// + (String) classDef.name.get());
							}
						}
					}
				}
			}
		}// end of checking component clause
	}

	private void addTabParameters(Comment comment, String name,
			String compName, String description, String defaultValue,
			List<Animation> animationList, List<Initialization> initList,
			List<Advanced> advancedList) {
		if (comment != null) {
			if (comment.annotation != null) {
				Annotation ann = comment.annotation;
				String expression;
				OMModification m = (OMModification) ann.map.get("Dialog.tab");
				if (m != null) {
					expression = MiscUtils.parseString(m.expression.toCode());
					if (expression.equals("Animation")) {
						animationList.add(new Animation(name, compName,
								description, defaultValue));
					} else if (expression.equals("Advanced")) {
						advancedList.add(new Advanced(name, compName,
								description, defaultValue));
					} else if (expression.equals("Initialization")) {
						initList.add(new Initialization(name, compName,
								description, defaultValue));
					}
				}
			} else {// default tab and default category

			}
		} else {

		}

	}

	/**
	 * @return the components
	 */
	public Component[] getComponents() {
		if (components != null) {
			return components;
		}
		return getComponents(this).toArray(new Component[0]);
	}

	public static List<Component> getComponents(ClassDef classDef) {
		List<Component> comps = new ArrayList<Component>();
		// System.out.println("Load class def : " + classDef.type);
		// Library library = (Library) ObjectRegistry
		// .get(Constants.LIBRARY_OBJECT);
		// if (classDef instanceof Component) {
		// Component component = (Component) classDef;
		// File[] libFiles = component.getLibFiles();
		// if (libFiles.length > 0) {
		// for (File file : libFiles) {
		// try {
		// library.loadCustomLib(file);
		// } catch (Exception e) {
		// System.out.println("Error loading file:"
		// + file.getAbsolutePath() + "");
		// }
		// }
		// }
		// }

		// Icon diagram = classDef.getDiagram();
		// if (diagram != null && diagram instanceof Icon.ModelicaImpl) {
		// Icon.ModelicaImpl im = (Icon.ModelicaImpl) diagram;
		// System.out.println(im.getText());
		// }

		// Map<String, ComponentCanvasItem> cciMap = new HashMap<String,
		// ComponentCanvasItem>();
		// for (ExtendsClause ext : classDef.extendsList) {
		// String extendsName = (String) ext.name.get();
		//
		// ClassDef superClassDef = RefResolver.forName(extendsName);
		// if (superClassDef != null) {
		// comps.addAll(getComponents(superClassDef));
		// }
		// }// end of extendclause
		//
		// for (ComponentClause cc : classDef.compList) {
		// if (cc.variability.get() != null)
		// continue;
		// // only solve result parameters variability value is discrete,
		// // parameter, constant
		// String typeName = (String) cc.typeName.get();
		// if (RefResolver.primitiveSet.contains(typeName)) {
		// continue;
		// }
		// // Component component = (Component)
		// // RefResolver.forName(typeName);
		// ClassDef cd = RefResolver.getClassDef(typeName);
		// Component component = null;
		// if (cd instanceof Component) {
		// component = (Component) cd;
		// } else if (cd instanceof Category) {
		// System.err.println("Package is found " + typeName);
		// continue;
		// }
		// if (component == null) {
		// System.err.println("Could not load component: " + typeName);
		// continue;
		// } else if (component.restriction.equals(Constants.TYPE_TYPE)) {
		// continue;
		// }
		// for (ComponentDecl compdecl : cc.declList) {
		// StringProperty varNameProperty = compdecl.varName;
		// component.setVarNameProperty(varNameProperty);
		// comps.add(component);
		// }
		//
		// }// end of component clause

		return comps;
	}

	/**
	 * @param varName
	 *            the varName to set
	 */
	public void setVarNameProperty(StringProperty varNameProperty) {
		this.varNameProperty = varNameProperty;
	}

	/**
	 * @return the varName
	 */
	public StringProperty getVarNameProperty() {
		return varNameProperty;
	}

	public static class Parameter implements Serializable {
		// varibility such as paramater, constant will set header to true;
		public boolean header = false;

		public String name;

		public final String type;
		
		private String wholename;//used to search for the type_id;

		public String description;

		public String defaultValue;

		public final MPrimitive primitiveType;

		public String unit;

		public ComponentDecl cdecl;

		private String group;

		private String tab;
		
		private String initDialog;
		
		private String variability;
		
		private String typing;
		
		private String causality;
		
		private List<String> arrayFormList = new ArrayList<String>();
		

		/**
		 * @return the group
		 */
		public String getGroup() {
			return group;
		}

		/**
		 * @return the initDialog
		 */
		public String getInitDialog() {
			return initDialog;
		}

		/**
		 * @param initDialog the initDialog to set
		 */
		public void setInitDialog(String initDialog) {
			this.initDialog = initDialog;
		}

		/**
		 * @param group
		 *            the group to set
		 */
		public void setGroup(String group) {
			this.group = group;
		}

		public Parameter(String name, String type, String description,
				String defaultValue, MPrimitive primitiveType,
				ComponentDecl compdecl) {
			this.name = name;
			this.type = type;
			this.description = description;
			this.defaultValue = defaultValue;
			this.primitiveType = primitiveType;
			this.cdecl = compdecl;
		}
		public String getVariability() {
			return variability;
		}

		public void setVariability(String variability) {
			this.variability = variability;
		}

		public String getTyping() {
			return typing;
		}

		public void setTyping(String typing) {
			this.typing = typing;
		}

		public String getCausality() {
			return causality;
		}

		public void setCausality(String causality) {
			this.causality = causality;
		}

		public List<String> getArrayFormList() {
			return arrayFormList;
		}

		public void setArrayFormList(List<String> arrayFormList) {
			this.arrayFormList = arrayFormList;
		}

		public Parameter(String name, String type, String description,
				String defaultValue, MPrimitive primitiveType,
				 boolean header) {
			this(name, type, description, defaultValue, primitiveType, null);
			this.header = header;
		}
		public Parameter(String name, String type, String description,
				String defaultValue, MPrimitive primitiveType, boolean header,
				String group, String tab, String wholename) {
			this(name, type, description, defaultValue, primitiveType, null);
			this.header = header;
			this.group = group;
			this.tab = tab;
			this.wholename = wholename;
		}
		
		public Parameter(String name, String type, String description,
				String defaultValue, MPrimitive primitiveType, boolean header,
				String group, String tab, String wholename, String initDialog, String typing, String causality, String variability) {
			this(name, type, description, defaultValue, primitiveType, null);
			this.header = header;
			this.group = group;
			this.tab = tab;
			this.wholename = wholename;
			this.initDialog = initDialog;
			this.typing  = typing;
			this.causality = causality;
			this.variability = variability;
			
		}

		/**
		 * @return the tab
		 */
		public String getTab() {
			return tab;
		}

		/**
		 * @param tab the tab to set
		 */
		public void setTab(String tab) {
			this.tab = tab;
		}

		public Parameter(String name, String type, String description,
				String defaultValue) {
			this(name, type, description, defaultValue, null, null);
		}

		public String getUnit() {
			if (primitiveType != null)
				return primitiveType.getUnit();
			else
				return null;
		}

		/**
		 * @return the wholename
		 */
		public String getWholename() {
			return wholename;
		}

		/**
		 * @param wholename the wholename to set
		 */
		public void setWholename(String wholename) {
			this.wholename = wholename;
		}
	}

	// @sam added for 3d animation
	public static class Animation extends Parameter {

		public String group;

		public boolean enable;

		public Animation(String name, String type, String description,
				String defaultValue) {
			super(name, type, description, defaultValue);
		}
	}

	// @sam added for 3d animation
	public static class Initialization extends Parameter {

		public Initialization(String name, String type, String description,
				String defaultValue) {
			super(name, type, description, defaultValue);
		}
	}

	// @sam added for 3d animation
	public static class Advanced extends Parameter {

		public Advanced(String name, String type, String description,
				String defaultValue) {
			super(name, type, description, defaultValue);
		}
	}

	public static class Result implements Serializable {
		public String name;

		public final String type;

		public String description;

		public final MPrimitive primitiveType;

		public Result(String name, String type, String description,
				MPrimitive primitiveType) {
			this.name = name;
			this.type = type;
			this.description = description;
			this.primitiveType = primitiveType;
		}

		public String getUnit() {
			if (primitiveType != null)
				return primitiveType.getUnit();
			else
				return null;
		}
	}

	public static class ComplexComponent implements Serializable {

	}
}
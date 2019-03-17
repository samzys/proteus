package proteus.gwt.server.modelica;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO;
import proteus.gwt.shared.datatransferobjects.IcongraphicDTO;

import com.infoscient.proteus.Constants;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.Component;
import com.infoscient.proteus.modelica.RefResolver;
import com.infoscient.proteus.modelica.AnnotationParser.GraphicExpression;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.Argument;
import com.infoscient.proteus.modelica.ClassDef.Comment;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.Equation;
import com.infoscient.proteus.modelica.ClassDef.ExtendsClause;
import com.infoscient.proteus.modelica.ClassDef.NamedArgument;
import com.infoscient.proteus.modelica.Component.Parameter;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.types.ExpressionBackedStringProperty;
import com.infoscient.proteus.types.StringProperty;
import com.infoscient.proteus.ui.util.GraphicConstants;
import com.infoscient.proteus.ui.util.GraphicUtils;
import com.itextpdf.text.Paragraph;

public class DiagramPaser implements GraphicConstants {

	public boolean isDiagram = false;
	private boolean first = true;

	// only get the owned icon graphics
	public List<IcongraphicDTO> getIconGraphics(ClassDef classDef) {
		List<IcongraphicDTO> iconDTOList = new ArrayList<IcongraphicDTO>();

		Component component = (Component) classDef;
		Parameter[] paremeters = component.getParameters();
		if (paremeters != null) {
			for (Parameter paremeter : paremeters) {
				classDef.paramList.add(paremeter);
			}
		}

		List<Annotation> anns = classDef.annotations;
		for (Annotation ann : anns) {
			IcongraphicDTO iconDTO = new IcongraphicDTO();
			OMModification omm;

			String name = "Icon.coordinateSystem.extent";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				String str = (String) strProp.get();
				str = str.replace("=", "").trim();
				iconDTO.setExtent(str);
			}

			name = "Icon.coordinateSystem.preserveAspectRatio";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				String str = (String) strProp.get();
				str = str.replace("=", "").trim();
				iconDTO.setPreserveAspectRatio(str);
			}

			name = "Icon.coordinateSystem.grid";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				String str = (String) strProp.get();
				str = str.replace("=", "").trim();
				iconDTO.setGrid(str);
			}

			name = "Icon.graphics";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				iconDTO.setGraphics((String) strProp.get());
			}
			if (iconDTO.getGraphics() != null) {
				iconDTOList.add(iconDTO);
			}
		}
		return iconDTOList;
	}

	/**
	 * get the diagram graphics
	 * 
	 * @param classDef
	 *            the classDef of this class
	 * @param hasConnect
	 *            true: get the owned diagram graphics false: get the owned,
	 *            included, extended diagram graphics
	 * */
	public DiagramgraphicDTO getDiagramGraphics(ClassDef classDef,
			boolean hasConnect) {
		DiagramgraphicDTO diagramDTO = new DiagramgraphicDTO();

		Component component = (Component) classDef;
		Parameter[] paremeters = component.getParameters();
		if (paremeters != null)
			for (Parameter paremeter : paremeters) {
				classDef.paramList.add(paremeter);
			}

		List<Annotation> anns = classDef.annotations;
		for (Annotation ann : anns) {

			OMModification omm;

			String name = "Diagram.coordinateSystem.extent";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				String str = (String) strProp.get();
				str = str.replace("=", "").trim();
				diagramDTO.setExtent(str);
			}

			name = "Diagram.coordinateSystem.preserveAspectRatio";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				String str = (String) strProp.get();
				str = str.replace("=", "").trim();
				diagramDTO.setPreserveAspectRatio(str);
			}

			name = "Diagram.coordinateSystem.grid";
			omm = (OMModification) ann.map.get(name);
			if (omm != null) {
				StringProperty strProp;
				strProp = new ExpressionBackedStringProperty(omm, name,
						Constants.CATEGORY_CODE, name, false);
				String str = (String) strProp.get();
				str = str.replace("=", "").trim();
				diagramDTO.setGrid(str);

			}
			if (hasConnect) {
				name = "Diagram.graphics";
				omm = (OMModification) ann.map.get(name);
				if (omm != null) {
					StringProperty strProp;
					strProp = new ExpressionBackedStringProperty(omm, name,
							Constants.CATEGORY_CODE, name, false);
					String str = ((String) strProp.get()).trim();
					diagramDTO.getGraphicList().add(str);
				}
			}
		}
		if (!hasConnect) {
			ArrayList<String> diagramList = new ArrayList<String>();
			addAllAnnotations(classDef, new ArrayList<StringProperty>(), null,
					null, diagramList, true);
			diagramDTO.getGraphicList().addAll(diagramList);
		}

		return diagramDTO;
	}

	/**
	 * add the annotation of extends class and component clause to strList
	 * 
	 * @param classDef
	 *            the classDef of this class
	 * @param annList
	 *            the list of stringProperty
	 * @param transform
	 *            the strict to this class from the super class, this class is a
	 *            component clause of super class
	 * @param argList
	 *            the parameter in super class, utilized to assign the boolean
	 *            flag
	 * */
	public void addAllAnnotations(ClassDef classDef,
			ArrayList<StringProperty> annList, ComponentDecl transform,
			List<Argument> argList, ArrayList<String> strList,
			boolean diagramFlag) {
		// System.out.println(classDef.type);

		Component component = (Component) classDef;
		if (classDef == null) {
			return;
		}
		// if (classDef.type.contains("Examples")
		// || !classDef.type.contains("Modelica")) {
		// return;
		// }
		for (Equation equation : component.equations) {
			if (equation.connectClause != null) {
				return;
			}
		}

		// add parameter to classDef
		Parameter[] paremeters = component.getParameters();

		if (paremeters != null && paremeters.length > 0)
			for (Parameter paremeter : paremeters) {
				classDef.paramList.add(paremeter);
			}

		// handle the annotation of this class
		List<Annotation> anns = classDef.annotations;
		for (Annotation ann : anns) {
			OMModification omm;
			if (diagramFlag) {
				omm = (OMModification) ann.map.get("Diagram.graphics");
			} else {
				omm = (OMModification) ann.map.get("Icon.graphics");
			}
			if (omm != null) {
				isDiagram = true;
				StringProperty strProp;
				if (diagramFlag) {
					strProp = new ExpressionBackedStringProperty(omm,
							"Diagram.graphics", Constants.CATEGORY_CODE,
							"Diagram.graphics", false);
				} else {
					strProp = new ExpressionBackedStringProperty(omm,
							"Icon.graphics", Constants.CATEGORY_CODE,
							"Icon.graphics", false);
				}
				if (transform != null) {
					String newStr = convertGraphics(ann, transform, diagramFlag);
					annList.add(strProp);
					strList.add(newStr);
				} else {
					strList.add((String) strProp.get());
					annList.add(strProp);
				}

			}
		}

		// handle the extends class
		for (ExtendsClause ext : classDef.extendsList) {
			String typeName = (String) ext.name.get();

			List<Argument> extendsArgList = null;
			if (ext.argList != null) {
				extendsArgList = ext.argList;
			}
			ClassDef superClassdef = RefResolver.forName(typeName);

			addAllAnnotations(superClassdef, annList, transform,
					extendsArgList, strList, diagramFlag);
		}

		// whether included connect
		if (first) {
			first = false;
			for (Equation equation : ((Component) classDef).equations) {
				if (equation.connectClause != null) {
					diagramFlag = false;
					System.out.println("***diagramFlag = false");
					break;
				}
				// System.out.println("&&&&&&&&&&&&&");
			}
		}

		// handle the component clause
		for (ComponentClause comp : classDef.compList) {

			String typeName = (String) comp.typeName.get();
			if (RefResolver.primitiveSet.contains(typeName)) {
				continue;
			}
			ClassDef superClassdef = RefResolver.forName(typeName);
			for (ComponentDecl compdecl : comp.declList) {
				Comment comment = compdecl.comment;
				if (comment != null) {

					// set boolean variable if exist
					if (comment.annotation != null) {
						boolean ifFlag = true;
						if (compdecl.booleanVariable != null) {
							String booleanStr = compdecl.booleanVariable;
							// System.out.println(varName);
							// System.out.println(classDef.paramList.size());

							for (Parameter p : classDef.paramList) {
								if (p.name.equals(booleanStr)) {

									ifFlag = p.defaultValue.equals("true") ? true
											: false;
								}
							}
							if (argList != null) {
								for (Argument argument : argList) {
									if (argument.name.equals(booleanStr)) {

										String value = (String) argument.modification.expression.value
												.get();
										ifFlag = value.equals("true") ? true
												: false;
										// System.out.println(argument.name);
										// System.out.println(value);
									}
								}
							}
							if (compdecl.notSign) {
								ifFlag = !ifFlag;
							}
						}

						if (ifFlag) {
							// System.out.println(typeName);
							addAllAnnotations(superClassdef, annList, compdecl,
									null, strList, diagramFlag);

						}

					}// annotation
				}// comment
			}// ComponentDecl

		}// end of component list

	}

	/**
	 * @param classDefAnn
	 *            the annotation of this class
	 * @param component
	 *            the strict to this class from the super class, this class is a
	 *            component clause of super class
	 * */
	private String convertGraphics(Annotation classDefAnn,
			ComponentDecl component, boolean isDiagram) {

		// set the newRange and oldRange
		String varName = (String) component.varName.get();
		Annotation componentAnn = component.comment.annotation;

		String newExtent = ((OMModification) componentAnn.map
				.get("Placement.transformation.extent")).expression.toCode();
		String oldExtent = "";
		if (isDiagram) {
			oldExtent = ((OMModification) classDefAnn.map
					.get("Diagram.coordinateSystem.extent")).expression
					.toCode();
		} else {
			oldExtent = ((OMModification) classDefAnn.map
					.get("Icon.coordinateSystem.extent")).expression.toCode();
		}
		Range newRange = parseToRange(newExtent);
		Range oldRange = parseToRange(oldExtent);
		// System.out.println(varName);
		// System.out.println(newExtent);
		// System.out.println(oldExtent);

		if (componentAnn.map.get("Placement.transformation.origin") != null) {
			String origin = ((OMModification) componentAnn.map
					.get("Placement.transformation.origin")).expression
					.toCode();

			Point originPoint = GraphicUtils.parsePoint(origin);

			newRange.cx += originPoint.x;
			newRange.cy += originPoint.y;

		}

		// set the angle
		Integer angle = null;
		// TODO: to modify the angle
		if (componentAnn.map.get("Placement.transformation.rotation") != null) {
			String rotation = ((OMModification) componentAnn.map
					.get("Placement.transformation.rotation")).expression
					.toCode();
			angle = Integer.parseInt(rotation);
			if (needReverse(newExtent)) {
				angle = (angle + 180) % 360;
			}
			// System.out.println(originPoint);
		}

		//
		OMModification omm;
		if (isDiagram) {
			omm = (OMModification) classDefAnn.map.get("Diagram.graphics");
		} else {
			omm = (OMModification) classDefAnn.map.get("Icon.graphics");
		}
		String ret = "";
		List<GraphicExpression> graphicExpressions = null;
		graphicExpressions = GraphicExpression
				.parseGraphic((String) omm.expression.toCode());
		ret += "={";
		for (int i = 0; i < graphicExpressions.size(); i++) {
			// each Line/Polygon/Rectangle/Ellipse/Text/Bitmap

			GraphicExpression exp = graphicExpressions.get(i);
			if (exp.name.equals(LINE_NAME)) {
				ret += "Line(";

				for (int j = 0; j < exp.namedArguments.size(); j++) {
					// each varible in Line
					// each varible in Line
					NamedArgument arg = exp.namedArguments.get(j);
					String value = arg.expression.getObjectModel().toCode();
					String name = arg.name;
					if (arg.name.equals("extent")) {
						Point[] points = GraphicUtils.parsePoints(value);
						ret += "extent={";
						for (int k = 0; k < points.length; k++) {
							// each Point
							Point point = points[k];
							point = parseToNewPoint(point, newRange, oldRange,
									angle);
							ret += "{" + point.x + "," + point.y + "}";
							// System.out.println("Point:" + p);
							if (k != points.length - 1) {
								ret += ",";
							}
						}
						ret += "}";
					} else {
						ret += name + "=" + value;
					}
					if (j != exp.namedArguments.size() - 1) {
						ret += ",";

					}
				}
				ret += ")";

			} else if (exp.name.equals(POLYGON_NAME)) {
				ret += "Polygon(";
				for (int j = 0; j < exp.namedArguments.size(); j++) {
					// each varible in Line
					// each varible in Line
					NamedArgument arg = exp.namedArguments.get(j);
					String value = arg.expression.getObjectModel().toCode();
					String name = arg.name;
					if (arg.name.equals("points")) {
						Point[] points = GraphicUtils.parsePoints(value);
						ret += "points={";
						for (int k = 0; k < points.length; k++) {
							// each Point
							Point point = points[k];
							point = parseToNewPoint(point, newRange, oldRange,
									angle);
							ret += "{" + point.x + "," + point.y + "}";
							// System.out.println("Point:" + p);
							if (k != points.length - 1) {
								ret += ",";
							}
						}
						ret += "}";
					} else {
						ret += name + "=" + value;
					}
					if (j != exp.namedArguments.size() - 1) {
						ret += ",";

					}
				}
				ret += ")";

			} else if (exp.name.equals(RECTANGLE_NAME)) {
				ret += "Rectangle(";
				for (int j = 0; j < exp.namedArguments.size(); j++) {
					// each varible in Line
					NamedArgument arg = exp.namedArguments.get(j);
					String value = arg.expression.getObjectModel().toCode();
					String name = arg.name;
					if (arg.name.equals("extent")) {
						Point[] points = GraphicUtils.parsePoints(value);
						ret += "extent={";
						for (int k = 0; k < points.length; k++) {
							// each Point
							Point point = points[k];
							point = parseToNewPoint(point, newRange, oldRange,
									angle);
							ret += "{" + point.x + "," + point.y + "}";
							// System.out.println("Point:" + p);
							if (k != points.length - 1) {
								ret += ",";
							}
						}
						ret += "}";
					} else {
						ret += name + "=" + value;
					}
					if (j != exp.namedArguments.size() - 1) {
						ret += ",";

					}
				}
				ret += ")";

			} else if (exp.name.equals(ELLIPSE_NAME)) {
				// ret += "Ellipse";
				ret += "Ellipse(";
				for (int j = 0; j < exp.namedArguments.size(); j++) {
					// each varible in Line
					NamedArgument arg = exp.namedArguments.get(j);
					String value = arg.expression.getObjectModel().toCode();
					String name = arg.name;
					if (arg.name.equals("extent")) {
						Point[] points = GraphicUtils.parsePoints(value);
						ret += "extent={";
						for (int k = 0; k < points.length; k++) {
							// each Point
							Point point = points[k];
							point = parseToNewPoint(point, newRange, oldRange,
									angle);
							ret += "{" + point.x + "," + point.y + "}";
							// System.out.println("Point:" + p);
							if (k != points.length - 1) {
								ret += ",";
							}
						}
						ret += "}";
					} else {
						ret += name + "=" + value;
					}
					if (j != exp.namedArguments.size() - 1) {
						ret += ",";

					}
				}
				ret += ")";
			} else if (exp.name.equals(TEXT_NAME)) {
				ret += "Text(";
				for (int j = 0; j < exp.namedArguments.size(); j++) {
					// each varible in Line
					NamedArgument arg = exp.namedArguments.get(j);
					String value = arg.expression.getObjectModel().toCode();
					String name = arg.name;

					if (arg.name.equals("extent")) {

						Point[] points = GraphicUtils.parsePoints(value);
						ret += "extent={";
						for (int k = 0; k < points.length; k++) {
							// each Point
							Point point = points[k];
							point = parseTextToNewPoint(point, newRange,
									oldRange, angle);
							ret += "{" + point.x + "," + point.y + "}";
							// System.out.println("Point:" + p);
							if (k != points.length - 1) {
								ret += ",";
							}
						}
						ret += "}";
					} else if (name.equals("textString")
							&& value.contains("%name")) {
						ret += name + "=\"" + varName + "\"";
					} else {
						ret += name + "=" + value;
					}

					if (j != exp.namedArguments.size() - 1) {
						ret += ",";

					}
				}
				ret += ")";

			} else if (exp.name.equals(BITMAP_NAME)) {
				ret += "Bitmap(";
				for (int j = 0; j < exp.namedArguments.size(); j++) {
					// each varible in Line
					// each varible in Line
					NamedArgument arg = exp.namedArguments.get(j);
					String value = arg.expression.getObjectModel().toCode();
					String name = arg.name;
					if (arg.name.equals("points")) {
						Point[] points = GraphicUtils.parsePoints(value);
						ret += "points={";
						for (int k = 0; k < points.length; k++) {
							// each Point
							Point point = points[k];
							point = parseToNewPoint(point, newRange, oldRange,
									angle);
							ret += "{" + point.x + "," + point.y + "}";
							// System.out.println("Point:" + p);
							if (k != points.length - 1) {
								ret += ",";
							}
						}
						ret += "}";
					} else {
						ret += name + "=" + value;
					}
					if (j != exp.namedArguments.size() - 1) {
						ret += ",";

					}
				}
				ret += ")";

			}
			if (i != graphicExpressions.size() - 1) {
				ret += ",";
			}
		}
		ret += "}";
		return ret;

		// return newStrProp;
	}

	// added by @Gao Lei Dec.2 2010
	public Point parseToNewPoint(Point point, Range newRange, Range oldRange,
			Integer rotation) {
		Point ret = new Point();
		Point newPoint = new Point();
		newPoint.x = point.x;
		newPoint.y = point.y;
		if (rotation != null) {
			int r = rotation.intValue();
			r = 360 - r;
			double angle = r * 2.0 * Math.PI / 360.0;
			int cos = (int) Math.cos(angle);
			int sin = (int) Math.sin(angle);
			newPoint.x = cos * point.x + sin * point.y;
			newPoint.y = cos * point.y - sin * point.x;
			// System.out.println(newPoint.x + "  " + newPoint.y);
		}
		ret.x = newRange.w * newPoint.x / oldRange.w
				+ (newRange.cx - oldRange.cx);
		ret.y = newRange.h * newPoint.y / oldRange.h
				+ (newRange.cy - oldRange.cy);

		return ret;
	}

	public Point parseTextToNewPoint(Point point, Range newRange,
			Range oldRange, Integer rotation) {
		Point ret = new Point();
		Point newPoint = new Point();
		newPoint.x = point.x;
		newPoint.y = point.y;
		// if (rotation != null) {
		// int r = rotation.intValue();
		// r = 360 - r;
		// double angle = r * 2.0 * Math.PI / 360.0;
		// int cos = (int) Math.cos(angle);
		// int sin = (int) Math.sin(angle);
		// newPoint.x = cos * point.x + sin * point.y;
		// newPoint.y = cos * point.y - sin * point.x;
		// // System.out.println(newPoint.x + "  " + newPoint.y);
		// }
		ret.x = newRange.w * newPoint.x / oldRange.w
				+ (newRange.cx - oldRange.cx);
		ret.y = newRange.h * newPoint.y / oldRange.h
				+ (newRange.cy - oldRange.cy);

		return ret;
	}

	private boolean needReverse(String extent) {
		boolean ret = false;
		Point[] points = GraphicUtils.parsePoints(extent);
		if (points[0].x > points[1].x) {
			ret = true;
			// System.out.println(extent);
		}

		return ret;
	}

	private Range parseToRange(String extent) {
		java.awt.Rectangle rect = GraphicUtils.parseRectangle(extent);
		Range range = new Range();
		range.w = rect.width;
		range.h = rect.height;
		range.cx = rect.x + rect.width / 2;
		range.cy = rect.y + rect.height / 2;
		return range;
	}

	// Ractangel with center
	public class Range {
		public int cx;
		public int cy;
		public int w;
		public int h;
	}

	// String name = "Modelica.Electrical.Analog.Basic.Resistor";
	public static void main(String[] args) {

		DiagramPaser cp = new DiagramPaser();
		// System.out.println("Build library started");
		// new RemoteUA().buildLibrary();
		// System.out.println("Build library finished");
		// while (true) {
		// try {
		// Range newRange = cp.parseToRange("{{-110,-10},{-90,10}}");
		// Range oldRange = cp.parseToRange("{{-100,-100},{100,100}}");
		//
		// Scanner scanner = new Scanner(System.in);
		//
		// System.out.println("input:");
		//
		// String name = scanner.nextLine();
		// System.out.println(name);
		// ClassDef classdef = RefResolver.forName(name);
		// System.out.println(classdef.type);
		// cp.modifyExtend(classdef, newRange, oldRange);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		// ClassDefPaser cp = new ClassDefPaser();

		// Point oldPoint = new Point(0, 0);
		// Point newPoint = cp.parseToNewExtend(oldPoint, oldRange, newRange);
		// System.out.println(newPoint);

		cp.needReverse("{{-140,-20},{-180,20}}");
	}
}

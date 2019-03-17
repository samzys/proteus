package com.infoscient.proteus.modelica;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.infoscient.proteus.Constants;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.Placement.Transformation;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.types.ClassModificationBackedStringProperty;
import com.infoscient.proteus.modelica.types.ExpressionBackedStringProperty;
import com.infoscient.proteus.types.StringProperty;
//import com.infoscient.proteus.ui.canvas.Icon;
import com.infoscient.proteus.ui.util.MiscUtils;

public class Connector implements Serializable {
	public StringProperty name;

	public final String prefix;

	// as [x1, y1, x2, y2]
	// public int[] extent;
	// 09 Oct 09 lt
	protected Transformation transformation;

	public ClassDef classDef;

	public ClassDef parentClassDef;

//	protected Icon icon, diagram;

	public boolean notSign = false;
	public String booleanVariable;
	
	public Connector(StringProperty name, ClassDef classDef,
			ClassDef parentClassDef) {
		this(name, null, classDef, parentClassDef);
	}

	public Connector(StringProperty name, String prefix, ClassDef classDef,
			ClassDef parentClassDef) {
		this(name, null, null, classDef, parentClassDef, null);
	}

	public Connector(StringProperty name, String prefix,
			Transformation transformation, ClassDef classDef,
			ClassDef parentClassDef, String booleanVar) {
		this.name = name;
		this.prefix = prefix;
		this.transformation = transformation;
		this.classDef = classDef;
		this.parentClassDef = parentClassDef;
		this.booleanVariable = booleanVar;
	}

//	public Icon getIcon() {
//		ClassDef classDef = this.classDef;
//		List<Annotation> anns = new LinkedList<Annotation>();
//		while (icon == null && classDef != null) {
//			anns.addAll(classDef.annotations);
//			if (classDef.extendsName != null) {
//
//				classDef = RefResolver.forName(classDef.extendsName);
//
//			} else {
//				classDef = null;
//			}
//		}
//
//		for (Annotation ann : anns) {
//			OMModification m = (OMModification) ann.map.get("Icon.graphics");
//			if (m != null) {
//				StringProperty strProp = new ClassModificationBackedStringProperty(
//						m, "Icon.graphics", Constants.CATEGORY_CODE,
//						"Icon.graphics", false);
//				icon = new Icon.ModelicaImpl(strProp, null, this.classDef);
//			}
//			m = (OMModification) ann.map.get("Icon.coordinateSystem.extent");
//			int[] extent = null;
//			if (m != null && m.expression != null) {
//				extent = MiscUtils.parseExtent(m.expression.toCode());
//			}
//			if (icon != null && extent != null) {
//				((Icon.ModelicaImpl) icon).setExtent(extent);
//			}
//		}
//		// Couldn't find icon in annotation block
//		if (icon == null) {
//			icon = new Icon.DefaultImpl((String) name.get());
//		}
//		return icon;
//	}

	// public Icon getIcon() {
	// ClassDef classDef = this.classDef;
	// while (icon == null && classDef != null) {
	// List<Annotation> anns = classDef.annotations;
	// for (Annotation ann : anns) {
	// OMModification m = (OMModification) ann.map.get("Icon.graphics");
	// if (m != null) {
	// StringProperty strProp = new ClassModificationBackedStringProperty(
	// m , "Icon.graphics", Constants.CATEGORY_CODE,
	// "Icon.graphics", false);
	// icon = new Icon.ModelicaImpl(strProp, null);
	// }
	// m = (OMModification) ann.map.get("Icon.coordinateSystem.extent");
	// int [] extent = null;
	// if (m != null && m.expression != null) {
	// extent = MiscUtils.parseExtent(m.expression.toCode());
	// }
	// if (icon != null && extent != null) {
	// ((Icon.ModelicaImpl)icon).extent = extent;
	// }
	// }
	//
	// if (classDef.extendsName != null) {
	// if (!classDef.extendsName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
	// classDef = RefResolver.forName(RefResolver.getResolvedName(classDef.type
	// + "#" + classDef.extendsName));
	// } else {
	// classDef = RefResolver.forName(classDef.extendsName);
	// }
	// } else {
	// classDef = null;
	// }
	// }
	// // Couldn't find icon in annotation block
	// if (icon == null) {
	// icon = new Icon.DefaultImpl((String) name.get());
	// }
	// return icon;
	// }

	// public Icon getIcon() {
	// ClassDef classDef = this.classDef;
	// while (icon == null && classDef != null) {
	// List<Annotation> anns = classDef.annotations;
	// for (Annotation ann : anns) {
	// OMModification cm = (OMModification) ann.map.get("Coordsys");
	// OMModification im = (OMModification) ann.map.get("Icon");
	// if (im != null) {
	// try {
	// StringProperty coordSysProp = cm == null ? null
	// : new ClassModificationBackedStringProperty(cm,
	// "Coordsys", Constants.CATEGORY_CODE,
	// "Coordsys", false);
	// StringProperty strProp = new ClassModificationBackedStringProperty(
	// im, "Icon", Constants.CATEGORY_CODE, "Icon",
	// false);
	// icon = new Icon.ModelicaImpl(strProp, coordSysProp);
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// if (classDef.extendsName != null) {
	// if (!classDef.extendsName.startsWith(Constants.MODELICA_LIB_PREFIX)) {
	// classDef = RefResolver.forName(RefResolver.getResolvedName(classDef.type
	// + "#" + classDef.extendsName));
	// } else {
	// classDef = RefResolver.forName(classDef.extendsName);
	// }
	// } else {
	// classDef = null;
	// }
	// }
	//		
	// // Couldn't find icon in annotation block
	// if (icon == null) {
	// icon = new Icon.DefaultImpl((String) name.get());
	// }
	// return icon;
	// }
	//
	private boolean diagramCreated;

//	public Icon getDiagram() {
//		ClassDef classDef = this.classDef;
//		if (!diagramCreated) {
//			while (!diagramCreated && classDef != null) {
//				List<Annotation> anns = classDef.annotations;
//				for (Annotation ann : anns) {
//					OMModification m = (OMModification) ann.map
//							.get("Diagram.graphics");
//					if (m != null) {
//						StringProperty strProp = new ExpressionBackedStringProperty(
//								m, "Diagram.graphics", Constants.CATEGORY_CODE,
//								"Diagram.graphics", false);
//						diagram = new Icon.ModelicaImpl(strProp, null,
//								this.classDef);
//						diagram.setIconSize(200, 200);
//						diagramCreated = true;
//					}
//					m = (OMModification) ann.map
//							.get("Diagram.coordinateSystem.extent");
//					int[] extent = null;
//					if (m != null && m.expression != null) {
//						extent = MiscUtils.parseExtent(m.expression.toCode());
//					}
//					if (diagram != null && extent != null) {
//						((Icon.ModelicaImpl) diagram).setExtent(extent);
//					}
//				}
//
//				if (classDef.extendsName != null) {
//
//					classDef = RefResolver.forName(classDef.extendsName);
//
//				} else {
//					classDef = null;
//				}
//			}
//		}
//		return diagram;
//	}

	/*
	 * public Icon getDiagram() { if (!diagramCreated) { diagramCreated = true;
	 * for (Annotation ann : classDef.annotations) { OMModification cm =
	 * (OMModification) ann.map.get("CoordSys"); OMModification dm =
	 * (OMModification) ann.map.get("Diagram"); if (dm != null) { try {
	 * StringProperty coordSysProp = cm == null ? null : new
	 * ClassModificationBackedStringProperty(cm, "CoordSys",
	 * Constants.CATEGORY_CODE, "CoordSys", false); StringProperty strProp = new
	 * ClassModificationBackedStringProperty( dm, "Diagram",
	 * Constants.CATEGORY_CODE, "Diagram", false); diagram = new
	 * Icon.ModelicaImpl(strProp, coordSysProp); diagram.setIconSize(200, 200);
	 * } catch (RuntimeException e) { e.printStackTrace(); } } } } return
	 * diagram; }
	 */

	public int[] getExtent() {
		if (transformation != null && transformation.extent != null) {
			return transformation.extent;
//		} else if (icon != null && icon instanceof Icon.ModelicaImpl) {
//			return ((Icon.ModelicaImpl) icon).getExtent();
		} else {
			return null;
		}
	}

	public int[] getOrigin() {
		if (transformation != null && transformation.origin != null) {
			return transformation.origin;
		} else {
			int[] ori = { 0, 0 };
			return ori;
		}
	}

	public int getRotation() {
		if (transformation != null) {
			return transformation.rotation;
		} else {
			return -1;
		}
	}
}
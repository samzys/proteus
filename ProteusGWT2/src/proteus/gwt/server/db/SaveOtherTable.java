package proteus.gwt.server.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import proteus.gwt.server.businesslogic.ComponentconnectProxy;
import proteus.gwt.server.businesslogic.DiagramgraphicProxy;
import proteus.gwt.server.businesslogic.DiagramgraphicmodificationProxy;
import proteus.gwt.server.businesslogic.DomainComponentProxy;
import proteus.gwt.server.businesslogic.EnumerationProxy;
import proteus.gwt.server.businesslogic.ExtendcomponentProxy;
import proteus.gwt.server.businesslogic.IcongraphicProxy;
import proteus.gwt.server.businesslogic.IncludecomponentProxy;
import proteus.gwt.server.businesslogic.ParameterProxy;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.modelica.DiagramPaser;
import proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO;
import proteus.gwt.shared.datatransferobjects.IcongraphicDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;

import com.infoscient.proteus.modelica.AnnotationParser;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.Component;
import com.infoscient.proteus.modelica.RefResolver;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.Argument;
import com.infoscient.proteus.modelica.ClassDef.Comment;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.ComponentRef;
import com.infoscient.proteus.modelica.ClassDef.Equation;
import com.infoscient.proteus.modelica.ClassDef.ExtendsClause;
import com.infoscient.proteus.modelica.Component.Parameter;
import com.infoscient.proteus.modelica.parser.OMArgument;
import com.infoscient.proteus.modelica.parser.OMEquation;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.types.MEnumeration;

/**
 * @author sam
 *The file to handle database after saveComponentTable.java
 */
public class SaveOtherTable {
	private static boolean debug_flag = false; // when true, only print not
	// insert into
	// database . if update database, must
	// manually delete the data first
	private static boolean saveFromFile_flag = true;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Build library started");
		new LocalUA().buildLibrary();
		System.out.println("Build library finished");
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		String wholename = "Modelica.Electrical.Analog.Basic.Ground";
		wholename = "Modelica.Blocks.Types.SimpleController";
		wholename = "Modelica.Mechanics.MultiBody.Examples.Loops.Utilities.Cylinder_analytic_CAD";
		wholename = "Modelica.Mechanics.Translational.Components.Mass";
		wholename = "Modelica.Electrical.Machines.Examples.AIMC_DOL";
		if (saveFromFile_flag) {
			saveFromFile(em);
		} else {
			save(wholename, null, em);
		}
		em.close();
		emf.close();
	}

	/**
	 * @param args
	 * @throws ComponentNotFoundException
	 */
	public static void save(String wholename, String extent, EntityManager em) {
		DiagramPaser paser = new DiagramPaser();
		Long id;
		try {
			id = DomainComponentProxy.getId(wholename);
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
			return;
		}
		ClassDef classDef = RefResolver.forName(wholename);
		Component component = (Component) classDef;

		// save special enumeration type
		if (classDef.primitiveType != null
				&& classDef.primitiveType instanceof MEnumeration) {
			MEnumeration me = (MEnumeration) classDef.primitiveType;
			if (!debug_flag) {
				EnumerationProxy.save(id, me);
			} else {
				Map<String, String> m = me.getEnumContent();
				System.out
						.println("\n********** Enumeration List  ************");
				System.out.println(me.getComment());
				for (String key : m.keySet()) {
					System.out.println(key + ": " + m.get(key));
				}
			}
		}

		boolean hasConnectFlag = false;
		for (Equation equation : component.equations) {
			if (equation.connectClause != null) {
				hasConnectFlag = true;
			}
		}

		// icon graphics
		List<IcongraphicDTO> iconList = paser.getIconGraphics(classDef);

		// diagram graphics
		DiagramgraphicDTO diagramDTO = paser.getDiagramGraphics(classDef,
				hasConnectFlag);

		// parameter
		Parameter[] parameters = component.getParameters();
		// include
		List<ComponentClause> compList = component.compList;
		// extends
		List<ExtendsClause> extendsList = component.extendsList;
		/*
		 * save to database
		 */
		// connection
		for (Equation equation : classDef.equations) {
			if (equation.connectClause != null) {
				String startwholeName = "";
				String endwholeName = "";
				String value = "";
				String color = "";
				Annotation a = equation.comment.annotation;

				OMEquation equationOM = equation.getObjectModel();
				if (equationOM != null
						&& equationOM.comment != null
						&& equationOM.comment.annotation != null
						&& equationOM.comment.annotation.classModification.argList != null
						&& equationOM.comment.annotation.classModification.argList.argList != null) {
					for (OMArgument arg : equationOM.comment.annotation.classModification.argList.argList) {
						AnnotationParser.parseConAnnotation(arg, a);
					}
				}

				String parametersString = null;
				if (a != null && a.map != null) {
					value = "={Line(";
					for (String key : a.map.keySet()) {
						if (a.map.get(key) == null
								|| ((OMModification) a.map.get(key)).expression == null) {
							continue;
						}
						if (key.startsWith("Line.")) {
							int index = key.indexOf(".");
							String paraName = null;
							if (index != -1) {
								paraName = key.substring(index + 1);
								String values = ((OMModification) a.map
										.get(key)).expression.toCode();
								if (values == null)
									continue;
								if (parametersString == null) {
									parametersString = paraName + "=" + values;
								} else {
									parametersString += "," + paraName + "="
											+ values;
								}
							}
						}
					}
					if (parametersString != null) {
						value += parametersString + ")}";
					}
				}

				ComponentRef cr1 = equation.connectClause.src;
				ComponentRef cr2 = equation.connectClause.dest;
				startwholeName = (String) cr1.ident.get();
				endwholeName = (String) cr2.ident.get();
				while (cr1.componentRef != null) {
					cr1 = cr1.componentRef;
					startwholeName += "." + (String) cr1.ident.get();
				}

				while (cr2.componentRef != null) {
					cr2 = cr2.componentRef;
					endwholeName += "." + (String) cr2.ident.get();
				}

				if (!debug_flag) {
					ComponentconnectProxy.save(id, value, startwholeName,
							endwholeName);
				} else {
					System.out.println("********** Connection ************");
					System.out.println("startPin name:" + startwholeName);
					System.out.println("endPin name:" + endwholeName);
					System.out.println("annotation:" + value);
				}
			}

		}

		if (!debug_flag) {
			IcongraphicProxy.save(id, iconList);
			DiagramgraphicProxy.save(id, diagramDTO);
			DiagramgraphicmodificationProxy.save(id, diagramDTO);
			ParameterProxy.save(id, parameters);
		} else {
			System.out.println("\n********** Icon ************");
			for (IcongraphicDTO value : iconList) {
				System.out.println(value);
			}
			System.out.println("\n********** Diagram ************");

			System.out.println(diagramDTO);

			System.out.println("\n********** Parameter ************");
			if (wholename.equals("Modelica.Blocks.Discrete.Sampler")) {
				System.err.println("&&&&&&&&&&&&&&" + wholename);
			}
			if(parameters!=null)
			for (Parameter value : parameters) {
				if (value.primitiveType == null) {
					continue;
				}
				System.out.println(value.getVariability()+ ", "
						+value.getWholename()+", "+value.name + ", " + value.defaultValue
						+ ", " + value.primitiveType.getStart() + ", "
						+ value.primitiveType.getFixed() + ", "
						+ value.primitiveType.getUnit() + ", "
						+ value.primitiveType.getDisplayUnit() + ", "
						+ value.primitiveType.getQuantity() + ", "
						+ value.primitiveType.getMin() + ", "
						+ value.primitiveType.getMax() + ", "
						+ value.primitiveType.getNominal() + ", "
						+ value.primitiveType.getStateSelect() + ", "
						+ value.getCausality()+ ", "
						+ value.getTyping()+ ", ");
			}
		}

		// include component
		for (ComponentClause comp : compList) {
			String compWholename = (String) comp.typeName.get();
			String compExtent = null;
			String origin = null;
			String rotation = null;
			String varName = null;
			List<String> compArrayList = comp.arrarysubscrpts;
			for (ComponentDecl compdecl : comp.declList) {
				Comment comment = compdecl.comment;
				if (comment != null && comment.annotation != null
						&& comment.annotation.map != null) {
					varName = (String) compdecl.varName.get();

					List<ModificationArgument> modArgList = new ArrayList<ModificationArgument>();
					String arrayForm = "";
					List<String> compdeclArrayList = new ArrayList<String>();
					compdeclArrayList.addAll(compdecl.arrarysubscrpts);
					compdeclArrayList.addAll(compArrayList);

					for (String str : compdeclArrayList) {
						arrayForm += str + ",";
					}
					if (arrayForm != null && !arrayForm.equals("")) {
						arrayForm = arrayForm.substring(0,
								arrayForm.length() - 1);
					}
					if (arrayForm.trim().equals("")) {
						arrayForm = null;
					}

					if (comment.annotation.map
							.get("Placement.transformation.extent") != null) {
						compExtent = ((OMModification) comment.annotation.map
								.get("Placement.transformation.extent")).expression
								.toCode();
					}

					if (comment.annotation.map
							.get("Placement.iconTransformation.extent") != null) {
						compExtent = ((OMModification) comment.annotation.map
								.get("Placement.iconTransformation.extent")).expression
								.toCode();
					}

					if (comment.annotation.map
							.get("Placement.transformation.origin") != null) {
						origin = ((OMModification) comment.annotation.map
								.get("Placement.transformation.origin")).expression
								.toCode();
					}

					if (comment.annotation.map
							.get("Placement.transformation.rotation") != null) {
						rotation = ((OMModification) comment.annotation.map
								.get("Placement.transformation.rotation")).expression
								.toCode();
					}

					// boolean variable
					String ifFlagName = null;
					if (compdecl.booleanVariable != null) {
						ifFlagName = compdecl.booleanVariable;
					}

					if (compdecl.modification != null
							&& compdecl.modification.arguments != null) {
						modArgList = resolveModification(
								compdecl.modification.arguments, modArgList,
								parameters);
					}
					// exception: sam
					if (!compWholename.equals("StateSelect")) {
						if (!debug_flag) {
							try {
								IncludecomponentProxy.save(id, compWholename,
										compExtent, origin, rotation, varName,
										ifFlagName, arrayForm, modArgList);
							} catch (ComponentNotFoundException e) {
								continue;
							}
							save(compWholename, compExtent, em);
						} else {
							System.out
									.println("\n********** ComponentClause ************");
							System.out.println("ComponentClause name:"
									+ compWholename);
							System.out.println("extent:\t" + compExtent);
							System.out.println("origin:\t" + origin);
							System.out.println("rotation:\t" + rotation);
							System.out.println("varName:\t" + varName);

							System.out.println("booleanStr:\t" + ifFlagName);
							System.out.println("arrayForm:\t" + arrayForm);
					
							for (ModificationArgument arguments : modArgList) {
								System.out.println("argument name : "
										+ arguments.getName() + ", "
										+ arguments.getValue() + ", "
										+ arguments.getStart() + ", "
										+ arguments.getFixed() + ", "
										+ arguments.getUnit() + ", "
										+ arguments.getQuantity() + ", "
										+ arguments.getDisplayUnit() + ", "
										+ arguments.getMin() + ", "
										+ arguments.getMax() + ", "
										+ arguments.getNominal() + ", "
										+ arguments.getStateSelect());
							}
						}
					}
				}// if
			}// for ComponentDecl
		}// for ComponentClause

		// extends component
		for (ExtendsClause extend : extendsList) {

			String extendWholename = (String) extend.name.get();
			String extendExtent = null;
			List<ModificationArgument> extendArgList = new ArrayList<ModificationArgument>();

			// the extends argument list
			List<Argument> extendsArgList = extend.argList;
			if (extendsArgList != null) {
				extendArgList = resolveModification(extendsArgList,
						extendArgList, parameters);
			}
			// if (extendsArgList != null && extendsArgList.size() > 1) {
			// System.out.println(wholename);
			// }
			if (!debug_flag) {
				try {
					ExtendcomponentProxy.save(id, extendWholename,
							extendExtent, extendArgList);
				} catch (ComponentNotFoundException e) {
					continue;
				}
				save(extendWholename, extendExtent, em);
			} else {
				System.out.println("\n********** Extend ************");
				System.out.println("extend Wholename :" + extendWholename);
				// Set<Entry<String, String>> extendArgumengts =
				// extendArgumentMap
				// .entrySet();
				for (ModificationArgument arguments : extendArgList) {
					System.out.println("argument name : " + arguments.getName()
							+ arguments.getName() + ", " + arguments.getValue()
							+ ", " + arguments.getStart() + ", "
							+ arguments.getFixed() + ", " + arguments.getUnit()
							+ ", " + arguments.getQuantity() + ", "
							+ arguments.getDisplayUnit() + ", "
							+ arguments.getMin() + ", " + arguments.getMax()
							+ ", " + arguments.getNominal() + ", "
							+ arguments.getStateSelect());
				}
			}
		}
	}

	public static List<ModificationArgument> resolveModification(
			List<Argument> arguments, List<ModificationArgument> modArgList,
			Parameter[] parameters) {
		for (Argument argument : arguments) {
			String name = null, value = null;
			String start = null, fixed = null, unit = null, quantity = null, displayUnit = null, min = null, max = null, nominal = null, stateSelect = null;

			try {
				name = argument.name;
				if (argument.modification.expression != null) {
					value = (String) argument.modification.expression.value
							.get();
				}
				if (argument.modification.arguments != null
						&& argument.modification.arguments.size() > 0) {
					List<Argument> args = argument.modification.arguments;
					for (Argument a : args) {
						String temp = (String) a.modification.expression.value
								.get();
						if (a.name.equals("start")) {
							start = temp;
						} else if (a.name.equals("fixed")) {
							fixed = temp;
						} else if (a.name.equals("unit")) {
							unit = temp;
						} else if (a.name.equals("quantity")) {
							quantity = temp;
						} else if (a.name.equals("displayUnit")) {
							displayUnit = temp;
						} else if (a.name.equals("min")) {
							min = temp;
						} else if (a.name.equals("max")) {
							max = temp;
						} else if (a.name.equals("nominal")) {
							nominal = temp;
						} else if (a.name.equals("stateSelect")) {
							stateSelect = temp;
						}
					}
				}
				if (parameters != null)
					for (Parameter parameter : parameters) {
						if (parameter.name.equals(value)) {
							value = parameter.defaultValue;
						} else if (parameter.name.equals(start)) {
							start = parameter.defaultValue;
						} else if (parameter.name.equals(fixed)) {
							fixed = parameter.defaultValue;
						} else if (parameter.name.equals(unit)) {
							unit = parameter.defaultValue;
						} else if (parameter.name.equals(displayUnit)) {
							displayUnit = parameter.defaultValue;
						} else if (parameter.name.equals(min)) {
							min = parameter.defaultValue;
						} else if (parameter.name.equals(max)) {
							max = parameter.defaultValue;
						} else if (parameter.name.equals(nominal)) {
							nominal = parameter.defaultValue;
						} else if (parameter.name.equals(stateSelect)) {
							stateSelect = parameter.defaultValue;
						} else if (parameter.name.equals(quantity)) {
							quantity = parameter.defaultValue;
						}
					}

			} catch (NullPointerException e) {
				continue;
			}
			if (name != null
					&& (value != null || start != null || fixed != null
							|| quantity != null || unit != null
							|| displayUnit != null || min != null
							|| max != null || nominal != null || stateSelect != null)) {
				modArgList.add(new ModificationArgument(name, value, start,
						fixed, min, max, unit, displayUnit, quantity, nominal,
						stateSelect));
			}
		}
		return modArgList;
	}

	public static void saveFromFile(EntityManager em)
			throws FileNotFoundException {
		String wholename = "";
		String filePath = System.getProperty("user.dir")
				+ "/src/proteus/gwt/server/db/mslList";
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		try {
			while ((wholename = br.readLine()) != null) {
				System.out.println(wholename);
				if (!wholename.contains("Modelica")) {
					continue;
				}

				save(wholename, null, em);
				// System.out.println(wholename);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testContains() {
		// EntityManagerFactory emf = Persistence
		// .createEntityManagerFactory("ProteusGWT");
		// EntityManager em = emf.createEntityManager();
		// Long id = 1L;
		// System.out.println(DiagramgraphicProxy.contains(id, em));
		// System.out.println(ExtendcomponentProxy.contains(id, em));
		// System.out.println(IcongraphicProxy.contains(id, em));
		// System.out.println(IncludecomponentProxy.contains(id, em));
		// System.out.println(ParameterProxy.contains(id, em));
	}

}

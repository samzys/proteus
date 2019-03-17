package proteus.gwt.server.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import proteus.gwt.server.businesslogic.DomainModelProxy2;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;
import proteus.gwt.shared.datatransferobjects.ModelincludeDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;

import com.infoscient.proteus.ProteusParserInterfaceImpl;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.RefResolver;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.Comment;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.ComponentRef;
import com.infoscient.proteus.modelica.ClassDef.Equation;
import com.infoscient.proteus.modelica.Component;
import com.infoscient.proteus.modelica.parser.OMModification;

public class MOFileParser {
	private EntityManagerFactory emf;

	public MOFileParser() {
	}

	public MOFileParser(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * @param classDef
	 * @param qqnewName
	 * @param qqnewDescription
	 * @return
	 * @throws ComponentNotFoundException
	 * @sam compare this with public static void save(String wholename, String
	 *      extent, EntityManager em) in SaveOtherTable 
	 *      
	 *   	TODO add support for
	 *      parameters...though the model develop tool does not support adding
	 *      paramters at the moment, but eventually, it will support adding
	 *      paramters into the codecanvas 16 Apr, 2012 sam
	 */
	private long parser(ClassDef classDef, String qqnewName,
			String qqnewDescription) throws ComponentNotFoundException {
		long ret = -1;
		Component component = (Component) classDef;
		List<ComponentClause> compList = component.compList;

		String name = component.getName().get().toString();

		List<ConnectDTO> connectList = new ArrayList<ConnectDTO>();
		List<ModelincludeDTO> includeList = new ArrayList<ModelincludeDTO>();

		// connection
		for (Equation equation : classDef.equations) {
			if (equation.connectClause != null) {
				String startwholeName = "";
				String endwholeName = "";
				String value = "";
				String color = "";
				Annotation a = equation.comment.annotation;

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

				ConnectDTO connect = new ConnectDTO();
				connect.setValue(value);
				connect.setStartpin(startwholeName);
				connect.setEndpin(endwholeName);
				connectList.add(connect);

				if (debug_flag) {
					System.out.println("********** Connection ************");
					System.out.println("name:" + name);
					System.out.println("startPin name:" + startwholeName);
					System.out.println("endPin name:" + endwholeName);
					System.out.println("annotation:" + value);
				}
			}
		}

		// include component
		for (ComponentClause comp : compList) {

			String compWholename = (String) comp.typeName.get();
			String compExtent = null;
			String origin = null;
			String rotation = null;
			String varName = null;
			String protected_ = (comp.protected_ != null) ? (String) comp.protected_
					.get() : null;
			String final_ = (comp.final_ != null) ? ((Boolean) comp.final_
					.get() ? "final" : null) : null;
			String typing = (comp.typing != null) ? (String) comp.typing.get()
					: null;
			String varibility = (comp.variability != null) ? (String) comp.variability
					.get() : null;
			String causality = (comp.causality != null) ? (String) comp.causality
					.get() : null;

			List<String> compArrayList = comp.arrarysubscrpts;
			for (ComponentDecl compdecl : comp.declList) {
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
					arrayForm = arrayForm.substring(0, arrayForm.length() - 1);
				}
				if (arrayForm.trim().equals("")) {
					arrayForm = null;
				}

				Comment comment = compdecl.comment;
				if (comment != null && comment.annotation != null
						&& comment.annotation.map != null) {

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
					// TODO tobe removed later
					// check whether the oldway annotation is used or not?
					if (compExtent == null && origin == null
							&& rotation == null) {
						if (comment.annotation.map.get("extent") != null) {
							String oldExt = ((OMModification) comment.annotation.map
									.get("extent")).expression.toCode();
							compExtent = "{{-30, -30},{30, 30}}";

						}
					}
				}// end of checking comments

				// boolean variable
				String ifFlagName = null;
				if (compdecl.booleanVariable != null) {
					ifFlagName = compdecl.booleanVariable;
				}

				if (compdecl.modification != null
						&& compdecl.modification.arguments != null) {
					modArgList = SaveOtherTable.resolveModification(
							compdecl.modification.arguments, modArgList, null);
				}

				// TODO tobe removed
				if (compExtent == null)
					compExtent = "{{-30, -30},{30, 30}}";

				// compExtent = convertExtent(compExtent);
				// origin = convertOrigin(origin);
				ModelincludeDTO include = new ModelincludeDTO();
				include.setVarName(varName);
				include.setTypeName(compWholename);
				include.setExtent(compExtent);
				include.setOrigin(origin);
				include.setRotation(rotation);
				include.setModification(modArgList);
				include.setCausality(causality);
				include.setVariability(varibility);
				include.setFinal_(final_);
				include.setProtected_(protected_);
				include.setTyping(typing);
				include.setArrayForm(arrayForm);
				include.setIfFlagName(ifFlagName);
				includeList.add(include);

				if (debug_flag) {
					System.out
							.println("\n********** ComponentClause ************");
					System.out.println("ComponentClause name:" + compWholename);
					System.out.println("varName:" + varName);
					System.out.println("procted: " + protected_);
					System.out.println("final_: " + final_);
					System.out.println("typing: " + typing);
					System.out.println("varibility: " + varibility);
					System.out.println("causality: " + causality);
					System.out.println("booleanStr:" + ifFlagName);
					System.out.println("arrayForm:" + arrayForm);
					System.out.println("compExtent:" + compExtent);
					System.out.println("origin:" + origin);
					System.out.println("rotation:" + rotation);

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
			}// for ComponentDecl
		}// for ComponentClause
			// System.out.println(includeList.size());
		if (!debug_flag) {
			ret = DomainModelProxy2.save(name, connectList, includeList);
		}
		return ret;
	}

	/*
	 * return: <=0 failed >0 successful
	 */

	/**
	 * @param filePath
	 * @param name
	 * @param description
	 * @return
	 * @throws IOException
	 * @by defalut. the return value >0 if return ==-2 then parser error if
	 *     return ==-1 then component not found error
	 */
	public long parser(String filePath, String name, String description)
			throws IOException {
		long ret = -1;
		String template = read(filePath);
		ProteusParserInterfaceImpl ppi = new ProteusParserInterfaceImpl();
		ClassDef[] classDefArray = ppi.paserMO(template);

		if (classDefArray == null || classDefArray.length != 1) {
			ret = -2;
		} else {
			ClassDef classDef = classDefArray[0];
			RefResolver._nameLookup3(classDef);
			try {
				ret = parser(classDef, name, description);
			} catch (ComponentNotFoundException e) {
				ret = -1;
			}
		}
		return ret;
	}

	public long parser(long mid, String filePath, String name,
			String description) throws IOException {
		long ret = 0;
		String template = read(filePath);
		ProteusParserInterfaceImpl parser = new ProteusParserInterfaceImpl();
		ClassDef[] classDefArray = parser.paserMO(template);

		if (classDefArray == null || classDefArray.length != 1) {
			ret = -2;
		} else {
			ClassDef classDef = classDefArray[0];
			try {
				ret = parser(mid, classDef, name, description);
			} catch (ComponentNotFoundException e) {
				ret = -1;
			}
		}
		return ret;
	}

	public long parser(long mid, ClassDef classDef, String newName,
			String newDescription) throws ComponentNotFoundException {
		long ret = 0;
		Component component = (Component) classDef;
		List<ComponentClause> compList = component.compList;

		String name = component.getName().get().toString();
		if (newName != null && !newName.trim().equals("")) {
			name = newName;
		}

		List<ConnectDTO> connectList = new ArrayList<ConnectDTO>();
		List<ModelincludeDTO> includeList = new ArrayList<ModelincludeDTO>();

		// connection
		for (Equation equation : classDef.equations) {
			if (equation.connectClause != null) {
				String startwholeName = "";
				String endwholeName = "";
				String value = "";
				String color = "";
				Annotation a = equation.comment.annotation;

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

				ConnectDTO connect = new ConnectDTO();
				connect.setValue(value);
				connect.setStartpin(startwholeName);
				connect.setEndpin(endwholeName);
				connectList.add(connect);

				if (debug_flag) {
					System.out.println("********** Connection ************");
					System.out.println("name:" + name);
					System.out.println("startPin name:" + startwholeName);
					System.out.println("endPin name:" + endwholeName);
					System.out.println("annotation:" + value);
				}
			}
		}

		// include component
		for (ComponentClause comp : compList) {

			String compWholename = (String) comp.typeName.get();
			String compExtent = null;
			String origin = null;
			String rotation = null;
			String varName = null;

			String protected_ = (comp.protected_ != null) ? (String) comp.protected_
					.get() : null;
			String final_ = (comp.final_ != null) ? ((Boolean) comp.final_
					.get() ? "final" : null) : null;
			String typing = (comp.typing != null) ? (String) comp.typing.get()
					: null;
			String varibility = (comp.variability != null) ? (String) comp.variability
					.get() : null;
			String causality = (comp.causality != null) ? (String) comp.causality
					.get() : null;

			List<String> compArrayList = comp.arrarysubscrpts;
			for (ComponentDecl compdecl : comp.declList) {
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
					arrayForm = arrayForm.substring(0, arrayForm.length() - 1);
				}
				if (arrayForm.trim().equals("")) {
					arrayForm = null;
				}

				Comment comment = compdecl.comment;
				if (comment != null && comment.annotation != null
						&& comment.annotation.map != null) {

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
					// TODO tobe removed later
					// check whether the oldway annotation is used or not?
					if (compExtent == null && origin == null
							&& rotation == null) {
						if (comment.annotation.map.get("extent") != null) {
							String oldExt = ((OMModification) comment.annotation.map
									.get("extent")).expression.toCode();
							compExtent = "{{-30, -30},{30, 30}}";

						}
					}
				}// end of checking comments

				// boolean variable
				String ifFlagName = null;
				if (compdecl.booleanVariable != null) {
					ifFlagName = compdecl.booleanVariable;
				}

				if (compdecl.modification != null
						&& compdecl.modification.arguments != null) {
					modArgList = SaveOtherTable.resolveModification(
							compdecl.modification.arguments, modArgList, null);
				}

				// TODO tobe removed
				if (compExtent == null)
					compExtent = "{{-30, -30},{30, 30}}";

				// compExtent = convertExtent(compExtent);
				// origin = convertOrigin(origin);
				ModelincludeDTO include = new ModelincludeDTO();
				include.setVarName(varName);
				include.setTypeName(compWholename);
				include.setExtent(compExtent);
				include.setOrigin(origin);
				include.setRotation(rotation);
				include.setModification(modArgList);
				include.setCausality(causality);
				include.setVariability(varibility);
				include.setFinal_(final_);
				include.setProtected_(protected_);
				include.setTyping(typing);
				includeList.add(include);

				if (debug_flag) {
					System.out
							.println("\n********** ComponentClause ************");
					System.out.println("ComponentClause name:" + compWholename);
					System.out.println("varName:" + varName);
					System.out.println("booleanStr:" + ifFlagName);
					System.out.println("arrayForm:" + arrayForm);
					System.out.println("compExtent:" + compExtent);
					System.out.println("origin:" + origin);
					System.out.println("rotation:" + rotation);

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
			}// for ComponentDecl
		}// for ComponentClause
			// System.out.println(includeList.size());
		if (!debug_flag) {
			// determine save or update operation for the databases;
			ret = DomainModelProxy2.update(mid, name, connectList, includeList);
		}
		return ret;
	}

	public String read(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String strLine = "";
		StringBuffer allLine = new StringBuffer();
		while ((strLine = br.readLine()) != null) {
			allLine.append(strLine + "\n");
		}
		fr.close();
		br.close();
		return allLine.toString();
	}

	// private String convertExtent(String extent) {
	// String ret = extent.replace(" ", "");
	// ret = ret.substring(1, ret.length() - 1);
	// String a[] = ret.split(",");
	// ret = "{{" + a[0] + "," + a[1] + "},{" + a[2] + "," + a[3] + "}}";
	// return ret;
	// }
	//
	// private String convertOrigin(String extent) {
	// String ret = extent.replace(" ", "").replace("[", "{")
	// .replace("]", "}");
	// return ret;
	// }

	static boolean debug_flag = false;

	public static void main(String[] args) throws IOException {

		String path = "D:\\My Documents\\projects\\modelica\\modelica library\\3.1-build-6\\Modelica 3.1\\Electrical\\Analog\\Examples\\ChuaCircuit.mo";
		path = "C:\\Users\\sam\\Downloads\\AIMC_DOL.mo";

		MOFileParser parser = new MOFileParser(null);
		System.out.println("parser return:" + parser.parser(path, "dfd", ""));

		// String extend="[-32, -32, 32, 32]";
		// System.out.println(convertExtent(extend));
		// String origin="[370, 244]";
		// System.out.println(convertOriginal(origin));

		// System.out.println("begin");
		// EntityManagerFactory emf = Persistence
		// .createEntityManagerFactory("ProteusGWT");// this is cost much
		// time.about 5 seconds
		// System.out.println("EntityManagerFactory");
		// EntityManager em = emf.createEntityManager();
		// System.out.println("EntityManager");
		// EntityTransaction tx = em.getTransaction();
		// System.out.println("EntityTransaction");
		// tx.begin();
		// System.out.println("tx.begin()");
		//
		// tx.commit();
		// em.close();
		// emf.close();
		// System.out.println("emf.close()");

	}

}

package com.infoscient.proteus.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManagerFactory;

import com.infoscient.proteus.ProteusParserInterfaceImpl;
import com.infoscient.proteus.db.businesslogic.DomainModelProxy;
import com.infoscient.proteus.db.datatransferobjects.ConnectDTO;
import com.infoscient.proteus.db.datatransferobjects.ModelincludeDTO;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.Component;
import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.Argument;
import com.infoscient.proteus.modelica.ClassDef.Comment;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.ComponentRef;
import com.infoscient.proteus.modelica.ClassDef.Equation;
import com.infoscient.proteus.modelica.parser.OMModification;
@Deprecated
public class MOFileParser {
	private EntityManagerFactory emf;

	public MOFileParser() {
	}

	public MOFileParser(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public long parser(ClassDef classDef, String newName, String newDescription)
			throws ComponentNotFoundException {
		long ret = 0;
		Component component = (Component) classDef;
		List<ComponentClause> compList = component.compList;

		String name = component.getName().get().toString();
		if (newName != null && !newName.trim().equals("")) {
			name = newName;
		}
		
		List<ConnectDTO> connectList = new ArrayList<ConnectDTO>();
		List<ModelincludeDTO> includeList = new ArrayList<ModelincludeDTO>();

		//connection
		for (Equation equation : classDef.equations) {
			if (equation.connectClause != null) {
				String startwholeName = "";
				String endwholeName = "";
				String value = "";
				String color = "";
				Annotation a = equation.comment.annotation;

				if (a != null && a.map != null
						&& a.map.get("Line.points") != null) {
					value = ((OMModification) a.map.get("Line.points")).expression
							.toCode();
					value = "={Line(points=" + value;

					if (a.map.get("Line.color") != null) {
						color = ((OMModification) a.map.get("Line.color")).expression
								.toCode();
						value += ",color=" + color;
					}
					value += ")}";
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

		//include component
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
					HashMap<String, String> includeModificationMap = new HashMap<String, String>();
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

					//boolean variable
					String ifFlagName = null;
					if (compdecl.booleanVariable != null) {
						ifFlagName = compdecl.booleanVariable;

					}
					if (compdecl.modification != null) {
						for (Argument argument : compdecl.modification.arguments) {
							String modificationName = null;
							String modificationvalue = null;
							modificationName = argument.name;
							modificationvalue = (String) argument.modification.expression.value
									.get();
							if (modificationName != null
									&& modificationvalue != null) {
								includeModificationMap.put(modificationName,
										modificationvalue);
							}
						}
					}

					//					compExtent = convertExtent(compExtent);
					//					origin = convertOrigin(origin);
					ModelincludeDTO include = new ModelincludeDTO();
					include.setVarName(varName);
					include.setTypeName(compWholename);
					include.setExtent(compExtent);
					include.setOrigin(origin);
					include.setRotation(rotation);
					include.getModification().putAll(includeModificationMap);
					includeList.add(include);

					if (debug_flag) {
						System.out
								.println("\n********** ComponentClause ************");
						System.out.println("ComponentClause name:"
								+ compWholename);
						System.out.println("varName:" + varName);
						System.out.println("booleanStr:" + ifFlagName);
						System.out.println("arrayForm:" + arrayForm);
						System.out.println("compExtent:" + compExtent);
						System.out.println("origin:" + origin);
						System.out.println("rotation:" + rotation);

						Set<Entry<String, String>> includeArgumengts = includeModificationMap
								.entrySet();
						for (Entry<String, String> arguments : includeArgumengts) {
							System.out.println("argument name : "
									+ arguments.getKey());
							System.out.println("argument value : "
									+ arguments.getValue());
						}
					}
				}
			}//for ComponentDecl	
		}//for ComponentClause
		//System.out.println(includeList.size());
		if (!debug_flag) {
			//determine save or update operation for the databases;
				ret = DomainModelProxy.save(name, connectList, includeList);
		}
		return ret;
	}

	/*
	 * return: 
	 * 		<=0 failed
	 * 		>0 successful
	 * */
	public long parser(String filePath, String name, String description)
			throws IOException {
		long ret = 0;
		String template = read(filePath);
		ClassDef[] classDefArray = ProteusParserInterfaceImpl.get().paserMO(template);

		if (classDefArray.length != 1) {
			ret = -2;
		} else {
			ClassDef classDef = classDefArray[0];
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
		ClassDef[] classDefArray = ProteusParserInterfaceImpl.get().paserMO(template);

		if (classDefArray.length != 1) {
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

		//connection
		for (Equation equation : classDef.equations) {
			if (equation.connectClause != null) {
				String startwholeName = "";
				String endwholeName = "";
				String value = "";
				String color = "";
				Annotation a = equation.comment.annotation;

				if (a != null && a.map != null
						&& a.map.get("Line.points") != null) {
					value = ((OMModification) a.map.get("Line.points")).expression
							.toCode();
					value = "={Line(points=" + value;

					if (a.map.get("Line.color") != null) {
						color = ((OMModification) a.map.get("Line.color")).expression
								.toCode();
						value += ",color=" + color;
					}
					value += ")}";
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

		//include component
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
					HashMap<String, String> includeModificationMap = new HashMap<String, String>();
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

					//boolean variable
					String ifFlagName = null;
					if (compdecl.booleanVariable != null) {
						ifFlagName = compdecl.booleanVariable;

					}
					if (compdecl.modification != null) {
						for (Argument argument : compdecl.modification.arguments) {
							String modificationName = null;
							String modificationvalue = null;
							modificationName = argument.name;
							modificationvalue = (String) argument.modification.expression.value
									.get();
							if (modificationName != null
									&& modificationvalue != null) {
								includeModificationMap.put(modificationName,
										modificationvalue);
							}
						}
					}

					//					compExtent = convertExtent(compExtent);
					//					origin = convertOrigin(origin);
					ModelincludeDTO include = new ModelincludeDTO();
					include.setVarName(varName);
					include.setTypeName(compWholename);
					include.setExtent(compExtent);
					include.setOrigin(origin);
					include.setRotation(rotation);
					include.getModification().putAll(includeModificationMap);
					includeList.add(include);

					if (debug_flag) {
						System.out
								.println("\n********** ComponentClause ************");
						System.out.println("ComponentClause name:"
								+ compWholename);
						System.out.println("varName:" + varName);
						System.out.println("booleanStr:" + ifFlagName);
						System.out.println("arrayForm:" + arrayForm);
						System.out.println("compExtent:" + compExtent);
						System.out.println("origin:" + origin);
						System.out.println("rotation:" + rotation);

						Set<Entry<String, String>> includeArgumengts = includeModificationMap
								.entrySet();
						for (Entry<String, String> arguments : includeArgumengts) {
							System.out.println("argument name : "
									+ arguments.getKey());
							System.out.println("argument value : "
									+ arguments.getValue());
						}
					}
				}
			}//for ComponentDecl	
		}//for ComponentClause
		//System.out.println(includeList.size());
		if (!debug_flag) {
			//determine save or update operation for the databases;
			ret = DomainModelProxy.update(mid, name, connectList, includeList);
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
		return allLine.toString();
	}

	//	private String convertExtent(String extent) {
	//		String ret = extent.replace(" ", "");
	//		ret = ret.substring(1, ret.length() - 1);
	//		String a[] = ret.split(",");
	//		ret = "{{" + a[0] + "," + a[1] + "},{" + a[2] + "," + a[3] + "}}";
	//		return ret;
	//	}
	//
	//	private String convertOrigin(String extent) {
	//		String ret = extent.replace(" ", "").replace("[", "{")
	//				.replace("]", "}");
	//		return ret;
	//	}


	static boolean debug_flag = false;
	public static void main(String[] args) throws IOException {

		String path = "D:\\1.mo";
		MOFileParser parser = new MOFileParser(null);
		System.out.println("parser return:" + parser.parser(path, "dfd", ""));

		//		String extend="[-32, -32, 32, 32]";
		//		System.out.println(convertExtent(extend));
		//		String origin="[370, 244]";
		//		System.out.println(convertOriginal(origin));

		//		System.out.println("begin");
		//		EntityManagerFactory emf = Persistence
		//				.createEntityManagerFactory("ProteusGWT");// this is cost much time.about 5 seconds
		//		System.out.println("EntityManagerFactory");
		//		EntityManager em = emf.createEntityManager();
		//		System.out.println("EntityManager");
		//		EntityTransaction tx = em.getTransaction();
		//		System.out.println("EntityTransaction");
		//		tx.begin();
		//		System.out.println("tx.begin()");
		//
		//		tx.commit();
		//		em.close();
		//		emf.close();
		//		System.out.println("emf.close()");

	}

}

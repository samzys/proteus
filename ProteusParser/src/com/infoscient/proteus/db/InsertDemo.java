package com.infoscient.proteus.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.infoscient.proteus.ModelicaExtHandler;
import com.infoscient.proteus.ParseException;
import com.infoscient.proteus.db.businesslogic.DomainComponentProxy;
import com.infoscient.proteus.db.businesslogic.DomainModelProxy;
import com.infoscient.proteus.db.businesslogic.IncludeModelProxy;
import com.infoscient.proteus.db.businesslogic.IncludecomponentProxy;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.Component;
import com.infoscient.proteus.modelica.DiagramPaser;
import com.infoscient.proteus.modelica.ClassDef.Comment;
import com.infoscient.proteus.modelica.ClassDef.ComponentClause;
import com.infoscient.proteus.modelica.ClassDef.ComponentDecl;
import com.infoscient.proteus.modelica.ClassDef.ExtendsClause;
import com.infoscient.proteus.modelica.Component.Parameter;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.types.StringProperty;

/**
 * @author sam
 * 
 */
public class InsertDemo {

	private ModelicaExtHandler handler;

	public InsertDemo() {
		handler = new ModelicaExtHandler();
	}

	public List<ClassDef> readFile(File file) {
		List<ClassDef> list = new LinkedList<ClassDef>();
		try {
			handler.read(file, list);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void main(String[] args) {
		InsertDemo insert = new InsertDemo();

		String name = "D:\\demo\\resources\\springSystem.mo";
		File file = new File(name);

		List<ClassDef> listcd = insert.readFile(file);
		ClassDef classDef = null;
		if (listcd.size() > 0)
			classDef = listcd.get(0);

		if (classDef == null)
			return;

		Component component = (Component) classDef;

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		saveDemo(component, em);
		em.close();
		emf.close();
	}

	private static void saveDemo(Component component, EntityManager em) {
		DiagramPaser paser = new DiagramPaser();
		String wholename = "proteus.massSpring";
		Long id=null;
		try {
			id = DomainModelProxy.getId(wholename, em);
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		}
		//parameter
//		Parameter[] parameters = component.getParameters();

		//include 
		List<ComponentClause> compList = component.compList;

		//extends
		List<ExtendsClause> extendsList = component.extendsList;

		for (ComponentClause comp : compList) {

			String compWholename = (String) comp.typeName.get();
			String compExtent = null;
			String origin = null;
			String rotation = null;
			String varName = null;
			for (ComponentDecl compdecl : comp.declList) {

				Comment comment = compdecl.comment;
				if (comment != null && comment.annotation != null
						&& comment.annotation.map != null) {
					varName = (String) compdecl.varName.get();

					if (comment.annotation.map
							.get("Placement.transformation.extent") != null) {
						compExtent = ((OMModification) comment.annotation.map
								.get("Placement.transformation.extent")).expression
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

					try {
						IncludeModelProxy.save(id, compWholename, compExtent,
								origin, rotation, varName);
					} catch (ComponentNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

			for (ExtendsClause extend : extendsList) {

			}
		}
	}
}

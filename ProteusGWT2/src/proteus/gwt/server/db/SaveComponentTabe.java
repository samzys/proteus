package proteus.gwt.server.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import proteus.gwt.server.businesslogic.DomainComponentProxy;
import proteus.gwt.server.businesslogic.RestrictionProxy;
import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Restriction;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.exceptions.RestrictionNotFoundException;

import com.infoscient.proteus.db.datatransferobjects.RestrictionDTO;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.RefResolver;

/**
 * @author sam
 *the file to handle database, before SaveOtherTable.java
 */
public class SaveComponentTabe {

	public static void addToComponentTable() throws IOException,
			RestrictionNotFoundException {
		System.out.println("Build library started");
		new LocalUA().buildLibrary();
		System.out.println("Build library finished");

		String filePath = System.getProperty("user.dir")
		+ "/src/proteus/gwt/server/db/mslList";
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);

		String wholename = new String();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		while ((wholename = br.readLine()) != null) {
			try {
				Long compID = DomainComponentProxy.getId(wholename, em);
				ClassDef classdef = RefResolver.forName(wholename);
				if (classdef != null) {
					String comment = null;
					if (classdef.description != null) {
						comment = (String) classdef.description.get();
					} else if (classdef.classComment != null) {
						if (classdef.classComment.string != null) {
							comment = (String) classdef.classComment.string
									.get();
						}
					}
					Component comp = DomainComponentProxy.getComponent(compID,
							em);
					if (comment != null) {
						comp.setComment(comment);
						EntityTransaction tx = em.getTransaction();
						tx.begin();
						em.persist(comp);
						tx.commit();
					}
				}
			} catch (ComponentNotFoundException e) {
				// System.out.println(wholename);
				// e.printStackTrace();
				ClassDef classdef = RefResolver.forName(wholename);
				if (classdef != null) {
					String restrictionName = classdef.restriction.toUpperCase();

					Long restrictionId = RestrictionProxy.getId(
							restrictionName, em);
					/*
					 * insert to database
					 */
					Component component = new Component();
					String name = wholename.substring(wholename
							.lastIndexOf(".") + 1);
					Restriction restriction = new Restriction();
					restriction.setId(restrictionId);

					component.setWholename(wholename);
					component.setName(name);
					component.setRestriction(restriction);

					EntityTransaction tx = em.getTransaction();
					tx.begin();
					em.persist(component);
					tx.commit();
				} else {
					System.out.println(wholename);
				}
			}
		}

		em.close();
		emf.close();
	}

	public static void saveToComponentTable() throws IOException,
			RestrictionNotFoundException {
		System.out.println("Build library started");
		new LocalUA().buildLibrary();
		System.out.println("Build library finished");

		String filePath = System.getProperty("user.dir")
				+ "/src/com/infoscient/proteus/db/ComponentList.txt";
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);

		String wholename = new String();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		while ((wholename = br.readLine()) != null) {

			try {
				DomainComponentProxy.getId(wholename, em);
				System.out.println("OK");
			} catch (ComponentNotFoundException e) {

				// System.out.println(wholename);
				// e.printStackTrace();
				ClassDef classdef = RefResolver.forName(wholename);
				if (classdef != null) {
					String restrictionName = classdef.restriction.toUpperCase();

					Long restrictionId = RestrictionProxy.getId(
							restrictionName, em);
					/*
					 * insert to database
					 */
					Component component = new Component();
					String name = wholename.substring(wholename
							.lastIndexOf(".") + 1);
					Restriction restriction = new Restriction();
					restriction.setId(restrictionId);

					component.setWholename(wholename);
					component.setName(name);
					component.setRestriction(restriction);

					EntityTransaction tx = em.getTransaction();
					tx.begin();
					em.persist(component);
					tx.commit();
				} else {
					System.out.println(wholename);
				}
			}

		}
		em.close();
		emf.close();

		// String wholeName = "Modelica.Electrical.Analog.Basic.Resistor";
		// ComponentDTO com = DomainComponentProxy.getComponent(wholeName);
		// System.out.println(DomainComponentProxy.getId(wholeName));
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		try {
//			saveToComponentTable();
			addToComponentTable();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RestrictionNotFoundException e) {
			e.printStackTrace();
		}

	}

}

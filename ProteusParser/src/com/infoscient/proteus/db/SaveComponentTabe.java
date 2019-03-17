package com.infoscient.proteus.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.infoscient.proteus.LocalUA;
import com.infoscient.proteus.db.businesslogic.DomainComponentProxy;
import com.infoscient.proteus.db.businesslogic.RestrictionProxy;
import com.infoscient.proteus.db.datatransferobjects.RestrictionDTO;
import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Restriction;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
import com.infoscient.proteus.db.exceptions.RestrictionNotFoundException;
import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.RefResolver;

/**
 * @author sam
 *@deprecated
 */
public class SaveComponentTabe {

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

				//System.out.println(wholename);
				//e.printStackTrace();
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

		//String wholeName = "Modelica.Electrical.Analog.Basic.Resistor";
		//ComponentDTO  com = DomainComponentProxy.getComponent(wholeName);
		//System.out.println(DomainComponentProxy.getId(wholeName));
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		try {
			saveToComponentTable();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (RestrictionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

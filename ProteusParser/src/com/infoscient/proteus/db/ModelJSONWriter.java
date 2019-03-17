package com.infoscient.proteus.db;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.infoscient.proteus.db.businesslogic.DomainModelProxy;
import com.infoscient.proteus.db.datatransferobjects.ComponentDTO;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
import com.infoscient.proteus.db.exceptions.ModelNotFoundException;
import com.infoscient.proteus.ui.util.ServerContext;

public class ModelJSONWriter {
	private static String outFolderPath = ServerContext.get("JsonFilePath");// "D:\\workspace\\ProteusGWT\\war\\resources\\files\\ModelJSON\\";

	public static void main(String[] args) {
		writeAllModel();
	}

	private static void writeAllModel() {

		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("ProteusGWT");
			EntityManager em = emf.createEntityManager();
			List<Long> idList = DomainModelProxy.getIdList();

			for (long id : idList) {
				writeModel(id, em);
			}
			em.close();
			emf.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeModel(Long id) {
		try {
			System.out.println("In writeModel");

			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("ProteusGWT");
			EntityManager em = emf.createEntityManager();
			ComponentDTO componentDTO = DomainModelProxy.getModel(em, id);

			Gson gson = new Gson();
			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(componentDTO);

			// write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(outFolderPath + "/" + id);
			writer.write(json);
			writer.close();
			em.close();
			emf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeModel(Long id, EntityManager em)
			throws ModelNotFoundException, ComponentNotFoundException {

		ComponentDTO componentDTO = DomainModelProxy.getModel(em, id);

		Gson gson = new GsonBuilder().setLongSerializationPolicy(
				LongSerializationPolicy.STRING).create();
		gson = new Gson();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(componentDTO);
		try {
			// write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(outFolderPath + "/" + id);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//System.out.println(json);

	}

}

package proteus.gwt.server.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import proteus.gwt.server.businesslogic.DomainModelProxy2;
import proteus.gwt.server.businesslogic.PersistenceManagerGWT;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.exceptions.ModelNotFoundException;
import proteus.gwt.server.util.ServerContext;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public class ModelJSONWriter {
	private static String outFolderPath = ServerContext.get("ProteusRootPath")
			+ ServerContext.get("JsonFilePath");// "D:\\workspace\\ProteusGWT\\war\\resources\\files\\ModelJSON\\";

	public static void main(String[] args) {
//		writeAllModel();
		
			writeModel(452L);
	}

	private static void writeAllModel() {

		try {
			EntityManager em = PersistenceManagerGWT.get().getEntityManager();
			List<Long> idList = DomainModelProxy2.getIdList();

			for (long id : idList) {
				writeModel(id, em);
			}
			if (em.isOpen())
				em.close();

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

			writeModel(id, em);	
			em.close();
			emf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeModel(Long mId, EntityManager em)
			throws ModelNotFoundException, ComponentNotFoundException {

		ComponentDTO componentDTO = DomainModelProxy2.getComponentModel(em, mId);

		Gson gson = new GsonBuilder().setLongSerializationPolicy(
				LongSerializationPolicy.STRING).create();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(componentDTO);
		try {
			// write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(outFolderPath + "/" + mId);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(json);

	}

	public static void removeModel(Long mid) {
		File f = new File(outFolderPath + "/" + mid);
		if (f.exists()) {
			f.delete();
		}
	}
}

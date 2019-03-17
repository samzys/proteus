package proteus.gwt.server.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import proteus.gwt.server.businesslogic.DomainComponentProxy;
import proteus.gwt.server.businesslogic.DomainModelProxy2;
import proteus.gwt.server.domain.Model;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.exceptions.ModelNotFoundException;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
@Deprecated
public class ComponentWriter {
	private static String componentOutFolderPath = System
			.getProperty("user.dir")
			+ "\\war\\resources\\files\\ComponentJSON";
	private static String modelOutFolderPath = System.getProperty("user.dir")
			+ "\\war\\resources\\files\\ModelJSON";

	// private static String inFile = System.getProperty("user.dir")
	// + "\\war\\resources\\files\\ComponentList.txt";

	static boolean saveFromFile_flag = true;

	public static void main(String[] args) {
		boolean isXML = false;
		String name = "Modelica.Electrical.Analog.Examples.CauerLowPassSC";
		name = "Modelica.Blocks.Continuous.Integrator";
		name = "Modelica.Mechanics.Translational.Components.SupportFriction";
		name = "Modelica.Mechanics.Translational.Components.Rod";
		name = "Modelica.Mechanics.Rotational.Components.Inertia";
//		name = "Modelica.Blocks.Interfaces.RealOutput";
//		name = "Modelica.Mechanics.MultiBody.Examples.Loops.Utilities.Cylinder_analytic_CAD";
		
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		try {
			// writeComponentXml(name, em);
			if (saveFromFile_flag) {
				writeFromFile(em, isXML);
			} else {
				writeComponentJson(name, em);
			}
			// writeModels(em);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readComponentJson(String jsonString) {

	}

	public static void readComponentXml(String name, EntityManager em)
			throws JAXBException, ComponentNotFoundException,
			FileNotFoundException {
		FileInputStream fileInput = new FileInputStream(componentOutFolderPath
				+ name + ".xml");

		JAXBContext context = JAXBContext.newInstance(ComponentDTO.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		JAXBElement<ComponentDTO> root = unmarshaller.unmarshal(
				new StreamSource(fileInput), ComponentDTO.class);
		ComponentDTO mindMapDTO = root.getValue();

		System.out.println(mindMapDTO.getWholeName());
	}

	public static void writeComponentJson(String name, EntityManager em)
			throws ComponentNotFoundException {
		ComponentDTO componentDTO = DomainComponentProxy.getComponent(name, em);
		writeJSON(componentDTO, componentOutFolderPath, name);
	}

	public static void writeComponentXml(String name, EntityManager em)
			throws JAXBException, ComponentNotFoundException {
		JAXBContext context = JAXBContext.newInstance(ComponentDTO.class);
		Marshaller marshaller = context.createMarshaller();
		ComponentDTO componentDTO = DomainComponentProxy.getComponent(name, em);
		JAXBElement<ComponentDTO> rootElement = new JAXBElement<ComponentDTO>(
				new javax.xml.namespace.QName("http://ws.visualphysics.net/",
						"ComponentDTO"), ComponentDTO.class, componentDTO);
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller
				.marshal(rootElement, new File(componentOutFolderPath + name));
	}

	public static void writeFromFile(EntityManager em, boolean isXML) {
		try {
			FileReader fr;
			BufferedReader br;
			String strLine = "";
			String filePath = System.getProperty("user.dir")
					+ "/src/proteus/gwt/server/db/mslList";
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null && !strLine.equals("")) {
				if (isXML)
					writeComponentXml(strLine, em);
				else
					writeComponentJson(strLine, em);
				 System.err.println("**" + strLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeJSON(ComponentDTO componentDTO, String filepath,
			String filename) {
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		Gson gson = gb.create();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(componentDTO);

		try {
			// write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(filepath + "/" + filename);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(filename);
		System.out.println(json);

	}

	public static void writeModelJson(Long id, EntityManager em)
			throws ComponentNotFoundException, ModelNotFoundException {
		ComponentDTO comp = DomainModelProxy2.getComponentModel(id);
		writeJSON(comp, modelOutFolderPath, id.toString());
	}

	public static void writeModels(EntityManager em)
			throws ModelNotFoundException, ComponentNotFoundException {
		Query query = em.createQuery("select model from Model model");

		try {
			List<Model> models = (List<Model>) query.getResultList();
			for (Model model : models) {
				writeModelJson(model.getId(), em);
			}
		} catch (NoResultException e) {
			throw new ModelNotFoundException();
		}

		em.close();
	}
}

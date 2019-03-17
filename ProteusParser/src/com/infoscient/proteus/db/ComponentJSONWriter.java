package com.infoscient.proteus.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.google.gson.Gson;
import com.infoscient.proteus.db.businesslogic.DomainComponentProxy;
import com.infoscient.proteus.db.datatransferobjects.ComponentDTO;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;

public class ComponentJSONWriter {
	private static String outFolderPath = "D:\\downloads\\ProteusGWT\\ProteusGWT\\war\\resources\\files\\ComponentJSON\\";
	
	private static String inFile = System.getProperty("user.dir")
	+ "/src/com/infoscient/proteus/db/ComponentList.txt";

	public static void writeComponentXml(String name, EntityManager em)
			throws JAXBException, ComponentNotFoundException {
		JAXBContext context = JAXBContext.newInstance(ComponentDTO.class);
		Marshaller marshaller = context.createMarshaller();
		ComponentDTO componentDTO = DomainComponentProxy.getComponent(name, em);
		JAXBElement<ComponentDTO> rootElement = new JAXBElement<ComponentDTO>(
				new javax.xml.namespace.QName("http://ws.visualphysics.net/",
						"ComponentDTO"), ComponentDTO.class, componentDTO);
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(rootElement, new File(outFolderPath + name));
	}

	public static void writeComponentJson(String name, EntityManager em)
			throws ComponentNotFoundException {
		ComponentDTO componentDTO = DomainComponentProxy.getComponent(name, em);

		Gson gson = new Gson();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(componentDTO);

		try {
			// write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(outFolderPath + name);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(json);
	}	

	public static void writeFromFile(EntityManager em) {
		try {
			FileReader fr;
			BufferedReader br;
			String strLine = "";

			fr = new FileReader(inFile);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null && !strLine.equals("")) {			
					writeComponentJson(strLine, em);
				// System.out.println("**" + strLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public static void main(String[] args) {
		
		String name = "Modelica.Electrical.Analog.Examples.CauerLowPassSC";
		name = "Modelica.Electrical.Analog.Basic.Ground";

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		try {

			writeFromFile(em);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

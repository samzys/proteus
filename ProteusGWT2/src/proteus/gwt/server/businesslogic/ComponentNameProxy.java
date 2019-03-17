package proteus.gwt.server.businesslogic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.util.ServerContext;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.util.TreeData;
import proteus.gwt.shared.util.TreeNode;

/**
 * @author Gao Lei
 */
public class ComponentNameProxy {
	public static ArrayList<String> getNames() {
		ArrayList<String> nameList = new ArrayList<String>();

		String componentFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/ComponentList.txt";
		String packageFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/PackageList.txt";

		try {
			FileReader fr;
			BufferedReader br;
			String strLine = "";

			fr = new FileReader(componentFilesPath);
			br = new BufferedReader(fr);
			while ((strLine = br.readLine()) != null) {
				nameList.add("0." + strLine);
			}

			fr = new FileReader(packageFilesPath);
			br = new BufferedReader(fr);

			while ((strLine = br.readLine()) != null) {
				nameList.add("1." + strLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return nameList;
	}

	public static void writeComponentToXMLFile() {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();

		String filePath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ProteusFilesPath") + "/componentName.xml";

		ArrayList<String> result = ComponentNameProxy.getNames();

		TreeData treeData = new TreeData();
		treeData.insertTo(result);

		TreeNode rootNode = treeData.root;
		Document root = new Document(new Element("Component"));
		insertDoc(rootNode, root.getRootElement(), em);
		try {
			FileWriter fw = new FileWriter(filePath);
			new XMLOutputter().output(root, fw);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (em.isOpen())
			em.close();
	}

	private static void insertDoc(TreeNode parentNode, Element parentElement,
			EntityManager em) {
		for (TreeNode node : parentNode.children) {
			Element element = new Element(node.data);
			if (node.children.size() == 0) {
				if (node.isPackage) {
					element.setAttribute(new Attribute("package", "1"));
				} else {

					// System.out.println(node.wholeName);
					try {
						ComponentDTO componentDTO = DomainComponentProxy
								.getComponent(node.wholeName, em);
						element.setAttribute(new Attribute("restrict",
								componentDTO.getRestriction()));
					} catch (ComponentNotFoundException e) {
						e.printStackTrace();
					}

				}
			}

			parentElement.addContent(element);
			insertDoc(node, element, em);
		}
	}

	public static void main(String[] args) throws IOException {
		writeComponentToXMLFile();
	}
}

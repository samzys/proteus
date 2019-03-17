package proteus.gwt.shared.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class TreeData implements IsSerializable {
	private static final Logger logger = Logger.getLogger("TreeData");

	public TreeNode root;
	private final static String rootData = "root";
	private ComponentsNameMap componentsNameMap = new ComponentsNameMap();

	public TreeData() {
		this(rootData);
	}

	public TreeData(String rootData) {
		root = new TreeNode();
		root.data = rootData;
		root.parent = null;
		root.children = new ArrayList<TreeNode>();
	}

	public ComponentsNameMap getComponentsNameMap() {
		return componentsNameMap;
	}

	public void parseXML(String xml) {
		Document doc = XMLParser.parse(xml);

		NodeList nodeList = doc.getChildNodes().item(0).getChildNodes();
		parseXML(nodeList, root);
	}

	private void parseXML(NodeList nodeList, TreeNode parentNode) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String packageStr = "0";
			String name = node.getNodeName();

			TreeNode child = new TreeNode();
			child = new TreeNode();
			child.data = name;
			child.children = new ArrayList<TreeNode>();
			child.parent = parentNode;
			child.restrict="";
			if (node.getAttributes().getNamedItem("package") != null) {
				packageStr = node.getAttributes().getNamedItem("package")
						.getNodeValue();
			}
			if (node.getAttributes().getNamedItem("restrict") != null) {
				String restrictStr = node.getAttributes().getNamedItem("restrict")
						.getNodeValue();
				child.restrict =restrictStr;
			}
			
			// logger.info(node.getNodeName());
			// logger.info(packageStr);
			child.isPackage = packageStr.equals("1") ? true : false;
			parentNode.children.add(child);
			componentsNameMap.insertIntoNameMap(name, getIndexList(child));
			parseXML(node.getChildNodes(),child);
		}
	}

	public void insertTo(List<String> items) {
		TreeNode parentNode;
		for (String item : items) {
			String[] models = item.trim().split("\\.");
			String flag = models[0];

			parentNode = root;
			for (int i = 1; i < models.length; i++) {
				String model = models[i];
				TreeNode child = getChild(parentNode, model);
				if (child == null) {
					// System.out.println(model);
					child = new TreeNode();
					child.data = model;
					child.children = new ArrayList<TreeNode>();
					child.parent = parentNode;
					child.isPackage = flag.equals("1") ? true : false;
					parentNode.children.add(child);
					componentsNameMap.insertIntoNameMap(child.data,
							getIndexList(child));	
					if(i==models.length-1){
						child.wholeName = item.substring(2);
						
					}
				}
				
				parentNode = child;
			}
		}
	}

	public String getWholeName(TreeNode node) {
		if (node.parent == null) {
			return node.data;
		}
		String ret = node.data;
		TreeNode parentNode = node.parent;
		while (!parentNode.data.equals(rootData)) {
			ret = parentNode.data + "." + ret;
			parentNode = parentNode.parent;
		}
		return ret;
	}

	public ArrayList<Integer> getIndexList(TreeNode node) {
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		TreeNode currentMod = node;
		TreeNode parentNode = currentMod.parent;

		while (parentNode != null) {
			indexList.add(parentNode.children.indexOf(currentMod));
			currentMod = parentNode;
			parentNode = parentNode.parent;
		}
		Collections.reverse(indexList);
		return indexList;
	}

	private TreeNode getChild(TreeNode parentNode, String data) {
		for (TreeNode child : parentNode.children) {
			if (child.data.equals(data))
				return child;
		}
		return null;
	}

}

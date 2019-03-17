package proteus.gwt.shared.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeDemo {

	/**
	 * @param args
	 */

	public static void getChildren(TreeNode parent) {
		// System.out.println("parent:" + parent.data);
		List<TreeNode> children = parent.children;
		if (children.size() == 0)
			return;
		for (TreeNode child : children) {
			// System.out.println(child.data);
		}
		// System.out.println("*************");
		for (TreeNode child : children) {
			getChildren(child);
		}
	}

	public static void main(String[] args) {
		String one = "1.Modelica.Icons.Example.PID_Controller";
		String two = "0.Modelica.Icons.Examples.st";
		String three = "1.Modelica.Stan";
		String four = "0.Modelicas.Icon.Examples.Controller";
		ArrayList<String> items = new ArrayList<String>();
		items.add(one);
		items.add(three);
		items.add(two);
		items.add(four);

		TreeData treeData = new TreeData();
		treeData.insertTo(items);

//		System.out
//				.println(treeData.getIndexList(treeData.root.children.get(0).children
//						.get(0).children.get(1)));
		HashMap<String, ArrayList<ArrayList<Integer>>> map = treeData
				.getComponentsNameMap().getNameMap();
		System.out.println(map.get("example"));
	}

}

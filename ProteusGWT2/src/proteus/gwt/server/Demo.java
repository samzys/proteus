package proteus.gwt.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.GetClassNamesResponseDTO;
import proteus.gwt.shared.util.TreeData;
import proteus.gwt.shared.util.TreeNode;

public class Demo {

	private TreeData treeData = new TreeData();
	BufferedWriter bWriter;

	private void checkIcon(TreeNode node) throws IOException {
		String samllFolderPath = System.getProperty("user.dir")
				+ "/war/resources/images/classname/small/";
		String wholeName = treeData.getWholeName(node);
		String smallIconPath = samllFolderPath + wholeName + ".png";

		System.out.println(smallIconPath);
		File file = new File(smallIconPath);
		System.out.println(file.exists());
		if (!file.exists()) {
			bWriter.append(wholeName + "\n");
		}
		List<TreeNode> children = node.children;
		for (TreeNode child : children) {
			checkIcon(child);
			
		}
	}

	private void checkIcon() throws IOException {
		FileWriter writer = new FileWriter(System.getProperty("user.dir")
				+ "NoIcon.txt");
		bWriter = new BufferedWriter(writer);

		ProteusRemoteServiceImpl impl = new ProteusRemoteServiceImpl();
		GetClassNamesResponseDTO result = impl.getClassNames();
		treeData = result.getTreeData();
		TreeNode root = treeData.root;
		checkIcon(root);

		bWriter.close();
		writer.close();
	}

	public static void main(String[] args) throws IOException {
		//Demo demo = new Demo();
		//demo.checkIcon();
		
		ProteusRemoteServiceImpl imp = new ProteusRemoteServiceImpl();
		imp.initComponent();
	}
}

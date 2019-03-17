package proteus.gwt.shared.util;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.TreeItem;

public class TreeNode implements IsSerializable {
	public List<TreeNode> children;
	public String data;
	public boolean isPackage;
	public String wholeName;
	// public List<Integer> indexList;
	public TreeNode parent;
	public String restrict;
	
	public TreeItem treeItem;
}
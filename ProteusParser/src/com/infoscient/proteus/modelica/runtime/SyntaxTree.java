/**
 * 
 */
package com.infoscient.proteus.modelica.runtime;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lei Ting
 *
 */
public class SyntaxTree {

	TreeNode root;
	
	public SyntaxTree(TreeNode root) {
		this.root = root;
	}
	
	public TreeNode getRoot() {
		return root;
	}
	
	public class TreeNode {
		TreeNode parent;
		List<TreeNode> children;
		Object data;
		
		public TreeNode(TreeNode parent) {
			this.parent = parent;
			this.children = new LinkedList<TreeNode>();
		}
		
		public void addChild(TreeNode n) {
			children.add(n);
		}
		
		public List<TreeNode> getChildren() {
			return children;
		}
		
		public int getChildCount() {
			return children.size();
		}
		
		public boolean isLeaf() {
			return getChildCount() == 0;
		}
	}
}

/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

/**
 * @author leiting
 * Created May 8, 2011
 */
public abstract class SimulationVariableTreeTraverser {

	SimulationVariableTree tree;
	
	public SimulationVariableTreeTraverser(
			SimulationVariableTree tree) {
		this.tree = tree;
	}
	
	public void traverse(boolean breadthFirst, NodeTraverseContext context) {
		SimulationVariable root = tree.getRoot();
		
		if (breadthFirst) {
			visitNode(root, context);
			
			breadthFirstTraverse(root, context);
		}
		else {
			depthFirstTraverse(root, context);
		}
	}
	
	public void breadthFirstTraverse(SimulationVariable node, NodeTraverseContext context) {
		
		for (int i = 0; i < node.getChildren().size(); ++i) {
			SimulationVariable child = node.getChildren().get(i);
			visitNode(child, context);
		}

		for (int i = 0; i < node.getChildren().size(); ++i) {
			SimulationVariable child = node.getChildren().get(i);
			breadthFirstTraverse(child, context);
		}
	}
	
	public void depthFirstTraverse(SimulationVariable node, NodeTraverseContext context) {
		// TODO implement if needed
	}
	
	protected abstract void visitNode(SimulationVariable node, NodeTraverseContext context);
	
	public static class NodeTraverseContext {
	}
}

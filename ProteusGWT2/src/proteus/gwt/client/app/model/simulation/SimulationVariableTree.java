/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

/**
 * @author leiting
 * Created May 8, 2011
 */
public class SimulationVariableTree {

	SimulationVariable root;
	
	public SimulationVariableTree(SimulationVariable root) {
		setRoot(root);
	}
	
	public void setRoot(SimulationVariable root) {
		this.root = root;
	}
	
	public SimulationVariable getRoot() {
		return root;
	}
}

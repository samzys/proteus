/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leiting
 * Created May 8, 2011
 */
public class SimulationVariable {

	List<SimulationVariable> children = new ArrayList<SimulationVariable>();
	private SimulationVariable parent = null;
	
	private String name;
	private boolean hasValue;
	
	public SimulationVariable(String name, boolean hasValue) {
		this.name = name;
		this.hasValue = hasValue;
	}
	
	public void addChild(SimulationVariable child) {
		children.add(child);
		child.setParent(this);
	}
	
	public boolean hasChild(SimulationVariable child) {
		return children.contains(child);
	}
	
	public void removeChild(SimulationVariable child) {
		children.remove(child);
		child.setParent(null);
	}
	
	public List<SimulationVariable> getChildren() {
		return children;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHasValue(boolean hasValue) {
		this.hasValue = hasValue;
	}

	public boolean hasValue() {
		return hasValue;
	}

	public void setParent(SimulationVariable parent) {
		this.parent = parent;
	}

	public SimulationVariable getParent() {
		return parent;
	}
}

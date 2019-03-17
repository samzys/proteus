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
public class SimulationResultDisplayOptions {
	
	List<String> variables = new ArrayList<String>();
	private String initialChartType = "";
	
	public SimulationResultDisplayOptions() {
		
	}

	public void setInitialChartType(String initialChartType) {
		this.initialChartType = initialChartType;
	}

	public String getInitialChartType() {
		return initialChartType;
	}
	
	public void addVariable(String variableName) {
		variables.add(variableName);
	}
	
	public void removeVariable(String variableName) {
		variables.remove(variableName);
	}
	
	public List<String> getVariableList() {
		return variables;
	}
	
	@Override
	public String toString() {
		String variableList = "";
		for (String var : variables) {
			variableList += var + "|";
		}
		
		return initialChartType + " " + variableList;
	}
}

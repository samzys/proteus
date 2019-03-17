/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import proteus.gwt.shared.datatransferobjects.PlotDataDTO;

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulationResult {

	private Map<String, SimulationDataTable> datasets = new HashMap<String, SimulationDataTable>();
	
	List<PlotDataDTO> pds = null;
	List<String> variables = new ArrayList<String>();
	
	private String name;
	
	public SimulationResult(String name, List<PlotDataDTO> pds) {
		this.pds = pds;
		setName(name);
		
		for (PlotDataDTO pd : pds) {
			SimulationDataTable data = (SimulationDataTable) SimulationDataTable.create();
			data.populateFromPlotDataDTO(pd);
			
			addDataset(pd.getYLabel(), data);

			variables.add(pd.getYLabel());
		}
	}
	
	public SimulationResult() {
		// for test only
		this("test-data", new SimulationResultTestData().getPds());
	}
	
	public void addDataset(String title, SimulationDataTable data) {
		datasets.put(title, data);
	}
	
	public SimulationDataTable getData(String title) {
		return datasets.get(title);
	}
	
	public Set<String> getDatasets() {
		return datasets.keySet();
	}

	public SimulationDataTable getSelectedData(List<String> variableNames) {
		SimulationDataTable data = (SimulationDataTable) SimulationDataTable.create();
		
		data.populateFromPlotDataDTO(pds, variableNames);
		
		return data;
	}
	
	void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}

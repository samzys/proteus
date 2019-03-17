/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import proteus.gwt.client.app.model.simulation.SimulationDataTable;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author leiting
 * Created May 14, 2011
 */
public interface VisualizationProvider {
	
	public Widget getVisualization(
			String title, String viewType, int width, int height, 
			SimulationDataTable dataTable);
	
}

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
public class MixedVisualizationProvider implements VisualizationProvider {

	ChartToolsVisualizationProvider p1 = new ChartToolsVisualizationProvider();
//	ImageChartsVisualizationProvider p2 = new ImageChartsVisualizationProvider();
	FlotVisualizationProvider p3 = new FlotVisualizationProvider();
	
	/* (non-Javadoc)
	 * @see proteus.gwt.client.app.ui.simulation.VisualizationProvider#getVisualization(java.lang.String, java.lang.String, int, int, proteus.gwt.client.app.model.simulation.SimulationDataTable)
	 */
	@Override
	public Widget getVisualization(String title, String viewType, int width,
			int height, SimulationDataTable dataTable) {
		if (viewType.equalsIgnoreCase("table")) {
			return p1.getVisualization(title, viewType, width, height, dataTable);
		}
		else {
			return p3.getVisualization(title, viewType, width, height, dataTable);
		}
	}

}

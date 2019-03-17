/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import proteus.gwt.client.app.model.simulation.SimulationDataTable;
import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.BarSeriesOptions;
import ca.nanometrics.gflot.client.options.LineSeriesOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;
import ca.nanometrics.gflot.client.options.BarSeriesOptions.BarAlignment;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author leiting
 * Created Jun 14, 2011
 */
public class FlotVisualizationProvider implements VisualizationProvider {

	/* (non-Javadoc)
	 * @see proteus.gwt.client.app.ui.simulation.VisualizationProvider#getVisualization(java.lang.String, java.lang.String, int, int, proteus.gwt.client.app.model.simulation.SimulationDataTable)
	 */
	@Override
	public Widget getVisualization(String title, String viewType, int width,
			int height, SimulationDataTable dataTable) {
		return create(title, viewType, width, height, dataTable);
	}

	private Widget create(String title, String viewType, int width,
			int height, SimulationDataTable dataTable) {
	
		PlotModel model = new PlotModel();
		PlotOptions plotOptions = new PlotOptions();
		
		plotOptions.setDefaultShadowSize(0.2);
		
		if ("LineChart".equals(viewType)) {
			LineSeriesOptions lineSeriesOptions = new LineSeriesOptions();
			lineSeriesOptions.setShow(true);
			lineSeriesOptions.setLineWidth(1);
			
			plotOptions.setDefaultLineSeriesOptions(lineSeriesOptions);
		}
		else if ("BarChart".equals(viewType)) {
			BarSeriesOptions barSeriesOptions = new BarSeriesOptions();
            barSeriesOptions.setShow(true);
            barSeriesOptions.setLineWidth(1);
            barSeriesOptions.setBarWidth(1);
            barSeriesOptions.setAlignment(BarAlignment.CENTER);

            plotOptions.setDefaultBarsSeriesOptions(barSeriesOptions);
		}
		
		// add tick formatter to the options
//		plotOptions.setXAxisOptions(new AxisOptions().setTicks(12).setTickFormatter(new TickFormatter() {
//		        public String formatTickValue(double tickValue, Axis axis) {
//		                return MONTH_NAMES[(int) (tickValue - 1)];
//		        }
//		}));
		
		// Add data. The first column will always be time
		for (int i = 1; i < dataTable.getNumberOfColumns(); ++i) {
			// create a series
			SeriesHandler handler = model.addSeries(dataTable.getColumnLabel(i));
			
			for (int j = 0; j < dataTable.getNumberOfRows(); ++j) {
				if (dataTable.isValueNull(j, 0) || dataTable.isValueNull(j, i)) continue;
				handler.add(new DataPoint(dataTable.getValueDouble(j, 0), dataTable.getValueDouble(j, i)));
			}
		}
		
		// create the plot
		SimplePlot plot = new SimplePlot(model, plotOptions);
		
		plot.setWidth(width);
		plot.setHeight(height);
		plot.setTitle(title);
		
		// put it on a panel
		FlowPanel panel = new FlowPanel();
		panel.add(plot);
		
		return panel;
	}
}

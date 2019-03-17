/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import java.util.logging.Level;
import java.util.logging.Logger;

import proteus.gwt.client.app.VisualizationApiLoader;
import proteus.gwt.client.app.model.simulation.SimulationDataTable;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.ImageSparklineChart;
import com.google.gwt.visualization.client.visualizations.IntensityMap;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Visualization;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.ScatterChart;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;

/**
 * Google Chart Tools implementation of visualization provider
 * 
 * @author leiting
 * Created May 14, 2011
 */
public class ChartToolsVisualizationProvider implements VisualizationProvider {

	private static final Logger logger = Logger.getLogger("ChartToolsVisualizationProvider");
	
	/* (non-Javadoc)
	 * @see proteus.gwt.client.app.ui.simulation.VisualizationProvider#getVisualization()
	 */
	@Override
	public Widget getVisualization(String title, String viewType, int width, int height, SimulationDataTable dataTable) {
		Visualization visualization = null;
		
		TextStyle titleStyle = TextStyle.create();
		titleStyle.setColor("#2D0909");
		titleStyle.setFontName("Arial");
		titleStyle.setFontSize(14);
		
		Options options = Options.create();
		options.setTitle(title);
		options.setTitleTextStyle(titleStyle);
		options.setWidth(width);
		options.setHeight(height);
		
		options.setCurveType("function");
		
		if (dataTable == null) {
			logger.log(Level.WARNING, "data table is null, cancelling making visualization");
			return visualization;
		}
		
		if (viewType.equals("Table")) {
			VisualizationApiLoader.loadApiIfNotLoaded(Table.PACKAGE);
			Table.Options tableOptions = Table.Options.create();
			visualization = new Table(dataTable, tableOptions);
		}
		else if (viewType.equals("AnnotatedTimeLine")) {
			VisualizationApiLoader.loadApiIfNotLoaded(AnnotatedTimeLine.PACKAGE);
			AnnotatedTimeLine.Options timelineOptions = AnnotatedTimeLine.Options.create();
			visualization = new AnnotatedTimeLine(dataTable, timelineOptions, ""+width, ""+height);
		}
		else if (viewType.equals("AreaChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(AreaChart.PACKAGE);
			visualization = new AreaChart(dataTable, options);
		}
		else if (viewType.equals("BarChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(BarChart.PACKAGE);
			visualization = new BarChart(dataTable, options);
		}
		else if (viewType.equals("ColumnChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(ColumnChart.PACKAGE);
			visualization = new ColumnChart(dataTable, options);
		}
		else if (viewType.equals("IntensityMap")) {
			VisualizationApiLoader.loadApiIfNotLoaded(IntensityMap.PACKAGE);
			visualization = new IntensityMap(dataTable, IntensityMap.Options.create());
		}
		else if (viewType.equals("LineChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(LineChart.PACKAGE);
			visualization = new LineChart(dataTable, options);
		}
		else if (viewType.equals("MotionChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(MotionChart.PACKAGE);
			visualization = new MotionChart(dataTable, MotionChart.Options.create());
		}
		else if (viewType.equals("PieChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(PieChart.PACKAGE);
			visualization = new PieChart(dataTable, options);
		}
		else if (viewType.equals("ScatterChart")) {
			VisualizationApiLoader.loadApiIfNotLoaded(ScatterChart.PACKAGE);
			visualization = new ScatterChart(dataTable, options);
		}
		else if (viewType.equals("Sparkline")) {
			VisualizationApiLoader.loadApiIfNotLoaded(ImageSparklineChart.PACKAGE);
			ImageSparklineChart.Options sparklineOptions = ImageSparklineChart.Options.create();
			sparklineOptions.setWidth(width);
			sparklineOptions.setHeight(height);
			visualization = new ImageSparklineChart(dataTable, sparklineOptions);
		}
		else {
			// default fall back to table view
			VisualizationApiLoader.loadApiIfNotLoaded(Table.PACKAGE);
			Table.Options tableOptions = Table.Options.create();
			visualization = new Table(dataTable, tableOptions);
		}

		return visualization;
	}

}

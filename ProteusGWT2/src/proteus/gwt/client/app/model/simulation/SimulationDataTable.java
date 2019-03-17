/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import java.util.List;

import proteus.gwt.shared.datatransferobjects.PlotDataDTO;
import proteus.gwt.shared.datatransferobjects.Values2D;

import com.google.gwt.visualization.client.DataTable;

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulationDataTable extends DataTable {

	// use AbstractDataTable.create() and convert type to SimulationDataTable
	protected SimulationDataTable() {
	}
	
	public final void populateFromPlotDataDTO(PlotDataDTO data) {
		Values2D values = data.getValues();
		
		if (values == null || values.xValues == null || values.yValues == null) {
			return;
		}
		
		addColumn(ColumnType.NUMBER, data.getXLabel());
		addColumn(ColumnType.NUMBER, data.getYLabel());
		
		for (int i = 0; i < values.xValues.length && i < values.yValues.length; ++i) {
			addRow();
			setValue(i, 0, values.xValues[i]);
			setValue(i, 1, values.yValues[i]);
		}
	}

	public final void populateFromPlotDataDTO(
			List<PlotDataDTO> pds, List<String> variableNames) {
		addColumn(ColumnType.NUMBER, "t");
		int k = 0;
		
		for (PlotDataDTO data : pds) {
			if (!variableNames.contains(data.getYLabel())) 
				continue;
			
			Values2D values = data.getValues();
			
			if (values == null || values.xValues == null || values.yValues == null) {
				return;
			}
			
			addColumn(ColumnType.NUMBER, data.getYLabel());
			++k;
			
			for (int i = 0; i < values.xValues.length && i < values.yValues.length; ++i) {
				addRow();
				setValue(i, 0, values.xValues[i]);
				setValue(i, k, values.yValues[i]);
			}
		}
	}

}

/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import java.util.ArrayList;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.PlotDataDTO;
import proteus.gwt.shared.datatransferobjects.Values2D;
import proteus.gwt.shared.types.IllegalDimensionsException;

/**
 * @author leiting
 * Created May 8, 2011
 */
public class SimulationResultTestData {
	
	List<PlotDataDTO> pds = new ArrayList<PlotDataDTO>();
	
	public SimulationResultTestData() {
		initPds();
	}
	
	void initPds() {
		for (int i = 0; i < 9; ++i) {
			PlotDataDTO data  = new PlotDataDTO();
			data.setName("test-data");
			data.setTitle("test-data-title");
			data.setXLabel("time");
			data.setYLabel("a.b.var"+i);
			
			try {
				data.setValues(getRandomValues(100, i));
			} catch (IllegalDimensionsException e) {
				e.printStackTrace();
			}
			
			pds.add(data);
		}
	}
	
	public List<PlotDataDTO> getPds() {
		return pds;
	}
	
	Values2D getRandomValues(int n, int batch) {
		Values2D values = new Values2D();
		
		double [] xValues = new double[n];
		double [] yValues = new double[n];
		
		for (int i = 0; i < n; ++i) {
			xValues[i] = 1.0*i/n;
			
			if (batch == 0) {
				yValues[i] = Math.sin(xValues[i]);
			}
			else if (batch == 1) {
				yValues[i] = Math.cos(xValues[i]);
			}
			else {
				yValues[i] = Math.random()*1.0;
			}
			
			if (batch % 2 == 1) {
				yValues[i] = -yValues[i];
			}
		}

		values.xValues = xValues;
		values.yValues = yValues;
		
		return values;
	}
}

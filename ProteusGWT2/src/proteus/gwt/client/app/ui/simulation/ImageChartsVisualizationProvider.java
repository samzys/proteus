/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import java.util.logging.Level;
import java.util.logging.Logger;

import proteus.gwt.client.app.model.simulation.SimulationDataTable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * Google Image Charts (aka Chart API) implementation of visualization provider
 * 
 * @author leiting
 * Created May 14, 2011
 */
public class ImageChartsVisualizationProvider implements VisualizationProvider {

	private static final Logger logger = Logger.getLogger("ImageChartsVisualizationProvider");
	
	protected static final String imageChartsLoadingImageUrl = "resources/images/loading.gif";
	protected static final String imageChartsErrorImageUrl = "resources/images/error.jpg";
	
	/* (non-Javadoc)
	 * @see proteus.gwt.client.app.ui.simulation.VisualizationProvider#getVisualization(java.lang.String, java.lang.String, int, int, proteus.gwt.client.app.model.simulation.SimulationDataTable)
	 */
	@Override
	public Widget getVisualization(final String title, final String viewType, 
			final int width, final int height, final SimulationDataTable dataTable) 
	{
		final Frame frame = new Frame("imagecharts.html");
		frame.setWidth(""+(width>0?width:0+2)+"px");
		frame.setHeight(""+(height>0?height:0+2)+"px");
		frame.setStylePrimaryName("imagecharts");
		
		final String [] data = getDataRepresentation(dataTable);
		
		logger.log(Level.FINE, buildRequest(title, viewType, width, height, dataTable));
		
		Timer t = new Timer() {

			@Override
			public void run() {

				load(frame.getElement(), 
						getTitle(title), getChartType(viewType), getChartSize(width, height),  
						getAxis(), encodeData(data[0]), data[1], data[2]);
			}
		};
		t.schedule(500);
		
		return frame;
	}

	native void load(Element frame, 
			String chtt, String cht, String chs, String chxt, String chd, String chds, String chxr) /*-{
		var c = frame.contentWindow || frame.contentDocument;
		var doc = c.document ? c.document : c;
		doc.getElementById('cht').value = cht;
      	doc.getElementById('chtt').value = chtt;
      	doc.getElementById('chs').value = chs;
      	doc.getElementById('chxt').value = chxt;
      	doc.getElementById('chd').value = chd;
      	//doc.getElementById('chds').value = chds;
      	//doc.getElementById('chxr').value = chxr;
		c.loadGraph();
	}-*/;
	
	String buildRequest(String title, String viewType, int width, int height, SimulationDataTable dataTable) {
		String [] data = getDataRepresentation(dataTable);
		
		String request =  
			"chtt=" + getTitle(title) + "&" + 
			"cht=" + getChartType(viewType) + "&" + 
			"chs=" + getChartSize(width, height) + "&" + 
			"chxt=" + getAxis() + "&" + 
			"chd=t:" + data[0] + "&" + 
			"chds=" + data[1] + "&" + 
			"chxr=" + data[2];
		
		return request;
	}
	
	String encodeData(String data) {
		return "t:" + data;
	}
	
	String getTitle(String title) {
		return title;
	}
	
	String getChartSize(int width, int height) {
		return width + "x" + height;
	}
	
	String getAxis() {
		return "x,y";
	}
	
	String [] getDataRepresentation(SimulationDataTable dataTable) {
		String data = "";
		String scale = "";
		String axis = "";
		String xdata = "";
		String xscale = "";
		
		double gmin = Integer.MAX_VALUE, gmax = Integer.MIN_VALUE;
		
		for (int i = 0; i < dataTable.getNumberOfColumns(); ++i) {
			if (i != 0) data += "|";
			if (i >  1) data += xdata + "|";
			if (i != 0) scale += ",";
			
			double min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
			
			for (int j = 0; j < dataTable.getNumberOfRows(); ++j) {
				if (dataTable.isValueNull(j, i)) continue;
				
				if (j != 0) data += ",";
				
				double value = dataTable.getValueDouble(j, i);
				data += getTruncatedDouble(value, 4);
				
				if (value > max) max = value;
				if (value < min) min = value;
			}
			
			if (max > gmax) gmax = max;
			else if (min < gmin) gmin = min;

			if (i > 1) 
				scale += xscale + ",";
			
			String [] range = getHumanAcceptableRange(min, max);
			scale += range[0];
			
			if (i == 0) {
				range = getHumanAcceptableRange(gmin, gmax);
				axis = "0," + range[0] + "," + range[1]; // x-range
				xdata = data;
				xscale = scale;
			}
		}
		
		String [] range = getHumanAcceptableRange(gmin, gmax);
		axis += "|";
		axis += "1," + range[0] + "," + range[1]; // y-range
		
		return new String [] {data, scale, axis};
	}
	
	/**
	 * 
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	String [] getHumanAcceptableRange(double minValue, double maxValue) {
		if (minValue > 0)
			minValue = 0;
		else if (minValue >= -1)
			minValue = -1;
		else {
			int m = (int) (minValue % 10);
			minValue = (int) minValue + m;
		}
		
		if (maxValue > 1) {
			int m = (int) (maxValue % 10);
			maxValue = (int) maxValue + 10 - m;
		}
		else if (maxValue > 0){
			maxValue = 1;
		}
		else {
			maxValue = 0;
		}
		
		float step = (float)((maxValue - minValue) / 10.0); 
		
		return new String [] {String.valueOf((long)minValue) + "," + String.valueOf((long)maxValue), ""+step};
	}
	
	/**
	 * truncate the double value to having specified valid digits
	 * 
	 * @param data the double value
	 * @param valid valid digit number
	 * @return
	 */
	String getTruncatedDouble(double data, int valid) {
		boolean sign = data >= 0;
		data = sign ? data : -data;
		
		int k = (int) data;
		data -= k;
		
		int kvalue = k;
		int kdigit = k==0 ? 0 : 1;
		while ((kvalue /= 10) > 0) kdigit++;
		
		int i = valid - kdigit;
		long s = 1;
		while (i > 0) {
			data *= 10;
			s *= 10;
			--i;
		}
		
		double kk = (double) ((int)data) / s;
		
		return (sign ? "" : "-") + k + String.valueOf(kk).substring(1);
	}
	
	String getChartType(String viewType) {
		String type = "";
		
		if (viewType.equalsIgnoreCase("LineChart")) {
			type = "lxy";
		}
		else if (viewType.equalsIgnoreCase("ColumnChart")) {
			type = "lxy";
		}
		else {
			type  = "lxy";
		}
		
		return type;
	}
}

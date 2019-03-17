/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import proteus.gwt.server.ServerContext;
import proteus.gwt.server.util.ParseException;
import proteus.gwt.server.util.PltExtHelper;
import proteus.gwt.shared.datatransferobjects.PlotDataDTO;

/**
 * @author leiting
 * Created Apr 28, 2011
 */
public class SimulationResultTest {
	private static final Logger log = Logger.getLogger(SimulationResultTest.class.getSimpleName());
	
	@Test
	public void testPopulateFromPlotDataDTO() {
		File file = new File(ServerContext.get("ProteusRootPath") + 
				"/resources/files/MyModel_res.plt");
		
		List<PlotDataDTO> list = new ArrayList<PlotDataDTO>();
		try {
			PltExtHelper.readPltPlot(file, list);
			for (PlotDataDTO data : list) {
				log.info(data.getTitle() + " " + 
						data.getXLabel() + " " + data.getYLabel() + " " +  
						data.getValues().xValues.length + " " + 
						data.getValues().yValues.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			assertEquals(25, list.size());
		}
	}
}

/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import static org.junit.Assert.assertEquals;

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
 * Created May 8, 2011
 */
public class SimulationVariableTest {
private static final Logger log = Logger.getLogger(SimulationVariableTest.class.getSimpleName());
	
	@Test
	public void testFlatToTree() {
		File file = new File(ServerContext.get("ProteusRootPath") + 
				"/resources/files/MyModel_res.plt");

		List<String> names = new ArrayList<String>();
		List<String> names2 = null;
		
		SimulationVariableTree tree;
		
		List<PlotDataDTO> list = new ArrayList<PlotDataDTO>();
		try {
			
			PltExtHelper.readPltPlot(file, list);
			for (PlotDataDTO data : list) {
				names.add(data.getYLabel());
			}
			
			// flat to tree
			tree = SimulationVariableTreeParser.flatToTree("test-data", names);
			
			// tree to flat
			names2 = SimulationVariableTreeParser.treeToFlat(tree);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			assertEquals(names.size(), names2.size());
		}
	}
}

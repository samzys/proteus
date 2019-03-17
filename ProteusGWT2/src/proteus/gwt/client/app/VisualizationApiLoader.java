/**
 * 
 */
package proteus.gwt.client.app;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.visualization.client.VisualizationUtils;

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class VisualizationApiLoader implements Runnable {

	private static final Logger logger = Logger.getLogger("VisualizationApiLoader");
	
	private static final Map<String, Boolean> loadedMap = new HashMap<String, Boolean>();
	
	public static void loadApiIfNotLoaded(String api) {
		if (! loadedMap.containsKey(api)) {
			logger.log(Level.INFO, "Loading Visualization API package: " + api);
			
			VisualizationUtils.loadVisualizationApi(new VisualizationApiLoader(), api);
			loadedMap.put(api, true);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

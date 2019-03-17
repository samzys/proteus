/**
 * 
 */
package proteus.gwt.client.app.model.simulation;


/**
 * For now, only store one result
 * 
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulationResultPool {
	
	// TODO store multiple result, if needed
	// private List<SimulateResult> resultList;
	
	private static SimulationResult lastResult;

	public static void putSimulateResult(SimulationResult res) {
		lastResult = res;
	}
	
	public static SimulationResult getLastSimulateResult() {
		return lastResult;
	}
}

/**
 * 
 */
package proteus.gwt.client.app.event.simulation;

import proteus.gwt.client.app.event.simulation.SimulateFinishedEvent.SimulateFinishedEventHandler;
import proteus.gwt.client.app.model.simulation.SimulationDataTable;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.UIObject;

// TODO: all events can extends one common base class, which
//             implements getAssociatedType() and dispatch()

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulateFinishedEvent extends GwtEvent<SimulateFinishedEventHandler> {
	public static Type<SimulateFinishedEventHandler> TYPE = new Type<SimulateFinishedEventHandler>();

	private boolean succeeded;
	
	public SimulateFinishedEvent() {
	}
	
	@Override
	public Type<SimulateFinishedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimulateFinishedEventHandler handler) {
		handler.onSimulateFinished(this);
	}
	
	/**
	 * @param succeeded the succeeded to set
	 */
	void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}

	/**
	 * @return the succeeded
	 */
	boolean isSucceeded() {
		return succeeded;
	}
	
	public static interface SimulateFinishedEventHandler extends EventHandler {
		public void onSimulateFinished(SimulateFinishedEvent event);
	}
}
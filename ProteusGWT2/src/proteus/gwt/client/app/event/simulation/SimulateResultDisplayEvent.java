/**
 * 
 */
package proteus.gwt.client.app.event.simulation;

import proteus.gwt.client.app.event.simulation.SimulateResultDisplayEvent.SimulateResultDisplayEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

// TODO: all events can extends one common base class, which
//             implements getAssociatedType() and dispatch()

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulateResultDisplayEvent extends GwtEvent<SimulateResultDisplayEventHandler> {
	public static Type<SimulateResultDisplayEventHandler> TYPE = new Type<SimulateResultDisplayEventHandler>();

	@Override
	public Type<SimulateResultDisplayEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimulateResultDisplayEventHandler handler) {
		handler.onSimulateResultDisplay(this);
	}
	
	public static interface SimulateResultDisplayEventHandler extends EventHandler {
		public void onSimulateResultDisplay(SimulateResultDisplayEvent event);
	}
}
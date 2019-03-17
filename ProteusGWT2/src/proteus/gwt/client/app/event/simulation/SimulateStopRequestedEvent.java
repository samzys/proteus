/**
 * 
 */
package proteus.gwt.client.app.event.simulation;

import proteus.gwt.client.app.event.simulation.SimulateStopRequestedEvent.SimulateStopRequestedEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

// TODO: all events can extends one common base class, which
//             implements getAssociatedType() and dispatch()

/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulateStopRequestedEvent extends GwtEvent<SimulateStopRequestedEventHandler> {
	public static Type<SimulateStopRequestedEventHandler> TYPE = new Type<SimulateStopRequestedEventHandler>();

	@Override
	public Type<SimulateStopRequestedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SimulateStopRequestedEventHandler handler) {
		handler.onSimulateStopRequested(this);
	}
	
	public static interface SimulateStopRequestedEventHandler extends EventHandler {
		public void onSimulateStopRequested(SimulateStopRequestedEvent event);
	}
}
/**
 * 
 */
package proteus.gwt.client.app.event.simulation;

import proteus.gwt.client.app.event.simulation.SimulateEvent.SimulateEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.UIObject;

// TODO: all events can extends one common base class, which
//             implements getAssociatedType() and dispatch()

/**
 * 
 * @author Lei Ting Created Apr 10, 2011
 */
public class SimulateEvent extends GwtEvent<SimulateEventHandler> {
	public static interface SimulateEventHandler extends EventHandler {
		public void onSimulate(SimulateEvent event);
	}

	public static Type<SimulateEventHandler> TYPE = new Type<SimulateEventHandler>();
	private double startTime;

	private double stopTime;

	private int numOfInterval;

	private UIObject target;

	public SimulateEvent(UIObject target, double startTime, double stopTime,
			int interval) {
		this.setTarget(target);
		this.setStartTime(startTime);
		this.setStopTime(stopTime);
		this.setNumOfInterval(interval);
	}

	@Override
	protected void dispatch(SimulateEventHandler handler) {
		handler.onSimulate(this);
	}

	@Override
	public Type<SimulateEventHandler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}

	/**
	 * @return the stopTime
	 */
	public double getStopTime() {
		return stopTime;
	}

	/**
	 * @return the target
	 */
	UIObject getTarget() {
		return target;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	/**
	 * @param stopTime
	 *            the stopTime to set
	 */
	public void setStopTime(double stopTime) {
		this.stopTime = stopTime;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	void setTarget(UIObject target) {
		this.target = target;
	}

	/**
	 * @return the numOfInterval
	 */
	public int getNumOfInterval() {
		return numOfInterval;
	}

	/**
	 * @param numOfInterval
	 *            the numOfInterval to set
	 */
	public void setNumOfInterval(int numOfInterval) {
		this.numOfInterval = numOfInterval;
	}
}
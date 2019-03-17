package proteus.gwt.client.app.event;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;

public abstract class MouseEventHandler implements MouseMoveHandler,
		MouseDownHandler, MouseUpHandler, MouseOverHandler, MouseOutHandler,
		MouseWheelHandler, ClickHandler, DoubleClickHandler {
	public  void onClick(ClickEvent event) {}

	public void onDoubleClick(DoubleClickEvent event) {}

	public void onMouseDrag(MouseMoveEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.MouseMoveHandler#onMouseMove(com.google
	 * .gwt.event.dom.client.MouseMoveEvent)
	 */
	public void onMouseMove(MouseMoveEvent event){}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.MouseDownHandler#onMouseDown(com.google
	 * .gwt.event.dom.client.MouseDownEvent)
	 */
	public void onMouseDown(MouseDownEvent event){}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.MouseUpHandler#onMouseUp(com.google.gwt
	 * .event.dom.client.MouseUpEvent)
	 */
	public  void onMouseUp(MouseUpEvent event){}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.MouseOverHandler#onMouseOver(com.google
	 * .gwt.event.dom.client.MouseOverEvent)
	 */
	public  void onMouseOver(MouseOverEvent event){}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.MouseOutHandler#onMouseOut(com.google
	 * .gwt.event.dom.client.MouseOutEvent)
	 */
	public  void onMouseOut(MouseOutEvent event){}

	public  void onMouseWheel(MouseWheelEvent event){}

}

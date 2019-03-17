/**
 * 
 */
package proteus.gwt.client.app.event;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

/**
 * @author sam
 * 
 */
public abstract class KeyEventHandler implements KeyPressHandler,
		KeyDownHandler, KeyUpHandler {
	public int lastKeyCode = -1;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt
	 * .event.dom.client.KeyDownEvent)
	 */
	public abstract void onKeyDown(KeyDownEvent event);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event
	 * .dom.client.KeyUpEvent)
	 */
	public abstract void onKeyUp(KeyUpEvent event);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.KeyPressHandler#onKeyPress(com.google
	 * .gwt.event.dom.client.KeyPressEvent)
	 */
	public abstract void onKeyPress(KeyPressEvent event);
}

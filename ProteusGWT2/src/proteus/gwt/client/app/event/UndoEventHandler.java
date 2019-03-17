/**
 * 
 */
package proteus.gwt.client.app.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author leiting
 * Created Jun 19, 2011
 */
public interface UndoEventHandler extends EventHandler {

	public void onUndo(UndoEvent event);
}

/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.edit;

import proteus.gwt.client.app.ui.canvas.ConnectionItem;
import proteus.gwt.client.app.ui.canvas.action.ConnectionActions;
import emu.java.swing.undo.AbstractUndoableEdit;
import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;

/**
 *
 * @author Lei Ting
 * Created Apr 6, 2011
 */
public class RemoveConnectionEdit extends AbstractUndoableEdit {
	
	private ConnectionActions handler;
	private ConnectionItem[] items;

	public RemoveConnectionEdit(ConnectionActions handler, ConnectionItem[] items) {
		this.handler = handler;
		this.items = items;
		
		redo();
	}

	public void undo() throws CannotUndoException {
		handler.add(items);
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		handler.remove(items);
		handler.refresh();
	}
}

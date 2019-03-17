/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.edit;

import proteus.gwt.client.app.ui.canvas.CanvasItem;
import proteus.gwt.client.app.ui.canvas.action.ComponentActions;
import emu.java.swing.undo.AbstractUndoableEdit;
import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;

/**
 * @author Lei Ting
 * Created Apr 6, 2011
 */
public class RemoveComponentEdit extends AbstractUndoableEdit {
	
	private ComponentActions handler;
	private CanvasItem[] items;

	public RemoveComponentEdit(ComponentActions handler, CanvasItem[] items) {
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
	}
}

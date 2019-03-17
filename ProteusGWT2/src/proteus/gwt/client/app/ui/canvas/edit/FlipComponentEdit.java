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
 *
 * @author Lei Ting
 * Created Apr 6, 2011
 */
public class FlipComponentEdit extends AbstractUndoableEdit {
	private ComponentActions handler;
	private CanvasItem[] items;
	private boolean horizontal;

	public FlipComponentEdit(ComponentActions handler, CanvasItem[] items, boolean horizontal) {
		this.handler = handler;
		this.items = new CanvasItem[items.length];
		this.horizontal = horizontal;
		
		System.arraycopy(items, 0, this.items, 0, this.items.length);
		redo();
	}

	public void undo() throws CannotUndoException {
		handler.flip(items, horizontal);
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		handler.flip(items, horizontal);
		handler.refresh();
	}
}

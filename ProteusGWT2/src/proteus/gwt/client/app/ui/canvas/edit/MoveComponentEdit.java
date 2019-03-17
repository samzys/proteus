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
 * Created Apr 7, 2011
 */
public class MoveComponentEdit extends AbstractUndoableEdit {
	
	private ComponentActions handler;
	private CanvasItem[] items;
	private int dx, dy;

	public MoveComponentEdit(ComponentActions handler, CanvasItem[] items, int dx, int dy) {
		this.handler = handler;
		this.items = new CanvasItem[items.length];
		this.dx = dx;
		this.dy = dy;
		
		System.arraycopy(items, 0, this.items, 0, this.items.length);
	}

	public void undo() throws CannotUndoException {
		handler.move(items, -dx, -dy);
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		handler.move(items, dx, dy);
		handler.refresh();
	}
}

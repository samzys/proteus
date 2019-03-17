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
public class ResizeComponentEdit extends AbstractUndoableEdit {
	private ComponentActions handler;
	private CanvasItem[] items;
	private int resize;
	private int dx, dy;
	
	public ResizeComponentEdit(ComponentActions handler, CanvasItem[] items, 
			int resize, int dx, int dy) {
		this.handler = handler;
		this.items = new CanvasItem[items.length];
		this.resize = resize;
		this.dx = dx;
		this.dy = dy;
		
		System.arraycopy(items, 0, this.items, 0, this.items.length);
		redo();
	}

	public void undo() throws CannotUndoException {
		handler.resize(items, resize, -dx, -dy);
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		handler.resize(items, resize, dx, dy);
		handler.refresh();
	}
}

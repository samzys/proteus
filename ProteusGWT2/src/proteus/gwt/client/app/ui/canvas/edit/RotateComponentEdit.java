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
public class RotateComponentEdit extends AbstractUndoableEdit {
	
	private ComponentActions handler;
	private CanvasItem[] items;
	private boolean clockwise;

	public RotateComponentEdit(ComponentActions handler, CanvasItem[] items, boolean clockwise) {
		this.handler = handler;
		this.items = new CanvasItem[items.length];
		this.clockwise = clockwise;
		
		System.arraycopy(items, 0, this.items, 0, this.items.length);
		redo();
	}

	public void undo() throws CannotUndoException {
		handler.rotate(items, !clockwise);
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		handler.rotate(items, clockwise);
		handler.refresh();
	}
}

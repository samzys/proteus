/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.edit;

import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.ui.canvas.Canvas;
import proteus.gwt.client.app.ui.canvas.CanvasItem;
import emu.java.swing.undo.AbstractUndoableEdit;
import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;

/**
 * @author Lei Ting
 * Created Apr 6, 2011
 */
public class PasteEdit extends AbstractUndoableEdit {
	private Canvas canvas;

	private Object[] items;

	public PasteEdit(Canvas canvas, Object[] items) {
		this.canvas = canvas;
		this.items = items;
		redo();
	}

	public void undo() throws CannotUndoException {
		List<CanvasItem> canvasItems = new LinkedList<CanvasItem>();
		for (Object item : items) {
			if (item instanceof CanvasItem) {
				canvasItems.add((CanvasItem) item);
			}
		}
		canvas.remove(canvasItems.toArray(new CanvasItem[0]));
	}

	public void redo() throws CannotRedoException {
		List<CanvasItem> canvasItems = new LinkedList<CanvasItem>();
		for (Object item : items) {
			if (item instanceof CanvasItem) {
				canvasItems.add((CanvasItem) item);
			}
		}
		canvas.add(canvasItems.toArray(new CanvasItem[0]));
	}

}

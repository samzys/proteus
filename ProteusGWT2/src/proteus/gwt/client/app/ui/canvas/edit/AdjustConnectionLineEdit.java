/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.edit;

import proteus.gwt.client.app.ui.canvas.ConnectionItem;
import proteus.gwt.client.app.ui.canvas.action.ConnectionActions;
import proteus.gwt.shared.graphics.geom.Line2D;
import emu.java.swing.undo.AbstractUndoableEdit;
import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;

/**
 *
 * @author Lei Ting
 * Created Apr 6, 2011
 */
public class AdjustConnectionLineEdit extends AbstractUndoableEdit {
	
	private ConnectionActions handler;
	private ConnectionItem item;
	private Line2D line;
	private Line2D lineAdjusted;
	private int dx, dy;
	
	public AdjustConnectionLineEdit(ConnectionActions handler, 
			ConnectionItem item, Line2D line, int dx, int dy) {
		this.handler = handler;
		this.item = item;
		this.dx = dx;
		this.dy = dy;
		this.lineAdjusted = new Line2D.Double(line.getX1(), line.getY1(), line.getX2(), line.getY2());
	}

	public void undo() throws CannotUndoException {
		line = handler.adjustLine(item, lineAdjusted, -dx, -dy);
		line = new Line2D.Double(line.getX1(), line.getY1(), line.getX2(), line.getY2());
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		lineAdjusted = handler.adjustLine(item, line, dx, dy);
		lineAdjusted = new Line2D.Double(lineAdjusted.getX1(), lineAdjusted.getY1(), 
				lineAdjusted.getX2(), lineAdjusted.getY2());
		handler.refresh();
	}
}

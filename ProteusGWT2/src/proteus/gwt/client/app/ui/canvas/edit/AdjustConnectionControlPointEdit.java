/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.edit;

import proteus.gwt.client.app.ui.canvas.ConnectionItem;
import proteus.gwt.client.app.ui.canvas.action.ConnectionActions;
import proteus.gwt.shared.graphics.geom.Point2D;
import emu.java.swing.undo.AbstractUndoableEdit;
import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;

/**
 *
 * @author Lei Ting
 * Created Apr 6, 2011
 */
public class AdjustConnectionControlPointEdit extends AbstractUndoableEdit {
	
	private ConnectionActions handler;
	private ConnectionItem item;
	private Point2D point;
	private Point2D pointAdjusted;
	private int dx, dy;
	
	public AdjustConnectionControlPointEdit(ConnectionActions handler, 
			ConnectionItem item, Point2D point, int dx, int dy) {
		this.handler = handler;
		this.item = item;
		this.dx = dx;
		this.dy = dy;
		this.pointAdjusted = new Point2D.Double(point.getX(), point.getY());
	}

	public void undo() throws CannotUndoException {
		point = handler.adjustControlPoint(item, pointAdjusted, -dx, -dy);
		point = new Point2D.Double(point.getX(), point.getY());
		handler.refresh();
	}

	public void redo() throws CannotRedoException {
		pointAdjusted = handler.adjustControlPoint(item, point, dx, dy);
		pointAdjusted = new Point2D.Double(pointAdjusted.getX(), pointAdjusted.getY());
		handler.refresh();
	}
}

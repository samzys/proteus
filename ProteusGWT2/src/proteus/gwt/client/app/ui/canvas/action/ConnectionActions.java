/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.action;

import proteus.gwt.client.app.ui.canvas.ConnectionItem;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Point2D;

/**
 * @author leiting
 * Created Jun 24, 2011
 */
public interface ConnectionActions extends CanvasItemActions {

	public void add(ConnectionItem [] items);
	public void remove(ConnectionItem[] items);
	public void addControlPoint(ConnectionItem item, Point2D point);
	public void removeControlPoint(ConnectionItem item, Point2D point);
	/**
	 * 
	 * @param item
	 * @param line
	 * @param delta
	 * @return the adjusted line
	 */
	public Line2D adjustLine(ConnectionItem item, Line2D line, int dx, int dy);
	/**
	 * 
	 * @param item
	 * @param point
	 * @param dx
	 * @param dy
	 * @return the adjusted point
	 */
	public Point2D adjustControlPoint(ConnectionItem item, Point2D point, int dx, int dy);
}

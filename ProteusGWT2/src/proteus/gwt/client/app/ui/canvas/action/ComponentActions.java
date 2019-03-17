/**
 * 
 */
package proteus.gwt.client.app.ui.canvas.action;

import proteus.gwt.client.app.ui.canvas.CanvasItem;

/**
 * @author leiting
 * Created Jun 21, 2011
 */
public interface ComponentActions extends CanvasItemActions {

	public void add(CanvasItem[] items);
	public void remove(CanvasItem[] items);
	public void move(CanvasItem[] items, int dx, int dy);
	public void resize(CanvasItem[] items, int dir, int dx, int dy);
	public void rotate(CanvasItem[] items, boolean clockwise);
	public void flip(CanvasItem[] items, boolean horizontal);
}

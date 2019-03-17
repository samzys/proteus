package proteus.gwt.client.app.ui.canvas;

import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.event.MouseEventHandler;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.PopupPanel;

public abstract class CanvasItem extends Item implements MouseUpHandler,
		MouseDownHandler {
	public static final String ORIENTATION_HORIZONTAL = "Horizontal",
			ORIENTATION_VERTICAL = "Vertical";

	public static final String POSITION_TOP = "Top", POSITION_LEFT = "Left",
			POSITION_BOTTOM = "Bottom", POSITION_RIGHT = "Right",
			NO_LABEL = "No Label";

	protected String _prefix;

	protected Canvas canvas;

	public String labelOrientation = ORIENTATION_HORIZONTAL;

	public String labelPosition = NO_LABEL;

	private List<MouseEventHandler> mouseListeners = new LinkedList<MouseEventHandler>();
	protected PopupPanel popupMenu;
	protected boolean resizable;

	protected boolean selected;
	protected boolean showLabel = true;

	public CanvasItem(Canvas canvas, String _prefix) {
		this.canvas = canvas;
		this._prefix = _prefix;
	}

	public CanvasItem(String _prefix) {
		this._prefix = _prefix;
	}

	public void addMouseEventHandler(MouseEventHandler ml) {
		mouseListeners.add(ml);
	}

	public void clearConnectorCover() {
	}

	public abstract CanvasItem copy();

	public String get_prefix() {
		return _prefix;
	}

	public Rectangle2D.Double getBounds(Rectangle2D.Double r) {
		r.x = bounds.x;
		r.y = bounds.y;
		r.width = bounds.width;
		r.height = bounds.height;

		return r;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Point2D.Double getLocation() {
		return new Point2D.Double(bounds.x, bounds.y);
	}

	/**
	 * @return the popupMenu
	 */
	public PopupPanel getPopupMenu() {
		return popupMenu;
	}

	public boolean isAroundConnItem(double x, double y) {
		return false;
	}

	public boolean isInRange(double z1, double z2, double width_height) {
		double differ = Math.abs(Math.abs(z1) - Math.abs(z2));

		if (differ <= 1.5 * width_height)
			return true;
		else
			return false;
	}

	public boolean isInRange(int z1, int z2, int width_height) {

		int differ = Math.abs(Math.abs(z1) - Math.abs(z2));

		if (differ <= 1.5 * width_height)
			return true;
		else
			return false;
	}

	public boolean isResizable() {
		return resizable;
	}

	public boolean isShowLabel() {
		return showLabel;
	}

	public void notifyAdd(Canvas canvas) {
		this.canvas = canvas;
		Point2D.Double loc = getLocation();
		setLocation(loc);
	}

	public void notifyRemove(Canvas canvas) {
		this.canvas = null;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		for (MouseEventHandler ml : mouseListeners) {
			ml.onMouseDown(event);
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		for (MouseEventHandler ml : mouseListeners) {
			ml.onMouseUp(event);
		}
	}

	@Override
	public void paint(Graphics2D g) {
	}

	public void removeMouseEventHandler(MouseEventHandler ml) {
		mouseListeners.remove(ml);
	}

	public void repaint() {
	}

	public void set_prefix(String prefix) {
		_prefix = prefix;
	}

	public void setLoc(int x, int y) {
		super.setLocation(x, y);

	}

	public void setPopupMenu(PopupPanel popupMenu) {
		this.popupMenu = popupMenu;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public abstract void updateCodeCanvas();

	public void drawSelectionBox(Graphics2D g2, CanvasItem item, int resize) {

	}

	/**
	 * @author Maryam
	 * @param g2
	 * @param item
	 * @function: highlights the intermediate points when a line is selected
	 */
	public void drawSelectionLine(Graphics2D g2, CanvasItem item) {
		ConnectionItem ci = (ConnectionItem) item;

		ConnectorItem src = ci.getSource();
		ConnectorItem dest = ci.getDestination();

		ComponentCanvasItem src_cci = src.getComponentCanvasItem();
		ComponentCanvasItem dest_cci = dest.getComponentCanvasItem();

		g2.setColor(Color.RED);

		g2.fillRect(src_cci.getX() + src.getX() + src.getHitX()
				- src.getWidth() / 2, src_cci.getY() + src.getY()
				+ src.getHitY() - src.getHeight() / 2, src.getWidth(), src
				.getHeight());// works fine

		g2.fillRect(dest_cci.getX() + dest.getX() + dest.getHitX()
				- dest.getWidth() / 2, dest_cci.getY() + dest.getY()
				+ dest.getHitY() - dest.getHeight() / 2, dest.getWidth(), dest
				.getHeight());

		// Maryam: This is to highlight the corners
		List<Point2D> corners = ci.getCornerPoints();

		for (Point2D p : corners) {
			g2.fillRect((int) p.getX() - dest.getWidth() / 2, (int) p.getY()
					- dest.getHeight() / 2, dest.getWidth(), dest.getHeight());
			// TODO
		}
	}
}

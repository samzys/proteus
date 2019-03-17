package proteus.gwt.client.app.ui.canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.ui.canvas.edit.AdjustConnectionControlPointEdit;
import proteus.gwt.client.app.ui.canvas.edit.AdjustConnectionLineEdit;
import proteus.gwt.client.app.ui.canvas.edit.MoveComponentEdit;
import proteus.gwt.client.app.ui.canvas.edit.ResizeComponentEdit;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Cursor;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Point2D;

import ca.nanometrics.gflot.client.event.SelectionListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;

import emu.java.swing.undo.UndoableEditSupport;

public class SelectionManager extends ModeHandler {

	private static final Color MARQUEE_COLOR = Color.RED;
	private static final BasicStroke MARQUEE_STROKE = new BasicStroke(1.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0.0f, new float[] {
					5.0f, 5.0f }, 0.0f);

	private static final int STATE_NONE = 0, STATE_MARQUEE = 1, STATE_MOVE = 2,
			STATE_RESIZE = 3;

	public static final int NW = 1, NE = 2, SW = 3, SE = 4, // @Maryam: I
			// changed all the
			// values to
			// positive ones
			N = 5, E = 6, S = 7, W = 8;

	private Rectangle marquee = new Rectangle();

	private CanvasItem resitem;

	private int resWidth, resHeight;

	private int state;

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	private final int HANDLE_SIZE = 6;

	private int resize = -1;

	private int gridSize = 1;

	private boolean preserveAspectRatio;

	private Canvas canvas;

	protected CanvasItem[] selection;// this is an array to store the selected

	protected int selectionCount;// index of the above array

	private Point2D.Double[] oldLocations;

	protected UndoableEditSupport undoSupport = new UndoableEditSupport();

	public SelectionManager(Canvas canvas, int mode, boolean preserveAspectRatio) {
		super(mode);
		this.canvas = canvas;
		this.preserveAspectRatio = preserveAspectRatio;
		state = STATE_NONE;
		undoSupport.addUndoableEditListener(canvas.getUndoManager());
	}

	@Override
	public boolean canPaint() {
		return true;
	}

	@Override
	public void init(Object... args) {
		canvas.setCursor(Cursor.getDefaultCursor());
		state = STATE_NONE;
	}

	public void paint(Graphics2D g2) {
		 CanvasItem[] selection = canvas.getSelectedItems();
		if (selection == null)
			return;
		if (state == STATE_NONE) {
			drawSelectionMark(selection, g2);
		} else if (state == STATE_MARQUEE) {
			drawSelectionMark(selection, g2);
			g2.setColor(MARQUEE_COLOR);
			g2.setStroke(MARQUEE_STROKE);
			g2.drawRect(marquee.x, marquee.y, marquee.width, marquee.height);
		} else if (state == STATE_RESIZE) {
			for (CanvasItem item : selection) {
				if (item == null) {
					continue;
				}
				drawResizeBox(g2, item);
			}
		} else if (state == STATE_MOVE) {// highlight when moving the
			// intermediate points
			drawSelectionMark(selection, g2);
		}
	}

	private void drawSelectionMark(CanvasItem[] selection, Graphics2D g2) {
		for (CanvasItem item : selection) {
			if (item == null) {
				continue;
			}
			if (item instanceof ComponentCanvasItem)
				item.drawSelectionBox(g2, item, resize);// in case of items
			else if (item instanceof ConnectionItem) {
				item.drawSelectionLine(g2, item);// in case of connection
			}
		}
	}

	private void drawResizeBox(Graphics2D g2, CanvasItem item) {
		item.getNormBounds(r);
		// int rw = (int) (item.getWidth() * resWidth);
		// int rh = (int) (item.getHeight() * resHeight);
		int rw = resWidth, rh = resHeight;
		g2.setColor(Color.black);
		switch (resize) {
		case NW:
			g2.drawRect(r.x + rw, r.y + rh, r.width - rw, r.height - rh);
			break;
		case NE:
			g2.drawRect(r.x, r.y + rh, r.width + rw, r.height - rh);
			break;
		case SW:
			g2.drawRect(r.x + rw, r.y, r.width - rw, r.height + rh);
			break;
		case SE:
			g2.drawRect(r.x, r.y, r.width + rw, r.height + rh);
			break;
		case N:
			g2.drawRect(r.x, r.y + rh, r.width, r.height - rh);
			break;
		case E:
			g2.drawRect(r.x, r.y, r.width + rw, r.height);
			break;
		case S:
			g2.drawRect(r.x, r.y, r.width, r.height + rh);
			break;
		case W:
			g2.drawRect(r.x + rw, r.y, r.width - rw, r.height);
			break;
		}
	}

	private Rectangle r = new Rectangle();

	protected int fx, fy;

	private int ix, iy;

	private int moveDx, moveDy;// record move distance
	private boolean componentMoved, lineMoved, controlPointMoved;

	private ConnectionItem selectedConnectionItem = null;
	private Line2D lineAdjusted = null;
	private Point2D controlPointAdjusted = null;

	@Override
	public void finish() {

	}

	public void update(int eventX, int eventY) {
		if (!(canvas instanceof DiagramCanvas)) {
			return;
		}
		List<CanvasItem> selectedList = new ArrayList<CanvasItem>();
		CanvasItem[] items = canvas.getCanvasItems();
		ConnectionItem[] cis = canvas.getConnections();
		if (state == STATE_NONE) {
			DiagramCanvas dc = ((DiagramCanvas) canvas);
			Line2D l;
			for (ConnectionItem con : cis) {
				LinkedList<Point2D> corners = con.getCornerPoints();
				boolean cornerSelected = false;
				boolean lineSelected = false;
				for (Point2D p : corners) {
					if (dc.isIntermediatePointMoved(p, eventX, eventY)) {
						cornerSelected = true;
						break;
					}
				}
//				if (cornerSelected)
//					break;
			}
			for (ConnectionItem con : cis) {
				LinkedList<Point2D> corners = con.getCornerPoints();
				for (int i = 0; i < corners.size() - 1; i++) {
					l = new Line2D.Double(corners.get(i), corners.get(i + 1));
					if (dc.isLineMoved(l, eventX, eventY)) {
//						lineSelected = true;
						canvas.setCursor(Cursor
								.getPredefinedCursor(Cursor.MOVE_CURSOR));
						break;
					}
				}
//				if (lineSelected)
//					break;
			}// finish checking the connection items

			// set boundary of marquee box
			marquee.x = Math.min(ix, fx);
			marquee.y = Math.min(iy, fy);
			marquee.width = Math.abs(fx - ix + 1);
			marquee.height = Math.abs(fy - iy + 1);

			// selectionCount = 0;
			for (int i = items.length - 1; i >= 0; i--) {
				if (items[i].getBounds().intersects(marquee)) {
					CanvasItem[] sel = canvas.getSelectedItems();
					boolean wasSelected = false;
					if (sel != null) {
						for (CanvasItem s : sel) {
							if (items[i] == s) {
								wasSelected = true;
								break;
							}
						}
					}

					if (wasSelected) {
						selectedList.addAll(Arrays.asList(sel));
						// System.arraycopy(sel, 0, selection, 0, sel.length);
						// selectionCount = sel.length;
					} else {
						selectedList.add(items[i]);
					}
					break;
				}
			}

			// Maryam: This is for individual connection selection
			// more specifically, it adds connection line to selected items
			Line2D selectedLine = canvas.getSelectedLine();
			Point2D selectedCorner = canvas.getSelectedCorner();

			Point2D p1 = null;
			Point2D p2 = null;

			if (selectedLine != null) {
				p1 = selectedLine.getP1();
				p2 = selectedLine.getP2();
			}

			for (ConnectionItem ci : cis) {
				if (isConnectionInMarquee(ci)
						|| (selectedLine != null && (ci.getCornerPoints()
								.contains(p1) && ci.getCornerPoints().contains(
								p2)))
						|| (selectedCorner != null && ci.getCornerPoints()
								.contains(selectedCorner))) {
					ci.selected = true;
					selectedList.add(ci);
					// selection[selectionCount++] = ci;
				}
			}
			selection = selectedList.toArray(new CanvasItem[0]);
			// selection[selectionCount] = null;
		} else if (state == STATE_MARQUEE) {
			// set boundary of marquee box
			marquee.x = Math.min(ix, fx);
			marquee.y = Math.min(iy, fy);
			marquee.width = Math.abs(fx - ix + 1);
			marquee.height = Math.abs(fy - iy + 1);

			// if (selection == null
			// || selection.length < items.length + cis.length + 1
			// || selection.length > (items.length + cis.length) * 2) {
			// selection = new CanvasItem[items.length + cis.length + 1];
			// }

			// selectionCount = 0;
			for (int i = items.length - 1; i >= 0; i--) {
				boolean b = items[i].getBounds().intersects(marquee);
				if (b) {
					selectedList.add(items[i]);
					// selection[selectionCount++] = items[i];
				}
			}

			// Maryam: This is for individual connection selection
			// more specifically, it adds connection line to selected items
			Line2D selectedLine = canvas.getSelectedLine();
			Point2D selectedCorner = canvas.getSelectedCorner();
			Point2D p1 = null;
			Point2D p2 = null;

			if (selectedLine != null) {
				p1 = selectedLine.getP1();
				p2 = selectedLine.getP2();
			}

			for (ConnectionItem ci : cis) {
				if (isConnectionInMarquee(ci)
						|| (selectedLine != null && (ci.getCornerPoints()
								.contains(p1) && ci.getCornerPoints().contains(
								p2)))
						|| (selectedCorner != null && ci.getCornerPoints()
								.contains(selectedCorner))) {
					ci.selected = true;
					selectedList.add(ci);
					// selection[selectionCount++] = ci;
				}
			}
			// selection[selectionCount] = null;
			selection = selectedList.toArray(new CanvasItem[0]);
		} else if (state == STATE_RESIZE) {
			if (!canvas.isEditable()) {
				return;
			}
			resWidth = snapToGrid(fx - ix);
			resHeight = snapToGrid(fy - iy);
			int s;
			if (preserveAspectRatio) {
				switch (resize) {
				case SE:
					s = Math.min(resWidth, resHeight);
					resWidth = s;
					resHeight = s;
					break;
				case NE:
					s = Math.min(resWidth, -resHeight);
					resWidth = s;
					resHeight = -s;
					break;
				case NW:
					s = Math.min(-resWidth, -resHeight);
					resWidth = -s;
					resHeight = -s;
					break;
				case SW:
					s = Math.min(-resWidth, resHeight);
					resWidth = -s;
					resHeight = s;
					break;
				}
			}

		} else if (state == STATE_MOVE) {

			if (selection != null) {
				int dx = snapToGrid(fx - ix);
				int dy = snapToGrid(fy - iy);
				boolean componentSelected = false;
				boolean connectionSelected = false;

				for (int i = 0; i < selection.length; ++i) {
					if (selection[i] instanceof ComponentCanvasItem) {
						componentSelected = true;
					} else if (selection[i] instanceof ConnectionItem) {
						connectionSelected = true;
					}
					if (componentSelected && connectionSelected)
						break;
				}

				if (componentSelected) {
					canvas.move(selection, dx, dy);
					componentMoved = true;
				} else {
					if (canvas.getSelectedCorner() != null) {
						// canvas.move(selection, dx, dy);
						// controlPointMoved = true;
						for (ConnectionItem ci : canvas.getConnections()) {
							if (ci.getCornerPoints().contains(
									canvas.getSelectedCorner())) {
								controlPointAdjusted = canvas
										.updateIntermediatePoints(ci, canvas
												.getSelectedCorner(), dx, dy);
								controlPointMoved = true;
								selectedConnectionItem = ci;
								break;
							}
						}
					} else if (canvas.getSelectedLine() != null) {
						// canvas.move(selection, dx, dy);
						// lineMoved = true;
						for (ConnectionItem ci : canvas.getConnections()) {
							if (ci.getCornerPoints().contains(
									canvas.getSelectedLine().getP1())
									&& ci.getCornerPoints().contains(
											canvas.getSelectedLine().getP2())) {
								lineAdjusted = canvas.updateLine(ci, canvas
										.getSelectedLine(), dx, dy);
								lineMoved = true;
								selectedConnectionItem = ci;
								break;
							}
						}
					} else {
					}
				}

				moveDx += dx;
				moveDy += dy;
				ix += dx;
				iy += dy;
			}
		}
	}

	/**
	 * @param event
	 * @author sam
	 * @deprecated
	 */
	public void update(Point event) {
		if (!(canvas instanceof DiagramCanvas)) {
			return;
		}

		if (state == STATE_NONE) {
			DiagramCanvas dc = ((DiagramCanvas) canvas);
			CanvasItem[] items = canvas.getCanvasItems();
			ConnectionItem[] cis = canvas.getConnections();
			Line2D l;
			for (ConnectionItem con : cis) {

				LinkedList<Point2D> corners = con.getCornerPoints();
				for (Point2D p : corners) {
					if (event != null)
						if (dc.isIntermediatePointMoved(p, event.x, event.y)) {
							break;
						}
				}
				/*********************************************************/
				for (int i = 0; i < corners.size() - 1; i++) {
					l = new Line2D.Double(corners.get(i), corners.get(i + 1));
					if (event != null)
						if (dc.isLineMoved(l, event.x, event.y)) {
							canvas.setCursor(Cursor
									.getPredefinedCursor(Cursor.MOVE_CURSOR));
							break;
						}
				}
			}

			// set boundary of marquee box
			marquee.x = Math.min(ix, fx);
			marquee.y = Math.min(iy, fy);
			marquee.width = Math.abs(fx - ix + 1);
			marquee.height = Math.abs(fy - iy + 1);

			if (selection == null
					|| selection.length < items.length + cis.length + 1
					|| selection.length > (items.length + cis.length) * 2) {
				selection = new CanvasItem[items.length + cis.length + 1];
			}
			selectionCount = 0;
			for (int i = items.length - 1; i >= 0; i--) {

				boolean b = items[i].getBounds().intersects(marquee);
				if (b) {
					selection[selectionCount++] = items[i];
					CanvasItem[] sel = canvas.getSelectedItems();
					boolean wasSelected = false;
					if (sel != null) {
						for (CanvasItem s : sel) {
							if (items[i] == s) {
								wasSelected = true;
								break;
							}
						}
					}

					if (wasSelected) {
						System.arraycopy(sel, 0, selection, 0, sel.length);
						selectionCount = sel.length;
					}
					break;
				}
			}

			// Maryam: This is for individual connection selection
			// more specifically, it adds connection line to selected items
			Line2D selectedLine = canvas.getSelectedLine();
			Point2D selectedCorner = canvas.getSelectedCorner();
			Point2D p1 = null;
			Point2D p2 = null;

			if (selectedLine != null) {
				p1 = selectedLine.getP1();
				p2 = selectedLine.getP2();
			}

			for (ConnectionItem ci : cis) {

				if (isConnectionInMarquee(ci)
						|| (selectedLine != null && (ci.getCornerPoints()
								.contains(p1) && ci.getCornerPoints().contains(
								p2)))
						|| (selectedCorner != null && ci.getCornerPoints()
								.contains(selectedCorner))) {
					ci.selected = true;
					selection[selectionCount++] = ci;
				}
			}

			selection[selectionCount] = null;

		} else if (state == STATE_MARQUEE) {
			// set boundary of marquee box
			marquee.x = Math.min(ix, fx);
			marquee.y = Math.min(iy, fy);
			marquee.width = Math.abs(fx - ix + 1);
			marquee.height = Math.abs(fy - iy + 1);

			CanvasItem[] items = canvas.getCanvasItems();// ConnectionItem is
			// not retrieved by
			// this function
			ConnectionItem[] cis = canvas.getConnections();// so we retrieve
			// them through
			// getConnections()

			if (selection == null
					|| selection.length < items.length + cis.length + 1
					|| selection.length > (items.length + cis.length) * 2) {
				selection = new CanvasItem[items.length + cis.length + 1];
			}

			selectionCount = 0;
			for (int i = items.length - 1; i >= 0; i--) {

				boolean b = items[i].getBounds().intersects(marquee);
				if (b) {
					selection[selectionCount++] = items[i];
				}
			}

			// Maryam: This is for individual connection selection
			// more specifically, it adds connection line to selected items
			Line2D selectedLine = canvas.getSelectedLine();
			Point2D selectedCorner = canvas.getSelectedCorner();
			Point2D p1 = null;
			Point2D p2 = null;

			if (selectedLine != null) {
				p1 = selectedLine.getP1();
				p2 = selectedLine.getP2();
			}

			for (ConnectionItem ci : cis) {

				if (isConnectionInMarquee(ci)
						|| (selectedLine != null && (ci.getCornerPoints()
								.contains(p1) && ci.getCornerPoints().contains(
								p2)))
						|| (selectedCorner != null && ci.getCornerPoints()
								.contains(selectedCorner))) {
					ci.selected = true;
					selection[selectionCount++] = ci;
				}
			}

			selection[selectionCount] = null;
		} else if (state == STATE_RESIZE) {

			if (!canvas.isEditable()) {
				return;
			}
			resWidth = snapToGrid(fx - ix);
			resHeight = snapToGrid(fy - iy);
			int s;
			if (preserveAspectRatio) {
				switch (resize) {
				case SE:
					s = Math.min(resWidth, resHeight);
					resWidth = s;
					resHeight = s;
					break;
				case NE:
					s = Math.min(resWidth, -resHeight);
					resWidth = s;
					resHeight = -s;
					break;
				case NW:
					s = Math.min(-resWidth, -resHeight);
					resWidth = -s;
					resHeight = -s;
					break;
				case SW:
					s = Math.min(-resWidth, resHeight);
					resWidth = -s;
					resHeight = s;
					break;
				}
			}

		} else if (state == STATE_MOVE) {
			int dx = snapToGrid(fx - ix);
			int dy = snapToGrid(fy - iy);
			boolean componentSelected = false;
			boolean connectionSelected = false;
			for (int i = 0; i < selection.length; ++i) {
				if (selection[i] instanceof ComponentCanvasItem) {
					componentSelected = true;
				} else if (selection[i] instanceof ConnectionItem) {
					connectionSelected = true;
				}
			}

			if (componentSelected) {
				canvas.move(selection, dx, dy);
				componentMoved = true;
			} else {
				if (canvas.getSelectedCorner() != null) {
					// canvas.move(selection, dx, dy);
					// controlPointMoved = true;
					for (ConnectionItem ci : canvas.getConnections()) {
						if (ci.getCornerPoints().contains(
								canvas.getSelectedCorner())) {
							controlPointAdjusted = canvas
									.updateIntermediatePoints(ci, canvas
											.getSelectedCorner(), dx, dy);
							controlPointMoved = true;
							selectedConnectionItem = ci;
							break;
						}
					}
				} else if (canvas.getSelectedLine() != null) {
					// canvas.move(selection, dx, dy);
					// lineMoved = true;
					for (ConnectionItem ci : canvas.getConnections()) {
						if (ci.getCornerPoints().contains(
								canvas.getSelectedLine().getP1())
								&& ci.getCornerPoints().contains(
										canvas.getSelectedLine().getP2())) {
							lineAdjusted = canvas.updateLine(ci, canvas
									.getSelectedLine(), dx, dy);
							lineMoved = true;
							selectedConnectionItem = ci;
							break;
						}
					}
				} else {
				}
			}

			moveDx += dx;
			moveDy += dy;
			ix += dx;
			iy += dy;
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		double scale = canvas.getCanvasScale();
		// this section comes from mousepressed section
		Point p = MiscUtils.scaleLoc(event.getX(), event.getY(), scale);

		ix = fx = p.x;
		iy = fy = p.y;
		if (resize >= 0) {
			state = STATE_RESIZE;
		}

		// update(new Point(event.getX(), event.getY()));
		update(p.x, p.y);

		if (selection == null || selection.length == 0) {
			// selection is an array converted from a list. so it could be
			// non-null, with the length equals to zero
			state = STATE_MARQUEE;
		} else if (state != STATE_RESIZE) {
			state = STATE_MOVE;
			moveDx = 0;
			moveDy = 0;

			Point2D.Double[] oldLocationsArray = new Point2D.Double[selection.length];
			int length = 0;
			for (CanvasItem item : selection) {
				oldLocationsArray[length++] = item.getLocation();
			}
			oldLocations = new Point2D.Double[length];
			System.arraycopy(oldLocationsArray, 0, oldLocations, 0, length);
		}

	}

	@Override
	public void onMouseDrag(MouseMoveEvent event) {
		double scale = canvas.getCanvasScale();
		Point p = MiscUtils.scaleLoc(event.getX(), event.getY(), scale);

		fx = p.x;
		fy = p.y;
		// update(new Point(event.getX(), event.getY()));
		update(p.x, p.y);

		if (componentMoved) {
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {

		double scale = canvas.getCanvasScale();
		Point p = MiscUtils.scaleLoc(event.getX(), event.getY(), scale);
		checkResize((int) p.x, (int) p.y);
		if (resize < 0) {
			canvas.setCursor(Cursor.getDefaultCursor());
		}

		// set the move cursor
		if (componentMoved) {
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		double scale = canvas.getCanvasScale();
		Point p = MiscUtils.scaleLoc(event.getX(), event.getY(), scale);
		// this section comes from mouseReleased section
		// update(new Point(event.getX(), event.getY()));
		update(p.x, p.y);
		if (state == STATE_MOVE) {
			if (componentMoved) {
				undoSupport.postEdit(new MoveComponentEdit(canvas, selection,
						moveDx, moveDy));
			} else if (lineMoved) {
				undoSupport.postEdit(new AdjustConnectionLineEdit(canvas,
						selectedConnectionItem, lineAdjusted, moveDx, moveDy));
			} else if (controlPointMoved) {
				undoSupport.postEdit(new AdjustConnectionControlPointEdit(
						canvas, selectedConnectionItem, controlPointAdjusted,
						moveDx, moveDy));
			}

			componentMoved = false;
			controlPointMoved = false;
			lineMoved = false;

			selectedConnectionItem = null;
			controlPointAdjusted = null;
			lineAdjusted = null;

			// set the cursor back
			canvas.setCursor(Cursor.getDefaultCursor());
		}

		if (state == STATE_RESIZE) {
			// canvas.resize(selection, resize, resWidth, resHeight);
			undoSupport.postEdit(new ResizeComponentEdit(canvas, selection,
					resize, resWidth, resHeight));
		} else {
			if (selection != null) {
				// CanvasItem[] sci = new CanvasItem[selectionCount];
				// for (int i = 0; i < selectionCount; i++) {
				// sci[i] = selection[i];
				// }
				canvas.setSelectedItems(selection);
			} else {
				canvas.setSelectedItems(null);
			}
		}

		state = STATE_NONE;
	}

	private void checkResize(int x, int y) {
		CanvasItem[] selItems = canvas.getSelectedItems();
		if (selItems == null) {
			resitem = null;
			resize = -1;
			return;
		}
		for (CanvasItem item : selItems) {
			if (item.isResizable()) {
				item.getNormBounds(r);
				r.x -= HANDLE_SIZE;
				r.y -= HANDLE_SIZE;
				r.width += 2 * HANDLE_SIZE;
				r.height += 2 * HANDLE_SIZE;
				if (!r.contains(x, y)) {
					continue;
				}
				int x1 = r.x + r.width - HANDLE_SIZE, y1 = r.y + r.height
						- HANDLE_SIZE;
				int x2 = r.x + (r.width - HANDLE_SIZE) / 2, y2 = r.y
						+ (r.height - HANDLE_SIZE) / 2;
				// NW
				if (x >= r.getX() && y >= r.y && x <= r.getX() + HANDLE_SIZE
						&& y <= r.y + HANDLE_SIZE) {
					canvas.setCursor(Cursor
							.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					resitem = item;
					resize = NW;
					return;
				}
				// NE
				else if (x >= x1 && y >= r.y && x <= x1 + HANDLE_SIZE
						&& y <= r.y + HANDLE_SIZE) {
					canvas.setCursor(Cursor
							.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					resitem = item;
					resize = NE;
					return;
				}
				// SW
				else if (x >= r.getX() && y >= y1
						&& x <= r.getX() + HANDLE_SIZE && y <= y1 + HANDLE_SIZE) {
					canvas.setCursor(Cursor
							.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					resitem = item;
					resize = SW;
					return;
				}
				// SE
				else if (x >= x1 && y >= y1 && x <= x1 + HANDLE_SIZE
						&& y <= y1 + HANDLE_SIZE) {
					canvas.setCursor(Cursor
							.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					resitem = item;
					resize = SE;
					return;
				} else if (!preserveAspectRatio) {
					// N
					if (x >= x2 && y >= r.y && x <= x2 + HANDLE_SIZE
							&& y <= r.y + HANDLE_SIZE) {
						canvas.setCursor(Cursor
								.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						resitem = item;
						resize = N;
						return;
					}
					// E
					else if (x >= x1 && y >= y2 && x <= x1 + HANDLE_SIZE
							&& y <= y2 + HANDLE_SIZE) {
						canvas.setCursor(Cursor
								.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));

						resitem = item;
						resize = E;
						return;
					}
					// S
					else if (x >= x2 && y >= y1 && x <= x2 + HANDLE_SIZE
							&& y <= y1 + HANDLE_SIZE) {
						canvas.setCursor(Cursor
								.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
						resitem = item;
						resize = S;
						return;
					}
					// W
					else if (x >= r.getX() && y >= y2
							&& x <= r.getX() + HANDLE_SIZE
							&& y <= y2 + HANDLE_SIZE) {
						canvas.setCursor(Cursor
								.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
						resitem = item;
						resize = W;
						return;
					}
				}
			}
		}
		resitem = null;
		resize = -1;
		return;
	}

	/**
	 * @author Maryam
	 * @function This function is to check whether the marquee has intersected
	 *           with any of the connection lines-works perfect
	 */
	private boolean isConnectionInMarquee(ConnectionItem ci) {
		boolean intersected = false;
		Line2D l1 = null;
		Line2D l3 = null;
		Line2D l4 = null;
		Line2D l2;
		List<Point2D> corners = new LinkedList<Point2D>();
		// corners = ci.getCornerPoints();
		ComponentCanvasItem srcComp = ci.src.getComponentCanvasItem();
		ComponentCanvasItem destComp = ci.dest.getComponentCanvasItem();
		ConnectorItem src = ci.getSource();
		ConnectorItem dest = ci.getDestination();

		Point2D start = new Point2D.Double(srcComp.getX() + ci.src.getX()
				+ ci.src.getHitX(), srcComp.getY() + ci.src.getY()
				+ ci.src.getHitY());

		Point2D end = new Point2D.Double(destComp.getX() + ci.dest.getX()
				+ ci.dest.getHitX(), destComp.getY() + ci.dest.getY()
				+ ci.dest.getHitY());

		Point2D start_heat_port = new Point2D.Double(srcComp.getX()
				+ srcComp.getWidth() / 2, srcComp.getY());

		Point2D end_heat_port = new Point2D.Double(destComp.getX()
				+ destComp.getWidth() / 2, destComp.getY());

		String src_type = ci.src.connector.name;
		String dest_type = ci.src.connector.name;

		// TODO later should be generalized
		if (corners.size() > 0) {
			if (src_type != "h" && dest_type != "h") {
				l1 = new Line2D.Double(start, corners.get(0));
				l3 = new Line2D.Double(corners.get(corners.size() - 1), end);
			} else if (src_type == "h" && dest_type == "h") {
				l1 = new Line2D.Double(start_heat_port, corners.get(0));
				l3 = new Line2D.Double(corners.get(corners.size() - 1),
						end_heat_port);
			}
			// TODO later should be removed, will never happen
			else if (src_type == "h" && dest_type != "h") {
				l1 = new Line2D.Double(start_heat_port, corners.get(0));
				l3 = new Line2D.Double(corners.get(corners.size() - 1), end);
			} else if (src_type != "h" && dest_type == "h") {
				l1 = new Line2D.Double(start, corners.get(0));
				l3 = new Line2D.Double(corners.get(corners.size() - 1),
						end_heat_port);
			}
		}

		if (corners.size() == 0) {
			if (src_type != "h" && dest_type != "h")
				l4 = new Line2D.Double(start, end);
			else if (src_type == "h" && dest_type == "h")
				l4 = new Line2D.Double(start_heat_port, end_heat_port);
			// TODO later should be removed, will never happen
			else if (src_type == "h" && dest_type != "h")
				l4 = new Line2D.Double(start_heat_port, end);
			else if (src_type != "h" && dest_type == "h")
				l4 = new Line2D.Double(start, end_heat_port);
		}

		if ((l1 != null && marquee.intersectsLine(l1))
				|| (l3 != null && marquee.intersectsLine(l3))
				|| (l4 != null && marquee.intersectsLine(l4))) {
			intersected = true;
		}

		for (int i = 0; i < corners.size() - 1; i++) {
			l2 = new Line2D.Double(corners.get(i), corners.get(i + 1));
			if (marquee.intersectsLine(l2)) {
				intersected = true;
				break;
			}
		}

		// TODO: this is for the select all function-Although everything looks
		// correct, still in select-all
		// connection lines are not highlighted!!
		Rectangle src_rect = new Rectangle((int) ((srcComp.getX() + ci.src
				.getX())), (int) ((srcComp.getY() + ci.src.getY())), (int) src
				.getWidth(), (int) src.getHeight());
		Rectangle dest_rect = new Rectangle((int) ((destComp.getX() + ci.dest
				.getX())), (int) ((destComp.getY() + ci.dest.getY())),
				(int) dest.getWidth(), (int) dest.getHeight());

		if (marquee.contains(src_rect) && marquee.contains(dest_rect)) {
			intersected = true;
		}

		return intersected;
	}

	private int snapToGrid(int d_) {
		if (gridSize > 1) {
			return (d_ / gridSize) * gridSize;
		}
		return d_;
	}
}
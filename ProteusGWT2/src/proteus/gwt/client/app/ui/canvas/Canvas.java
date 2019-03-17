package proteus.gwt.client.app.ui.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.LogMessagePanelEvent;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.client.app.ui.MessagePanel.Message;
import proteus.gwt.client.app.ui.canvas.action.ComponentActions;
import proteus.gwt.client.app.ui.canvas.action.ConnectionActions;
import proteus.gwt.client.app.ui.canvas.edit.AddConnectionEdit;
import proteus.gwt.client.app.ui.util.Constant;
import proteus.gwt.client.app.ui.util.GraphicUtils;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Cursor;
import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Paint2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.modelica.ClassDef;
import proteus.gwt.shared.modelica.ModelicaLoader;
import proteus.gwt.shared.modelica.Templates;
import proteus.gwt.shared.modelica.ClassDef.Equation;
import proteus.gwt.shared.modelica.parser.ModelicaParser;
import proteus.gwt.shared.modelica.parser.OMEquation;
import proteus.gwt.shared.util.StringReader;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

import emu.java.swing.undo.UndoManager;
import emu.java.swing.undo.UndoableEditSupport;

public class Canvas extends Paint2D implements
// HasAllMouseHandlers,
		// HasAllKeyHandlers, HasClickHandlers, HasDoubleClickHandlers,
		// HasFocusHandlers,
		ComponentActions, ConnectionActions {

	private static final int DEFAULT_SCALE_INDEX = 3;

	private static Dimension diagramSize = Constant.diagramCanvasSize;

	public static final double[] SCALE_LEVELS = { 0.25, 0.5, 0.75, 1.0, 1.25,
			1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.25, 3.5 };// maryam

	protected int _mode;
	protected int scaleIndex = DEFAULT_SCALE_INDEX;

	public double canvasScale = SCALE_LEVELS[scaleIndex];

	protected ClassDef classDef;

	// This class will be corresponds to the Canvas class in Proteus
	protected int connMgrMode;

	protected ModeHandler currModeHandler;

	private boolean editable = true;

	protected CanvasItem hitItem;

	boolean is_resized = false;

	private final int MAX_CONNECTION = 20;// Max number of connection that a

	protected ModeHandler[] modeHandlers;
	protected CanvasModel model;

	protected double scale = SCALE_LEVELS[scaleIndex];

	private Point2D selected_corner;

	private ComponentCanvasItem selected_item;// to move

	private Line2D selected_line;// TODO needs = null for initialization?

	protected int selMgrMode, moveMgrMode;
	Dimension size;
	protected ConnectorItem src, dest;

	protected UndoManager undoManager = new UndoManager();

	protected UndoableEditSupport undoSupport = new UndoableEditSupport();

	protected Map<ConnectionItem, Equation> connectMap = new HashMap<ConnectionItem, Equation>();

	public Canvas(CanvasModel model) {
		super(diagramSize.width, diagramSize.height);
		setCanvasSize(diagramSize);// maryam added TODO remove if
		// extra
		this.model = model;
		this.setBackground(Color.white);
		modeHandlers = createModeHandlers();
		currModeHandler = modeHandlers[0];

		// pay us S$1.99 to get another 50 undo limit now
		undoManager.setLimit(2);
		undoSupport.addUndoableEditListener(undoManager);
	}

	public void add(CanvasItem item) {
		model.add(item);

		item.notifyAdd(this);
		String info = "An item, " + item.getName() + ", has been added.";
		ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(Message.INFO,
				info));
		item.setPopupMenu(createPopupPanel(item));
	}

	public void add(CanvasItem[] items) {
		String info = "";
		model.add(items);
		for (CanvasItem item : items) {
			item.notifyAdd(this);
			info = "An item, " + item.getName() + ", has been added." + "<br/>";
			item.setPopupMenu(createPopupPanel(item));
			ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
					Message.INFO, info));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ConnectionActions#add(proteus
	 * .gwt.client.app.ui.canvas.ConnectionItem[])
	 */
	@Override
	public void add(ConnectionItem[] items) {
		String info = "";
		for (int i = 0; i < items.length; ++i) {
			info += addConnection(items[i]);
		}
		ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(Message.INFO,
				info));
		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
	}

	public String addConnection(ConnectionItem connection) {
		String template = Templates.CONNECT;
		template = template.replace("@src@", connection.src.getName()).replace(
				"@dest@", connection.dest.getName());
		LinkedList<Point2D> points = connection.getCornerPoints();

		String replaceString = "";
		Point2D p = null;
		if (points.size() > 0) {
			p = GraphicUtils.fromPixel(points.get(0));
			replaceString = "{" + p.getX() + "," + p.getY() + "}";
			for (int i = 1; i < points.size(); i++) {
				p = GraphicUtils.fromPixel(points.get(i));
				replaceString += ", {" + p.getX() + "," + p.getY() + "}";
			}
			template = template.replace("{@x1@,@y1@}", replaceString);
		} else {
			template = template.replace(
					"annotation(Line(points= {{@x1@,@y1@}}))", "");
		}

		ModelicaParser parser = new ModelicaParser(new StringReader(template));
		String info = "";
		try {
			OMEquation ome = parser.equation();
			Equation equation = (Equation) ome.jjtAccept(new ModelicaLoader(),
					null);
			// @@sam comment out@@
			if (equation == null || classDef == null) {
				System.err.println("@@@@" + template);
			}
			classDef.addEquation(equation);
			connectMap.put(connection, equation);

			connection.initConnect(equation);
			info = "A connection between " + connection.getSource().getName()
					+ " and " + connection.getDestination().getName()
					+ " has been created." + "<br/>";
			connection.updateCodeCanvas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public void addConnections(ConnectionItem[] items) {
		String info = "";
		for (int i = 0; i < items.length; ++i) {
			info += addConnection(items[i]);
		}
		ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(Message.INFO,
				info));
		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ConnectionActions#addControlPoint
	 * (proteus.gwt.client.app.ui.canvas.ConnectionItem,
	 * proteus.gwt.client.graphics.geom.Point2D)
	 */
	@Override
	public void addControlPoint(ConnectionItem item, Point2D point) {
		// TODO not implemented yet
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ConnectionActions#adjustControlPoint
	 * (proteus.gwt.client.app.ui.canvas.ConnectionItem,
	 * proteus.gwt.client.graphics.geom.Point2D, int, int)
	 */
	@Override
	public Point2D adjustControlPoint(ConnectionItem item, Point2D point,
			int dx, int dy) {
		return updateIntermediatePoints(item, point, dx, dy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ConnectionActions#adjustLine(
	 * proteus.gwt.client.app.ui.canvas.ConnectionItem,
	 * proteus.gwt.client.graphics.geom.Line2D, int)
	 */
	@Override
	public Line2D adjustLine(ConnectionItem item, Line2D line, int dx, int dy) {
		return updateLine(item, line, dx, dy);
	}

	public void cancelConnection() {
		src = null;
		setMode(_mode);
		String info = "The connection has been cancelled.";
		ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(Message.INFO,
				info));
		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
	}

	public void completeConnection(ConnectorItem connItem) {
		dest = connItem;

		ConnectionManager cm = (ConnectionManager) modeHandlers[connMgrMode];

		// Maryam: check whether the same connection is not already available
		if (getConnection(src, dest) != null) {
			return;
		}
		// @sam, 5, May, 2011. move auto generate cornerPoints into
		// ConnectionItem constructor
		LinkedList<Point2D> cornerPoints = new LinkedList<Point2D>();

		ConnectionItem connection = new ConnectionItem(this, src, dest);

		ComponentCanvasItem srcComp = src.getComponentCanvasItem();
		ComponentCanvasItem destComp = dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double((srcComp.getX() + src.getX() + src
				.getHitX()), (srcComp.getY() + src.getY() + src.getHitY()));

		Point2D end = new Point2D.Double((destComp.getX() + dest.getX() + dest
				.getHitX()), (destComp.getY() + dest.getY() + dest.getHitY()));

		// TODO: new algorithm should be devised which can jump over the item if
		// the item is on the way
		if (start.getX() == end.getX() || start.getY() == end.getY()) {// no
			// need
			// to
			// insert
			// intermediate
			// points
		} else {
			cornerPoints.add(new Point2D.Double((int) ((start.getX() + end
					.getX()) / 2), start.getY()));
			cornerPoints.add(new Point2D.Double((int) ((start.getX() + end
					.getX()) / 2), end.getY()));
		}

		connection.getCornerPoints().addAll(cornerPoints);

		// addConnection(connection);
		undoSupport.postEdit(new AddConnectionEdit(this,
				new ConnectionItem[] { connection }));
		// go back to normal
		src = dest = null;
		setMode(_mode);
		setCursor(Cursor.getDefaultCursor());
		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
	}

	public void connectorSelected(ConnectorItem connItem) {
		// this is method has been override inside diagramcanvas
		if (isConnectionInProgress()) {
			completeConnection(connItem);
		} else {
			startConnection(connItem);
		}
	}

	public String createDeclName(String prefix) {
		int index = 0;
		boolean taken;
		String name = null;
		do {
			if (name == null) {
				// Sam Apr 12 made to lowercase
				name = prefix.toLowerCase();
				if (index >= 0) {
					name += index;
				}
				index++;
			}
			taken = false;
			for (CanvasItem exist : getCanvasItems()) {
				if (exist.getName().equals(name)) {
					taken = true;
					name = null;
					break;
				}
			}
		} while (taken);

		return name;
	}

	public ModeHandler[] createModeHandlers() {
		ModeHandler[] mhs = new ModeHandler[2];
		int n = 0;
		mhs[n] = new SelectionManager(this, n, isPreserveAspectRatio());
		selMgrMode = n++;
		mhs[n] = new MoveModeManager(this, n);
		moveMgrMode = n++;
		return mhs;
	}

	private PopupPanel createPopupPanel(CanvasItem item) {
		PopupPanel itemPopup = new PopupPanel(true);
		MenuBar popupMenuBar = new MenuBar(true);
		MenuItem menuItem;
		if (item instanceof ComponentCanvasItem) {
			ComponentCanvasItem cci = (ComponentCanvasItem) item;

			// String key = Constant.DISCONNECTIONACTION;
			// menuItem = new MenuItem(key, cci.getAction(key));
			// popupMenuBar.addItem(menuItem);
			String key;
			
			key = Constant.ATTRIBUTEACTION;
			menuItem = new MenuItem(key, cci.getAction(key));
			popupMenuBar.addItem(menuItem);

			key = Constant.OPENCOMPONENTACTION;
			menuItem = new MenuItem(key, cci.getAction(key));
			popupMenuBar.addItem(menuItem);

			key = Constant.PROPERTIESACTION;
			menuItem = new MenuItem(key, cci.getAction(key));
			popupMenuBar.addItem(menuItem);
		}

		popupMenuBar.setVisible(true);
		itemPopup.add(popupMenuBar);
		// itemPopup.setStyleName("popup");

		return itemPopup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ComponentActions#flip(proteus
	 * .gwt.client.app.ui.canvas.CanvasItem[], boolean)
	 */
	@Override
	public void flip(CanvasItem[] items, boolean horizontal) {
		if (horizontal) {
			CanvasUtils.flipHorizontal(items);
		} else {
			CanvasUtils.flipVertical(items);
		}
		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
	}

	public CanvasItem[] getCanvasItems() {
		return model.getCanvasItems();
	}

	public double getCanvasScale() {
		return canvasScale;
	}

	public Dimension getCanvasSize() {
		return size;
	}

	public ConnectionItem getConnection(ConnectorItem src, ConnectorItem dest) {
		ConnectionItem found = null;
		for (ConnectionItem con : getConnections()) {
			if (con.getSource() == src && con.getDestination() == dest) {
				found = con;
				break;
			}
		}
		return found;
	}

	/**
	 * @author Maryam
	 * @func this function receives an intermediate point as input and returns
	 *       the connection item which contains that. This is used for
	 *       highlighting the connection line when mouse is around it
	 * @return
	 */
	public ConnectionItem getConnectionItem(Point2D p1, Point2D p2) {
		ConnectionItem found = null;

		for (ConnectionItem ci : getConnections()) {
			if (ci.getCornerPoints().contains(p1)
					|| ci.getCornerPoints().contains(p2)) {
				found = ci;
				// System.out.println("ci.src, ci.dest: " + ci.getSource().name
				// + ", " + ci.getDestination().name);
				break;
			}
		}
		return found;
	}

	public ConnectionItem[] getConnections() {
		Set<ConnectionItem> connSet = connectMap.keySet();
		return connSet.toArray(new ConnectionItem[0]);
	}

	/**
	 * @author Maryam
	 * @func This function is used in updating intermediate points after
	 *       flip/rotation
	 */
	public ConnectionItem[] getConnections(ConnectorItem ci) {

		ConnectionItem[] cons = getConnections();
		List<ConnectionItem> found = new LinkedList<ConnectionItem>();

		int j = 0;
		for (int i = 0; i < cons.length; i++) {
			if (cons[i].getSource() == ci || cons[i].getDestination() == ci) {
				found.add(cons[i]);
				j++;
			}
		}
		if (j == 0)
			return null;
		else
			return found.toArray(new ConnectionItem[0]);
	}

	public ConnectorItem getDestination() {
		return dest;
	}

	public int getMode() {
		for (int i = 0; i < modeHandlers.length; i++) {
			if (currModeHandler == modeHandlers[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return the model
	 */
	public CanvasModel getModel() {
		return model;
	}

	public Point2D getSelectedCorner() {
		return selected_corner;
	}

	public ComponentCanvasItem getSelectedItem() {
		return selected_item;
	}

	public CanvasItem[] getSelectedItems() {
		return model.getSelectedItems();
	}

	public Line2D getSelectedLine() {
		return selected_line;
	}

	public ConnectorItem getSource() {
		return src;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public UndoableEditSupport getUndoSupport() {
		return undoSupport;
	}

	public CanvasItem hit(Point loc) {
		CanvasItem[] items = getCanvasItems();
		for (CanvasItem item : items) {
			item.clearConnectorCover();
			if (item.getBounds().contains(loc)) {
				return item;
			}
		}
		return null;
	}

	public boolean isConnectionInProgress() {
		return src != null;
	}

	public boolean isEditable() {
		return editable;
	}

	protected boolean isPreserveAspectRatio() {
		return false;
	}

	/**
	 * @author Maryam
	 * @func merge first intermediate point with src connection point
	 */
	public void mergeFirst(LinkedList<Point2D> ps, Point2D end) {

		int x1, y1, x2, y2;
		x1 = (int) end.getX();
		y1 = (int) end.getY();

		if (ps.size() > 0) {
			x2 = (int) ps.getLast().getX();
			y2 = (int) ps.getLast().getY();
			if (x1 == x2 && y1 == y2)
				ps.removeLast();
		}
	}

	/**
	 * @author Maryam
	 * @func merge last intermediate point with dest connection point
	 */
	public void mergeLast(LinkedList<Point2D> ps, Point2D start) {

		int x1, y1, x2, y2;
		x1 = (int) start.getX();
		y1 = (int) start.getY();

		if (ps.size() > 0) {
			x2 = (int) ps.getFirst().getX();
			y2 = (int) ps.getFirst().getY();
			if (x1 == x2 && y1 == y2)
				ps.removeFirst();
		}
	}

	/**
	 * @author Maryam
	 * @function Merge intermediate points after moving lines
	 */
	public void mergePointsAfterMovingLine(LinkedList<Point2D> ps, int i1,
			int i2, Point2D start, Point2D end) {
		boolean merged = false;

		if (i1 == 0) {// first corner should be merged with start
			if ((int) ps.getFirst().getX() == (int) start.getX()
					&& (int) ps.getFirst().getY() == (int) start.getY()) {
				ps.removeFirst();
			}
		}
		if (i2 == ps.size() - 1) {// last corner should be merged with end
			if ((int) ps.getLast().getX() == (int) end.getX()
					&& (int) ps.getLast().getY() == (int) end.getY()) {
				ps.removeLast();
			}
		}

		int x1, y1, x2, y2;

		int j = 0;
		while (j < ps.size() - 1) {
			merged = false;
			x1 = (int) ps.get(j).getX();
			y1 = (int) ps.get(j).getY();
			for (int k = j + 1; k <= ps.size() - 1; k++) {
				x2 = (int) ps.get(k).getX();
				y2 = (int) ps.get(k).getY();
				if (x1 == x2 && y1 == y2) {
					for (int m = k; m > j - 1; m--) {
						ps.remove(m);
					}
					merged = true;
					break;
				}
			}
			if (merged)
				j = 0;
			else
				j++;
		}
	}

	/**
	 * @author Maryam
	 * @function After moving intermediate points
	 */
	public void mergePointsAfterMovingPoints(LinkedList<Point2D> ps, int i,
			Point2D start, Point2D end) {
		boolean merged = false;

		if (i == 0) {// first corner should be merged with start
			if ((int) ps.getFirst().getX() == (int) start.getX()
					&& (int) ps.getFirst().getY() == (int) start.getY()) {
				ps.removeFirst();
			}
		} else if (i == ps.size() - 1) {// last corner should be merged with end
			if ((int) ps.getLast().getX() == (int) end.getX()
					&& (int) ps.getLast().getY() == (int) end.getY()) {
				ps.removeLast();
			}
		}

		int x1, y1, x2, y2;

		int j = 0;
		while (j < ps.size() - 1) {
			merged = false;
			x1 = (int) ps.get(j).getX();
			y1 = (int) ps.get(j).getY();
			for (int k = j + 1; k <= ps.size() - 1; k++) {
				x2 = (int) ps.get(k).getX();
				y2 = (int) ps.get(k).getY();
				if (x1 == x2 && y1 == y2) {
					for (int m = k; m > j - 1; m--) {
						ps.remove(m);
					}
					merged = true;
					break;
				}
			}
			if (merged)
				j = 0;
			else
				j++;
		}
	}

	/**
	 * @param items
	 * @param dx
	 * @param dy
	 * @author sam @ if all component has been selected. move the whole diagram
	 *         without doing any change on the connectionLine or if the two
	 *         componentcanvasitem have been selected, the connection line
	 *         between them is automatically selected.
	 */
	public void move(CanvasItem[] items, int dx, int dy) {
		Canvas canvas = this;

		if (!canvas.isEditable()) {
			return;
		}
		if (items != null) {
			List<ConnectionItem> conItemList = new ArrayList<ConnectionItem>();
			for (CanvasItem item : items) {
				if (item == null) {
					break;
				}

				if (item instanceof ComponentCanvasItem) {
					Point2D.Double loc = item.getLocation();
					loc.x += dx;
					loc.y += dy;
					// loc.translate(dx, dy);
					item.setLocation(loc);

					ConnectorItem[] cis = ((ComponentCanvasItem) item)
							.getConnectorItems();
					if (cis != null && cis.length > 0)
						for (ConnectorItem ci : cis) {
							ConnectionItem[] conItems = canvas
									.getConnections(ci);
							if (conItems != null && conItems.length > 0) {
								for (ConnectionItem con : conItems) {
									ComponentCanvasItem srcComp = con.src
											.getComponentCanvasItem();
									ComponentCanvasItem destComp = con.dest
											.getComponentCanvasItem();

									Point2D start = new Point2D.Double(srcComp
											.getX()
											+ con.src.getX()
											+ con.src.getHitX(), srcComp.getY()
											+ con.src.getY()
											+ con.src.getHitY());

									Point2D end = new Point2D.Double(destComp
											.getX()
											+ con.dest.getX()
											+ con.dest.getHitX(), destComp
											.getY()
											+ con.dest.getY()
											+ con.dest.getHitY());

									if (isConnSelected(items, con)
											&& checkIfInSelection(items,
													srcComp, destComp)) {
										add2ConItemList(conItemList, con);
									} else {
										if (con.getSource().equals(ci)) {
											canvas
													.moveNextPointsAfterMovingItem(
															con
																	.getCornerPoints(),
															start, dx, dy);
											canvas.splitPointsAfterMovingItem(
													con, start, dx, dy);
											canvas.mergeLast(con
													.getCornerPoints(), start);
											canvas.setSelectedItem(null);
											// break;
										} else if (con.getDestination().equals(
												ci)) {
											canvas
													.movePrevPointsAfterMovingItem(
															con
																	.getCornerPoints(),
															end, dx, dy);
											canvas.splitPointsAfterMovingItem(
													con, end, dx, dy);
											canvas.mergeFirst(con
													.getCornerPoints(), end);
											canvas.setSelectedItem(null);
											// break;
										}
									}
								}
							}
						}
				}
			}

			// to avoid duplicate, move the connectionItem here
			for (ConnectionItem cItem : conItemList) {
				LinkedList<Point2D> corPts = (cItem).getCornerPoints();
				if (corPts != null && corPts.size() > 0) {
					for (Point2D p : corPts) {
						p.setLocation(p.getX() + dx, p.getY() + dy);
					}
				}
			}

			// moving intermediate points
			Point2D selected_corner = canvas.getSelectedCorner();
			if (canvas.getSelectedCorner() != null) {
				for (ConnectionItem ci : canvas.getConnections()) {
					if (ci.getCornerPoints().contains(selected_corner)) {
						canvas.updateIntermediatePoints(ci, canvas
								.getSelectedCorner(), dx, dy);
						// System.out.println("selected corner not null");
						canvas.setSelectedCorner(null);
					}
				}
			} else {
				// System.out.println("selected_corner = null");
			}

			// moving intermediate lines
			Line2D selectedLine = canvas.getSelectedLine();
			if (selectedLine != null) {
				Point2D p1 = selectedLine.getP1();
				Point2D p2 = selectedLine.getP2();
				for (ConnectionItem ci : canvas.getConnections()) {
					if (ci.getCornerPoints().contains(p1)
							&& ci.getCornerPoints().contains(p2)) {
						if (canvas.getSelectedLine() != null) {
							canvas.updateLine(ci, canvas.getSelectedLine(), dx,
									dy);
							// System.out.println("selected_line not null");
							canvas.setSelectedLine(null);
							break;
						} else {
							// System.out.println("selected_line = null");
						}

					}
				}
			}
		}
	}

	private void add2ConItemList(List<ConnectionItem> conItemList,
			ConnectionItem con) {
		boolean contains = false;
		for (ConnectionItem c : conItemList) {
			if (c.equals(con)) {
				contains = true;
				break;
			}
		}
		if (!contains)
			conItemList.add(con);
	}

	private boolean isConnSelected(CanvasItem[] items, ConnectionItem con) {
		if (items != null & items.length > 0) {
			for (CanvasItem item : items) {
				if (item instanceof ConnectionItem) {
					ConnectionItem conItem = (ConnectionItem) item;
					if (con.equals(conItem)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @author Maryam
	 * @func move a component
	 * 
	 * 
	 */
	public void move_old(CanvasItem[] items, int dx, int dy) {
		if (true) {
			move(items, dx, dy);
			return;
		}
		//
		Canvas canvas = this;

		if (!canvas.isEditable()) {
			return;
		}
		if (items != null) {
			List<ConnectionItem> conItemList = new ArrayList<ConnectionItem>();
			for (CanvasItem item : items) {
				if (item == null) {
					break;
				}

				if (item instanceof ComponentCanvasItem) {
					Point2D.Double loc = item.getLocation();
					loc.x += dx;
					loc.y += dy;
					// loc.translate(dx, dy);

					item.setLocation(loc);

					ConnectorItem[] cis = ((ComponentCanvasItem) item)
							.getConnectorItems();

					for (ConnectionItem con : canvas.getConnections()) {

						ComponentCanvasItem srcComp = con.src
								.getComponentCanvasItem();
						ComponentCanvasItem destComp = con.dest
								.getComponentCanvasItem();

						if (checkIfInSelection(items, srcComp, destComp)) {
							System.out.println("this is connection line is in"
									+ con.getSourceCCI().getName());
						}

						Point2D start = new Point2D.Double(srcComp.getX()
								+ con.src.getX() + con.src.getHitX(), srcComp
								.getY()
								+ con.src.getY() + con.src.getHitY());

						Point2D end = new Point2D.Double(destComp.getX()
								+ con.dest.getX() + con.dest.getHitX(),
								destComp.getY() + con.dest.getY()
										+ con.dest.getHitY());

						for (ConnectorItem ci : cis) {

							if (con.getSource().equals(ci)) {
								canvas.moveNextPointsAfterMovingItem(con
										.getCornerPoints(), start, dx, dy);
								canvas.splitPointsAfterMovingItem(con, start,
										dx, dy);
								canvas.mergeLast(con.getCornerPoints(), start);
								canvas.setSelectedItem(null);
								break;
							} else if (con.getDestination().equals(ci)) {
								canvas.movePrevPointsAfterMovingItem(con
										.getCornerPoints(), end, dx, dy);
								canvas.splitPointsAfterMovingItem(con, end, dx,
										dy);
								canvas.mergeFirst(con.getCornerPoints(), end);
								canvas.setSelectedItem(null);
								break;
							}
						}
					}
				}
			}

			// moving intermediate points
			Point2D selected_corner = canvas.getSelectedCorner();
			if (canvas.getSelectedCorner() != null) {
				for (ConnectionItem ci : canvas.getConnections()) {
					if (ci.getCornerPoints().contains(selected_corner)) {
						canvas.updateIntermediatePoints(ci, canvas
								.getSelectedCorner(), dx, dy);
						// System.out.println("selected corner not null");
						canvas.setSelectedCorner(null);
					}
				}
			} else {
				// System.out.println("selected_corner = null");
			}

			// moving intermediate lines
			Line2D selectedLine = canvas.getSelectedLine();
			if (selectedLine != null) {
				Point2D p1 = selectedLine.getP1();
				Point2D p2 = selectedLine.getP2();
				for (ConnectionItem ci : canvas.getConnections()) {
					if (ci.getCornerPoints().contains(p1)
							&& ci.getCornerPoints().contains(p2)) {
						if (canvas.getSelectedLine() != null) {
							canvas.updateLine(ci, canvas.getSelectedLine(), dx,
									dy);
							// System.out.println("selected_line not null");
							canvas.setSelectedLine(null);
							break;
						} else {
							// System.out.println("selected_line = null");
						}

					}
				}
			}
		}
	}

	private boolean checkIfInSelection(CanvasItem[] items,
			ComponentCanvasItem srcComp, ComponentCanvasItem destComp) {
		boolean srcIn = false, destIn = false;
		for (CanvasItem item : items) {
			if (item instanceof ComponentCanvasItem) {
				ComponentCanvasItem cci = (ComponentCanvasItem) item;
				if (srcComp.equals(cci)) {
					srcIn = true;
				} else if (destComp.equals(cci)) {
					destIn = true;
				}
				if (srcIn && destIn) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * @author Maryam
	 * @function Move lines which are not immediately connected to items
	 */
	public void moveLine(ConnectionItem con, Point2D p1, Point2D p2, int dx,
			int dy) {
		ComponentCanvasItem srcComp = con.src.getComponentCanvasItem();
		ComponentCanvasItem destComp = con.dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double(srcComp.getX() + con.src.getX()
				+ con.src.getHitX(), srcComp.getY() + con.src.getY()
				+ con.src.getHitY());

		Point2D end = new Point2D.Double(destComp.getX() + con.dest.getX()
				+ con.dest.getHitX(), destComp.getY() + con.dest.getY()
				+ con.dest.getHitY());

		int i1 = con.getCornerPoints().indexOf(p1);
		int i2 = con.getCornerPoints().indexOf(p2);

		if (i1 != -1 && i2 != -1) {
			Point2D temp1 = new Point2D.Double();
			temp1.setLocation(p1.getX() + dx, p1.getY() + dy);
			con.getCornerPoints().get(i1).setLocation(temp1);
			p1.setLocation(temp1);

			Point2D temp2 = new Point2D.Double();
			temp2.setLocation(p2.getX() + dx, p2.getY() + dy);
			con.getCornerPoints().get(i2).setLocation(temp2);
			p2.setLocation(temp2);

			if (i1 < i2) {
				mergePointsAfterMovingLine(con.getCornerPoints(), i1, i2,
						start, end);
			} else if (i1 > i2) {
				mergePointsAfterMovingLine(con.getCornerPoints(), i2, i1,
						start, end);
			}
		} else
			return;
	}

	/**
	 * @author Maryam
	 * @func Move next intermediate points after moving an item
	 */
	public void moveNextPointsAfterMovingItem(LinkedList<Point2D> ps,
			Point2D start, int dx, int dy) {
		int i = 0;

		if (ps.size() > 0) {
			if (dx != 0
					&& (int) (start.getX()) == (int) (ps.get(i).getX() + dx)) {
				ps.get(i).setLocation(
						new Point2D.Double(start.getX(), ps.get(i).getY()));// change
				// getX()
			}
			if (dy != 0
					&& (int) (start.getY()) == (int) (ps.get(i).getY() + dy)) {
				ps.get(i).setLocation(
						new Point2D.Double(ps.get(i).getX(), start.getY()));// change
				// y
			}
			if (ps.size() > 1
					&& (int) (ps.get(i).getX()) == (int) (ps.get(i + 1).getX())
					&& (int) (ps.get(i).getY()) == (int) (ps.get(i + 1).getY())) {
				ps.removeFirst();
				ps.removeFirst();
			}
		}
	}

	/**
	 * @author Maryam
	 * @function Move next intermediate points after moving intermediate points
	 *           *
	 */
	public void moveNextPointsAfterMovingPoints(LinkedList<Point2D> ps, int i,
			double dx, double dy) {

		if (i < ps.size() - 1) {
			if (dx != 0
					&& (int) (ps.get(i + 1).getX()) == (int) (ps.get(i).getX() - dx)) {
				ps.get(i + 1).setLocation(
						new Point2D.Double(ps.get(i).getX(), ps.get(i + 1)
								.getY()));
			}
			if (dy != 0
					&& (int) (ps.get(i + 1).getY()) == (int) (ps.get(i).getY() - dy)) {
				ps.get(i + 1).setLocation(
						new Point2D.Double(ps.get(i + 1).getX(), ps.get(i)
								.getY()));// change y
			}
		} else
			return;
		moveNextPointsAfterMovingPoints(ps, i + 1, dx, dy);
	}

	/**
	 * @author Maryam
	 * @func Move prev intermediate points after moving an item
	 */
	public void movePrevPointsAfterMovingItem(LinkedList<Point2D> ps,
			Point2D end, int dx, int dy) {
		int i = ps.size() - 1;

		if (ps.size() > 0) {
			if (dx != 0 && (int) (end.getX()) == (int) (ps.get(i).getX() + dx)) {
				ps.get(i).setLocation(
						new Point2D.Double(end.getX(), ps.get(i).getY()));// change
				// getX()
			}
			if (dy != 0 && (int) (end.getY()) == (int) (ps.get(i).getY() + dy)) {
				ps.get(i).setLocation(
						new Point2D.Double(ps.get(i).getX(), end.getY()));// change
				// y
			}
			if (ps.size() > 1
					&& ((int) (ps.get(i).getX()) == (int) (ps.get(i - 1).getX()) && (int) (ps
							.get(i).getY()) == (int) (ps.get(i - 1).getY()))) {
				ps.removeLast();
				ps.removeLast();
			}
		}
	}

	/**
	 * @author Maryam
	 * @function Move prev intermediate points after moving intermediate points
	 *           *
	 */
	public void movePrevPointsAfterMovingPoints(LinkedList<Point2D> ps, int i,
			double dx, double dy) {
		if (i > 0) {
			if (dx != 0
					&& (int) (ps.get(i - 1).getX()) == (int) (ps.get(i).getX() - dx)) {
				ps.get(i - 1).setLocation(
						new Point2D.Double(ps.get(i).getX(), ps.get(i - 1)
								.getY()));
			}
			if (dy != 0
					&& (int) (ps.get(i - 1).getY()) == (int) (ps.get(i).getY() - dy)) {
				ps.get(i - 1).setLocation(
						new Point2D.Double(ps.get(i - 1).getX(), ps.get(i)
								.getY()));// change y
			}
		} else
			return;
		movePrevPointsAfterMovingPoints(ps, i - 1, dx, dy);
	}

	protected void paintCanvasItems(Graphics2D g2) {

		for (CanvasItem c : getCanvasItems()) {
			c.paint(g2);
		}

		if (currModeHandler.canPaint()) {
			currModeHandler.paint(g2);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see proteus.gwt.client.app.ui.canvas.action.CanvasItemActions#refresh()
	 */
	@Override
	public void refresh() {
		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
	}

	public void remove(CanvasItem item) {
		model.remove(item);
		// toolTipMgr.unregister(item);
		item.notifyRemove(this);
		if (item.getName() != null) {
			String info = "The item, " + item.getName() + ", has been removed.";
			ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
					Message.INFO, info));
		}
	}

	public void remove(CanvasItem[] items) {
		model.remove(items);
		String info;
		for (CanvasItem item : items) {
			// toolTipMgr.unregister(item);
			item.notifyRemove(this);
			if (item.getName() != null) {
				info = "The item, " + item.getName() + ", has been removed.";
				ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
						Message.INFO, info));
			}
		}

		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
	}

	protected void drawGridLine(Graphics2D g2) {
		Dimension canvasSize = Constant.diagramCanvasSize;
		g2.setColor(Color.lightGray);
		g2.drawLine(0, canvasSize.height / 2, canvasSize.width,
				canvasSize.height / 2);
		g2.drawLine(canvasSize.width / 2, 0, canvasSize.width / 2,
				canvasSize.height);
		g2.drawLine(canvasSize.width, 0, canvasSize.width, canvasSize.height);
	}

	/*
	 * -----These are the functions to update intermediate line and points.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ConnectionActions#remove(proteus
	 * .gwt.client.app.ui.canvas.ConnectionItem[])
	 */
	@Override
	public void remove(ConnectionItem[] items) {
		for (int i = 0; i < items.length; ++i) {
			removeConnection(items[i]);
		}
	}

	public void removeConnection(ConnectionItem connection) {
		Equation equation = connectMap.get(connection);
		if (equation != null) {
			classDef.removeEquation(equation);
		}
		connectMap.remove(connection);
		String info = "The connection between "
				+ connection.getSource().getName() + " and "
				+ connection.getDestination().getName() + " has been removed.";
		ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(Message.INFO,
				info));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ConnectionActions#removeControlPoint
	 * (proteus.gwt.client.app.ui.canvas.ConnectionItem,
	 * proteus.gwt.client.graphics.geom.Point2D)
	 */
	@Override
	public void removeControlPoint(ConnectionItem item, Point2D point) {
		// TODO not implemented yet

	}

	public void repaint() {
	}

	/**
	 * @author Maryam
	 * @function resize items; works in 4 direction of NW, NE, SW, SE
	 */
	public void resize(CanvasItem[] items, int resize, int dx, int dy) {

		// TODO: FIX ME connector items are not correctly resized when undo
		boolean minimize = false;

		Canvas canvas = this;
		Rectangle r = new Rectangle();

		if (items != null) {
			for (CanvasItem item : items) {
				if (item == null) {
					break;
				}
				item.getNormBounds(r);

				int rw = dx;
				int rh = dy;

				ComponentCanvasItem cci = (ComponentCanvasItem) item;
				ConnectorItem[] cis = cci.getConnectorItems();
				ConnectionItem[] cs = canvas.getConnections();

				ComponentCanvasItem[] srcComp = new ComponentCanvasItem[cis.length];
				ComponentCanvasItem[] destComp = new ComponentCanvasItem[cis.length];

				Point2D[][] fp = null;// first intermediate point
				Point2D[][] lp = null;// last intermediate point
				Point2D[][] start = null;// start point of connection
				Point2D[][] end = null;// end point of connection

				boolean[][] shorizontal = new boolean[MAX_CONNECTION][MAX_CONNECTION];// start_horizontal:
				// when
				// first
				// intermediate
				// point
				// and
				// start
				// are
				// horizontally
				// connected
				boolean[][] svertical = new boolean[MAX_CONNECTION][MAX_CONNECTION];// start_vertical:
				// when
				// first
				// intermediate
				// point
				// and
				// start
				// are
				// vertically
				// connected
				boolean[][] ehorizontal = new boolean[MAX_CONNECTION][MAX_CONNECTION];// end_horizontal:
				// when
				// last
				// intermediate
				// point
				// and
				// end
				// are
				// horizontally
				// connected
				boolean[][] evertical = new boolean[MAX_CONNECTION][MAX_CONNECTION];// end_vertical:
				// when
				// last
				// intermediate
				// point
				// and
				// end
				// are
				// vertically
				// connected

				if (cs != null) {
					fp = new Point2D[cis.length][cs.length];// first
					// intermediate
					// point
					lp = new Point2D[cis.length][cs.length];// last intermediate
					// point
					start = new Point2D[cis.length][cs.length];// start point of
					// connection
					end = new Point2D[cis.length][cs.length];// end point of
					// connection

					shorizontal = new boolean[cis.length][cs.length];// start_horizontal:
					// when
					// first
					// intermediate
					// point
					// and
					// start
					// are
					// horizontally
					// connected
					svertical = new boolean[cis.length][cs.length];// start_vertical:
					// when
					// first
					// intermediate
					// point and
					// start are
					// vertically
					// connected
					ehorizontal = new boolean[cis.length][cs.length];// end_horizontal:
					// when
					// last
					// intermediate
					// point
					// and
					// end
					// are
					// horizontally
					// connected
					evertical = new boolean[cis.length][cs.length];// end_vertical:
					// when last
					// intermediate
					// point and
					// end are
					// vertically
					// connected
				}

				switch (resize) {
				case SelectionManager.NW:
					// System.out.println("-----NW----");

					minimize = (rw > 0 && rh > 0) ? true : false;

					// initial part before update
					for (int i = 0; i < cis.length; i++) {
						ConnectionItem[] conns = canvas.getConnections(cis[i]);

						if (conns != null) {
							for (int j = 0; j < conns.length; j++) {

								if (conns[j].getSource().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									fp[i][j] = conns[j].getCornerPoints()
											.getFirst();
								}

								if (conns[j].getDestination().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									lp[i][j] = conns[j].getCornerPoints()
											.getLast();
								}

								srcComp[i] = conns[j].src
										.getComponentCanvasItem();
								destComp[i] = conns[j].dest
										.getComponentCanvasItem();

								start[i][j] = new Point2D.Double(srcComp[i]
										.getX()
										+ conns[j].src.getX()
										+ conns[j].src.getHitX(), srcComp[i]
										.getY()
										+ conns[j].src.getY()
										+ conns[j].src.getHitY());

								end[i][j] = new Point2D.Double(destComp[i]
										.getX()
										+ conns[j].dest.getX()
										+ conns[j].dest.getHitX(), destComp[i]
										.getY()
										+ conns[j].dest.getY()
										+ conns[j].dest.getHitY());

								if (fp[i][j] != null) {
									if ((int) start[i][j].getX() == (int) fp[i][j]
											.getX())
										svertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) start[i][j].getY() == (int) fp[i][j]
											.getY())
											&& !svertical[i][j])
										shorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()

													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										svertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !svertical[i][j])
										shorizontal[i][j] = true;
								}

								if (lp[i][j] != null) {
									if ((int) end[i][j].getX() == (int) lp[i][j]
											.getX())
										evertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) end[i][j].getY() == (int) lp[i][j]
											.getY())
											&& !evertical[i][j])
										ehorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()
													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										evertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !evertical[i][j])
										ehorizontal[i][j] = true;
								}
							}
						}
					}

					is_resized = true;
					// update items bounds
					item.setBounds(r.x + rw, r.y + rh, r.width - rw, r.height
							- rh);

					// updating intermediate points
					updateConnector_IntermediatePointsAfterResize(cis, srcComp,
							destComp, shorizontal, svertical, ehorizontal,
							evertical, fp, lp, rw, rh, r.width, r.height,
							minimize);
					is_resized = false;

					break;

				case SelectionManager.NE:
					// System.out.println("-----NE----");

					minimize = (rw < 0 && rh > 0) ? true : false;

					// initial part before update
					for (int i = 0; i < cis.length; i++) {
						ConnectionItem[] conns = canvas.getConnections(cis[i]);

						if (conns != null) {
							for (int j = 0; j < conns.length; j++) {

								if (conns[j].getSource().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									fp[i][j] = conns[j].getCornerPoints()
											.getFirst();
								}

								if (conns[j].getDestination().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									lp[i][j] = conns[j].getCornerPoints()
											.getLast();
								}

								srcComp[i] = conns[j].src
										.getComponentCanvasItem();
								destComp[i] = conns[j].dest
										.getComponentCanvasItem();

								start[i][j] = new Point2D.Double(srcComp[i]
										.getX()
										+ conns[j].src.getX()
										+ conns[j].src.getHitX(), srcComp[i]
										.getY()
										+ conns[j].src.getY()
										+ conns[j].src.getHitY());

								end[i][j] = new Point2D.Double(destComp[i]
										.getX()
										+ conns[j].dest.getX()
										+ conns[j].dest.getHitX(), destComp[i]
										.getY()
										+ conns[j].dest.getY()
										+ conns[j].dest.getHitY());

								if (fp[i][j] != null) {
									if ((int) start[i][j].getX() == (int) fp[i][j]
											.getX())
										svertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) start[i][j].getY() == (int) fp[i][j]
											.getY())
											&& !svertical[i][j])
										shorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()

													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										svertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !svertical[i][j])
										shorizontal[i][j] = true;
								}

								if (lp[i][j] != null) {
									if ((int) end[i][j].getX() == (int) lp[i][j]
											.getX())
										evertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) end[i][j].getY() == (int) lp[i][j]
											.getY())
											&& !evertical[i][j])
										ehorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()
													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										evertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !evertical[i][j])
										ehorizontal[i][j] = true;
								}
							}
						}
					}

					is_resized = true;
					// update item bounds
					item.setBounds(r.x, r.y + rh, r.width + rw, r.height - rh);

					// updating intermediate points
					updateConnector_IntermediatePointsAfterResize(cis, srcComp,
							destComp, shorizontal, svertical, ehorizontal,
							evertical, fp, lp, rw, rh, r.width, r.height,
							minimize);
					is_resized = false;

					break;
				case SelectionManager.SW:
					// System.out.println("-----SW----");

					minimize = (rw > 0 && rh < 0) ? true : false;

					// initial part before update
					for (int i = 0; i < cis.length; i++) {
						ConnectionItem[] conns = canvas.getConnections(cis[i]);

						if (conns != null) {
							for (int j = 0; j < conns.length; j++) {

								if (conns[j].getSource().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									fp[i][j] = conns[j].getCornerPoints()
											.getFirst();
								}

								if (conns[j].getDestination().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									lp[i][j] = conns[j].getCornerPoints()
											.getLast();
								}

								srcComp[i] = conns[j].src
										.getComponentCanvasItem();
								destComp[i] = conns[j].dest
										.getComponentCanvasItem();

								start[i][j] = new Point2D.Double(srcComp[i]
										.getX()
										+ conns[j].src.getX()
										+ conns[j].src.getHitX(), srcComp[i]
										.getY()
										+ conns[j].src.getY()
										+ conns[j].src.getHitY());

								end[i][j] = new Point2D.Double(destComp[i]
										.getX()
										+ conns[j].dest.getX()
										+ conns[j].dest.getHitX(), destComp[i]
										.getY()
										+ conns[j].dest.getY()
										+ conns[j].dest.getHitY());

								if (fp[i][j] != null) {
									if ((int) start[i][j].getX() == (int) fp[i][j]
											.getX())
										svertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) start[i][j].getY() == (int) fp[i][j]
											.getY())
											&& !svertical[i][j])
										shorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()

													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										svertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !svertical[i][j])
										shorizontal[i][j] = true;
								}

								if (lp[i][j] != null) {
									if ((int) end[i][j].getX() == (int) lp[i][j]
											.getX())
										evertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) end[i][j].getY() == (int) lp[i][j]
											.getY())
											&& !evertical[i][j])
										ehorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()
													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										evertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !evertical[i][j])
										ehorizontal[i][j] = true;
								}
							}
						}
					}

					is_resized = true;
					// update item bounds
					item.setBounds(r.x + rw, r.y, r.width - rw, r.height + rh);

					// updating intermediate points
					updateConnector_IntermediatePointsAfterResize(cis, srcComp,
							destComp, shorizontal, svertical, ehorizontal,
							evertical, fp, lp, rw, rh, r.width, r.height,
							minimize);
					is_resized = false;

					break;
				case SelectionManager.SE:
					// System.out.println("-----SE----");

					minimize = (rw < 0 && rh < 0) ? true : false;

					// initial part before update
					for (int i = 0; i < cis.length; i++) {
						ConnectionItem[] conns = canvas.getConnections(cis[i]);

						if (conns != null) {
							for (int j = 0; j < conns.length; j++) {
								if (conns[j].getSource().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									fp[i][j] = conns[j].getCornerPoints()
											.getFirst();
								}

								if (conns[j].getDestination().equals(cis[i])
										&& conns[j].getCornerPoints().size() != 0) {
									lp[i][j] = conns[j].getCornerPoints()
											.getLast();
								}

								srcComp[i] = conns[j].src
										.getComponentCanvasItem();
								destComp[i] = conns[j].dest
										.getComponentCanvasItem();

								start[i][j] = new Point2D.Double(srcComp[i]
										.getX()
										+ conns[j].src.getX()
										+ conns[j].src.getHitX(), srcComp[i]
										.getY()
										+ conns[j].src.getY()
										+ conns[j].src.getHitY());

								end[i][j] = new Point2D.Double(destComp[i]
										.getX()
										+ conns[j].dest.getX()
										+ conns[j].dest.getHitX(), destComp[i]
										.getY()
										+ conns[j].dest.getY()
										+ conns[j].dest.getHitY());

								if (fp[i][j] != null) {
									if ((int) start[i][j].getX() == (int) fp[i][j]
											.getX())
										svertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) start[i][j].getY() == (int) fp[i][j]
											.getY())
											&& !svertical[i][j])
										shorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()

													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										svertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !svertical[i][j])
										shorizontal[i][j] = true;
								}

								if (lp[i][j] != null) {
									if ((int) end[i][j].getX() == (int) lp[i][j]
											.getX())
										evertical[i][j] = true;// start and
									// first
									// intermediate
									// points are
									// connected
									// vertically
									// before update
									else if (((int) end[i][j].getY() == (int) lp[i][j]
											.getY())
											&& !evertical[i][j])
										ehorizontal[i][j] = true;// start and
									// first
									// intermediate
									// points
									// are
									// connected
									// horizontally
									// before
									// update
								} else {
									Point2D start_point = new Point2D.Double(
											srcComp[i].getX()
													+ conns[j].src.getX()
													+ conns[j].src.getHitX(),
											srcComp[i].getY()
													+ conns[j].src.getY()
													+ conns[j].src.getHitY());
									Point2D end_point = new Point2D.Double(
											destComp[i].getX()
													+ conns[j].dest.getX()
													+ conns[j].dest.getHitX(),
											destComp[i].getY()
													+ conns[j].dest.getY()
													+ conns[j].dest.getHitY());
									if ((int) start_point.getX() == (int) end_point
											.getX())
										evertical[i][j] = true;
									else if ((int) start_point.getY() == (int) end_point
											.getY()
											&& !evertical[i][j])
										ehorizontal[i][j] = true;
								}
							}
						}
					}

					is_resized = true;
					// update item bounds
					item.setBounds(r.x, r.y, r.width + rw, r.height + rh);
					// updating intermediate points
					updateConnector_IntermediatePointsAfterResize(cis, srcComp,
							destComp, shorizontal, svertical, ehorizontal,
							evertical, fp, lp, rw, rh, r.width, r.height,
							minimize);
					is_resized = false;

					break;
				case SelectionManager.N:
					// System.out.println("-----N----");
					item.setBounds(r.x, r.y + rh, r.width, r.height - rh);
					break;
				case SelectionManager.E:
					// System.out.println("-----E----");
					item.setBounds(r.x, r.y, r.width + rw, r.height);
					break;
				case SelectionManager.S:
					// System.out.println("-----S----");
					item.setBounds(r.x, r.y, r.width, r.height + rh);
					break;
				case SelectionManager.W:
					// System.out.println("-----W----");
					item.setBounds(r.x + rw, r.y, r.width - rw, r.height);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.canvas.action.ComponentActions#rotate(proteus
	 * .gwt.client.app.ui.canvas.CanvasItem[], boolean)
	 */
	@Override
	public void rotate(CanvasItem[] items, boolean clockwise) {
		if (clockwise) {
			CanvasUtils.rotateClockwise(items);
		} else {
			CanvasUtils.rotateAntiClockwise(items);
		}
		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
	}

	public void setCanvasItems(CanvasItem[] list) {
		model.setCanvasItems(list);
		// repaint();
	}

	public void setCanvasSize(Dimension d) {
		this.size = d;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setMode(int mode, Object... args) {
		if (mode >= 0 && mode < modeHandlers.length) {
			currModeHandler.finish();
			currModeHandler = modeHandlers[mode];
			currModeHandler.init(args);
		}
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(CanvasModel model) {
		this.model = model;
	}

	public void setScale(int scale) {

		this.canvasScale = SCALE_LEVELS[scale];
	}

	public void setSelectedCorner(Point2D p) {
		selected_corner = p;
	}

	public void setSelectedItem(ComponentCanvasItem cci) {
		selected_item = cci;
	}

	public void setSelectedItems(CanvasItem[] selectedItems) {
		model.setSelectedItems(selectedItems);
	}

	public void setSelectedLine(Line2D l) {
		selected_line = l;
	}

	// connector can have is set to 20

	/**
	 * @author Maryam
	 * @function Splitting the intermediate points after moving items. It only
	 *           happens when there is a straight line between src and dest with
	 *           no intermediate points
	 */
	public void splitPointsAfterMovingItem(ConnectionItem con, Point2D p,
			int dx, int dy) {

		LinkedList<Point2D> ps = con.getCornerPoints();

		ComponentCanvasItem cci_src = con.getSource().getComponentCanvasItem();
		ComponentCanvasItem cci_dest = con.getDestination()
				.getComponentCanvasItem();
		ConnectorItem src, dest;
		src = con.getSource();
		dest = con.getDestination();

		Point2D start = new Point2D.Double(cci_src.getX() + src.getX()
				+ src.getHitX(), cci_src.getY() + src.getY() + src.getHitY());
		Point2D end = new Point2D.Double(cci_dest.getX() + dest.getX()
				+ dest.getHitX(), cci_dest.getY() + dest.getY()
				+ dest.getHitY());

		if (ps.size() == 0) {
			if (dx != 0) {
				if (p.equals(start)
						&& (int) (start.getX() - dx) == (int) (end.getX())) {
					ps.add(new Point2D.Double(start.getX(), (int) (Math
							.abs(start.getY() + end.getY()) / 2)));
					ps.add(new Point2D.Double(end.getX(), (int) (Math.abs(start
							.getY()
							+ end.getY()) / 2)));
				} else if (p.equals(end)
						&& (int) start.getX() == (int) (end.getX() - dx)) {
					ps.add(new Point2D.Double(start.getX(), (int) (Math
							.abs(start.getY() + end.getY()) / 2)));
					ps.add(new Point2D.Double(end.getX(), (int) (Math.abs(start
							.getY()
							+ end.getY()) / 2)));
				}
			}

			/* else */if (dy != 0) {

				if (p.equals(start)
						&& (int) (start.getY() - dy) == (int) end.getY()) {
					ps.add(new Point2D.Double((int) (Math.abs(start.getX()
							+ end.getX()) / 2), start.getY()));
					ps.add(new Point2D.Double((int) (Math.abs(start.getX()
							+ end.getX()) / 2), end.getY()));
				} else if (p.equals(end)
						&& (int) start.getY() == (int) (end.getY() - dy)) {
					ps.add(new Point2D.Double((int) (Math.abs(start.getX()
							+ end.getX()) / 2), start.getY()));
					ps.add(new Point2D.Double((int) (Math.abs(start.getX()
							+ end.getX()) / 2), end.getY()));
				}
			}
		}
	}

	/**
	 * @author Maryam
	 * @function After moving intermediate points. It happens when either the
	 *           first or last intermediate points which are immediately
	 *           connected to the src or dest are moved
	 */
	public int splitPointsAfterMovingPoints(ConnectionItem con, int i, int dx,
			int dy) {

		LinkedList<Point2D> ps = con.getCornerPoints();

		ComponentCanvasItem cci_src = con.getSource().getComponentCanvasItem();
		ComponentCanvasItem cci_dest = con.getDestination()
				.getComponentCanvasItem();
		ConnectorItem src, dest;
		src = con.getSource();
		dest = con.getDestination();

		Point2D start = new Point2D.Double(cci_src.getX() + src.getX()
				+ src.getHitX(), cci_src.getY() + src.getY() + src.getHitY());
		Point2D end = new Point2D.Double(cci_dest.getX() + dest.getX()
				+ dest.getHitX(), cci_dest.getY() + dest.getY()
				+ dest.getHitY());

		if (dx != 0 && i == 0) {// change occurred direction_x && this is the
			// first corner point, connecting to the src

			if ((int) start.getY() == (int) ps.get(i).getY()) {
				// source does not move
			} else if ((int) (start.getX() + dx) == (int) (ps.get(i).getX())) {
				ps.addFirst(new Point2D.Double(ps.get(i).getX(), (int) (Math
						.abs(start.getY() + ps.get(i).getY()) / 2)));
				i++;
				ps.addFirst(new Point2D.Double(start.getX(), (int) (Math
						.abs(start.getY() + ps.get(i).getY()) / 2)));
				i++;
				// canvas.setSelectedCorner(con.getCornerPoints().get(i));//TODO
				// set to nothing
			}
		} else if (dx != 0 && i == ps.size() - 1) {// change in direction_x &&
			// this is the last corner
			// point, connecting to the
			// dest

			if ((int) (end.getY()) == (int) (ps.get(i).getY())) {
				// source does not move
			} else if ((int) (end.getX() + dx) == (int) (ps.get(i).getX())) {
				// split to two points whose getX() are between the two points,
				// one with getY() equal to source, the other with getY() equal
				// to getY()+dy
				ps.addLast(new Point2D.Double(ps.get(i).getX(), (int) (Math
						.abs(end.getY() + ps.get(i).getY()) / 2)));
				ps.addLast(new Point2D.Double(end.getX(), (int) (Math.abs(end
						.getY()
						+ ps.get(i).getY()) / 2)));
			}
		}
		if (dy != 0 && i == 0) {// change in direction_y && this is the first
			// corner point, connecting to the src

			if ((int) (start.getX()) == (int) (ps.get(i).getX())) {
				// source does not move
			} else if ((int) (start.getY() + dy) == (int) (ps.get(i).getY())) {
				// split to two points whose getX() are between the two points,
				// one with getY() equal to source, the other with getY() equal
				// to getY()+dy
				ps.addFirst(new Point2D.Double((int) (Math.abs(start.getX()
						+ ps.get(i).getX()) / 2), ps.get(i).getY()));
				i++;
				ps.addFirst(new Point2D.Double((int) (Math.abs(start.getX()
						+ ps.get(i).getX()) / 2), start.getY()));
				i++;
			}
		} else if (dy != 0 && i == ps.size() - 1) {// change in direction_y &&
			// this is the last corner
			// point, connecting to the
			// src

			if ((int) (end.getX()) == (int) (ps.get(i).getX())) {
				// source does not move
			} else if ((int) (end.getY() + dy) == (int) (ps.get(i).getY())) {
				// split to two points whose getX() are between the two points,
				// one with getY() equal to dest, the other with getY() equal to
				// getY()+dy
				ps.addLast(new Point2D.Double((int) (Math.abs(end.getX()
						+ ps.get(i).getX()) / 2), ps.get(i).getY()));
				ps.addLast(new Point2D.Double((int) (Math.abs(end.getX()
						+ ps.get(i).getX()) / 2), end.getY()));
			}
		}
		return i;
	}

	public void startConnection(ConnectorItem connItem) {
		_mode = getMode();
		setMode(connMgrMode);
		src = connItem;
		// repaint();
	}

	/**
	 * @author Maryam
	 * @function updates connectoritem positions as well as intermediate points'
	 *           position after resize
	 */
	private void updateConnector_IntermediatePointsAfterResize(
			ConnectorItem[] cis, ComponentCanvasItem[] srcComp,
			ComponentCanvasItem[] destComp, boolean[][] shorizontal,
			boolean[][] svertical, boolean[][] ehorizontal,
			boolean[][] evertical, Point2D[][] fp, Point2D[][] lp, int rw,
			int rh, int w, int h, boolean minimize) {
		Canvas canvas = this;
		// System.out.println("---updateIntermediatePointsAfterResize----");

		ConnectionItem[] cs = canvas.getConnections();
		Point2D[][] start = null;
		Point2D[][] end = null;

		if (cs != null) {
			start = new Point2D[cis.length][cs.length];
			end = new Point2D[cis.length][cs.length];
		}

		for (int i = 0; i < cis.length; i++) {
			ConnectionItem[] conns = canvas.getConnections(cis[i]);

			// System.out.println("type: " + cis[i].connector.name);
			// System.out.println("rotateion con: " + cis[0].getState());

			// cis[i] bounds
			double cw = cis[i].getWidth();
			double ch = cis[i].getHeight();
			double x = cis[i].getX();
			double y = cis[i].getY();

			// System.out.println("----before----");
			// System.out.println("x, y, w, h: " + x + ", " + y + ", " + cw +
			// ", " + ch);
			// System.out.println("rw, rh: " + rw + ", " + rh);
			//			 
			if (!minimize) {// enlarge
				x = x * (w + Math.abs(rw)) / w;
				y = y * (h + Math.abs(rh)) / h;
				cw = cw * (w + Math.abs(rw)) / w;
				ch = ch * (h + Math.abs(rh)) / h;
				// x = (rw<0) ? x + rw:x;
				// y = (rh<0) ? y + rh:y;

			} else {
				x = x * (w - Math.abs(rw)) / w;
				y = y * (h - Math.abs(rh)) / h;
				cw = cw * (w - Math.abs(rw)) / w;
				ch = ch * (h - Math.abs(rh)) / h;
				// x = (rw>0) ? x + rw:x;
				// y = (rh>0) ? y + rh:y;
			}
			//			
			// System.out.println("-----after-----");
			// System.out.println("x, y, w, h: " + x + ", " + y + ", " + cw +
			// ", " + ch);
			// System.out.println("rw, rh: " + rw + ", " + rh);

			cis[i].setBounds((int) x, (int) y, (int) cw, (int) ch);
			cis[i].set_bounds(new Rectangle((int) x, (int) y, (int) cw,
					(int) ch));

			if (conns != null) {
				for (int j = 0; j < conns.length; j++) {
					if (conns[j].getSource().equals(cis[i])) {

						start[i][j] = new Point2D.Double(srcComp[i].getX()
								+ conns[j].src.getX() + conns[j].src.getHitX(),
								srcComp[i].getY() + conns[j].src.getY()
										+ conns[j].src.getHitY());

						if (fp[i][j] != null) {
							if (shorizontal[i][j]) {
								fp[i][j].setLocation(fp[i][j].getX(),
										start[i][j].getY());
								shorizontal[i][j] = false;
							} else if (svertical[i][j]) {
								fp[i][j].setLocation(start[i][j].getX(),
										fp[i][j].getY());
								svertical[i][j] = false;
							}
						} else {
							Point2D start_point = new Point2D.Double(srcComp[i]
									.getX()
									+ conns[j].src.getX()
									+ conns[j].src.getHitX(), srcComp[i].getY()
									+ conns[j].src.getY()
									+ conns[j].src.getHitY());

							Point2D end_point = new Point2D.Double(destComp[i]
									.getX()
									+ conns[j].dest.getX()
									+ conns[j].dest.getHitX(), destComp[i]
									.getY()
									+ conns[j].dest.getY()
									+ conns[j].dest.getHitY());

							if (shorizontal[i][j]) {
								Point2D p1 = new Point2D.Double(
										(int) ((start_point.getX() + end_point
												.getX()) / 2),
										(int) start_point.getY());
								Point2D p2 = new Point2D.Double(
										(int) ((start_point.getX() + end_point
												.getX()) / 2), (int) end_point
												.getY());
								conns[j].getCornerPoints().add(p1);
								conns[j].getCornerPoints().add(p2);
								shorizontal[i][j] = false;
							} else if (svertical[i][j]) {
								Point2D p1 = new Point2D.Double(
										(int) start_point.getX(),
										(int) ((start_point.getY() + end_point
												.getY()) / 2));
								Point2D p2 = new Point2D.Double((int) end_point
										.getX(),
										(int) ((start_point.getY() + end_point
												.getY()) / 2));
								conns[j].getCornerPoints().add(p1);
								conns[j].getCornerPoints().add(p2);
								svertical[i][j] = false;
							}
						}
					} else if (conns[j].getDestination().equals(cis[i])) {

						end[i][j] = new Point2D.Double(destComp[i].getX()
								+ conns[j].dest.getX()
								+ conns[j].dest.getHitX(), destComp[i].getY()
								+ conns[j].dest.getY()
								+ conns[j].dest.getHitY());

						if (lp[i][j] != null) {
							if (ehorizontal[i][j]) {
								lp[i][j].setLocation(lp[i][j].getX(), end[i][j]
										.getY());
								ehorizontal[i][j] = false;
							} else if (evertical[i][j]) {
								lp[i][j].setLocation(end[i][j].getX(), lp[i][j]
										.getY());
								evertical[i][j] = false;
							}
						} else {
							Point2D start_point = new Point2D.Double(srcComp[i]
									.getX()
									+ conns[j].src.getX()
									+ conns[j].src.getHitX(), srcComp[i].getY()
									+ conns[j].src.getY()
									+ conns[j].src.getHitY());

							Point2D end_point = new Point2D.Double(destComp[i]
									.getX()
									+ conns[j].dest.getX()
									+ conns[j].dest.getHitX(), destComp[i]
									.getY()
									+ conns[j].dest.getY()
									+ conns[j].dest.getHitY());

							if (ehorizontal[i][j]) {
								Point2D p1 = new Point2D.Double(
										(int) ((start_point.getX() + end_point
												.getX()) / 2),
										(int) start_point.getY());
								Point2D p2 = new Point2D.Double(
										(int) ((start_point.getX() + end_point
												.getX()) / 2), (int) end_point
												.getY());
								conns[j].getCornerPoints().add(p1);
								conns[j].getCornerPoints().add(p2);
								ehorizontal[i][j] = false;
							} else if (evertical[i][j]) {
								Point2D p1 = new Point2D.Double(
										(int) start_point.getX(),
										(int) ((start_point.getY() + end_point
												.getY()) / 2));
								Point2D p2 = new Point2D.Double((int) end_point
										.getX(),
										(int) ((start_point.getY() + end_point
												.getY()) / 2));
								conns[j].getCornerPoints().add(p1);
								conns[j].getCornerPoints().add(p2);
								evertical[i][j] = false;
							}
						}
					}// end else if (dest)
				}// end for (j)
			}// end if (conns != null)
		}
	}

	/**
	 * @author Maryam
	 * @function
	 */
	public Point2D updateIntermediatePoints(ConnectionItem con,
			Point2D selected_corner, int dx, int dy) {

		ComponentCanvasItem srcComp = con.src.getComponentCanvasItem();
		ComponentCanvasItem destComp = con.dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double(srcComp.getX() + con.src.getX()
				+ con.src.getHitX(), srcComp.getY() + con.src.getY()
				+ con.src.getHitY());

		Point2D end = new Point2D.Double(destComp.getX() + con.dest.getX()
				+ con.dest.getHitX(), destComp.getY() + con.dest.getY()
				+ con.dest.getHitY());

		int i = con.getCornerPoints().indexOf(selected_corner);

		if (i != -1) {

			Point2D temp = new Point2D.Double();
			temp.setLocation(selected_corner.getX() + dx, selected_corner
					.getY()
					+ dy);

			con.getCornerPoints().get(i).setLocation(temp);
			selected_corner.setLocation(temp);

			moveNextPointsAfterMovingPoints(con.getCornerPoints(), i, dx, dy);
			movePrevPointsAfterMovingPoints(con.getCornerPoints(), i, dx, dy);

			i = splitPointsAfterMovingPoints(con, i, dx, dy);
			mergePointsAfterMovingPoints(con.getCornerPoints(), i, start, end);

		} else {
			return null;// does not contain such corner point
		}

		return selected_corner;
	}

	/**
	 * @author Maryam
	 * @function This is to update the intermediate point after flip/rotation
	 */
	public void updateIntermediatePointsAfterRotate_Flip(ConnectionItem con,
			Point2D p, double dx, double dy) {

		// System.out.println("updateafterrotateflip");

		ComponentCanvasItem srcComp = con.src.getComponentCanvasItem();
		ComponentCanvasItem destComp = con.dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double(srcComp.getX() + con.src.getX()
				+ con.src.getHitX(), srcComp.getY() + con.src.getY()
				+ con.src.getHitY());

		Point2D end = new Point2D.Double(destComp.getX() + con.dest.getX()
				+ con.dest.getHitX(), destComp.getY() + con.dest.getY()
				+ con.dest.getHitY());

		int i = con.getCornerPoints().indexOf(p);
		System.err.println("!!!!!" + end);
		double x, y;
		double x0, y0;
		if (i != -1) {
			x = p.getX();
			y = p.getY();
			x0 = x + dx;
			y0 = y + dy;
			// sam algorithm here
			if (con.getCornerPoints().size() == 1) {
				if (x0 == start.getX()) {
					// redefine y0
					y0 = end.getY();
				} else if (x0 == end.getX()) {
					y0 = start.getY();
				} else if (y0 == start.getY()) {
					x0 = end.getX();
				} else if (y0 == end.getY()) {
					x0 = start.getX();
				} else {
					x0 = start.getX();
					y0 = end.getY();
				}
			}
			con.getCornerPoints().get(i).setLocation(x0, y0);

			moveNextPointsAfterMovingPoints(con.getCornerPoints(), i, dx, dy);
			movePrevPointsAfterMovingPoints(con.getCornerPoints(), i, dx, dy);
			mergePointsAfterMovingPoints(con.getCornerPoints(), i, start, end);
		}

		if (p == null) {
			start = new Point2D.Double(srcComp.getX() + con.src.getX()
					+ con.src.getHitX(), srcComp.getY() + con.src.getY()
					+ con.src.getHitY());

			end = new Point2D.Double(destComp.getX() + con.dest.getX()
					+ con.dest.getHitX(), destComp.getY() + con.dest.getY()
					+ con.dest.getHitY());

			if (start.getX() == end.getX() || start.getY() == end.getY()) {// no
				// need
				// to
				// insert
				// intermediate
				// points
			} else {
				con.getCornerPoints().add(
						new Point2D.Double(
								(int) ((start.getX() + end.getX()) / 2), start
										.getY()));
				con.getCornerPoints().add(
						new Point2D.Double(
								(int) ((start.getX() + end.getX()) / 2), end
										.getY()));
			}
		}
	}

	/**
	 * @author Maryam
	 * @function update position of line
	 * 
	 */
	public Line2D updateLine(ConnectionItem con, Line2D l, int dx, int dy) {
		Point2D p1 = l.getP1();
		Point2D p2 = l.getP2();

		if ((int) p1.getX() == (int) p2.getX()) {// vertical line
			// vertical line can only move in x direction
			if (dx != 0) {
				// move
				moveLine(con, p1, p2, dx, 0);
				setSelectedLine(null);
			} else if (dy != 0) {
				// dont move
			}
		} else if ((int) p1.getY() == (int) p2.getY()) {// horizontal line
			// horizontal line can only move in getY() direction
			if (dy != 0) {
				// move
				moveLine(con, p1, p2, 0, dy);
				setSelectedLine(null);
			} else if (dx != 0) {
				// dont move
			}
		}

		return new Line2D.Double(p1, p2);
	}

	// TODO: later if we need to zoom based on percentage
	public void zoom(String input) {
		double ex_scale = canvasScale;
		if (Double.parseDouble(input) > ex_scale) {
		} else if (Double.parseDouble(input) < ex_scale) {
		} else if (Double.parseDouble(input) == 100) {
		}
	}

	public void zoomIn() {
		if (scaleIndex < SCALE_LEVELS.length - 1) {
			scaleIndex++;
			setScale(scaleIndex);
			// applyZoom(change_scale);//to change canvas gridlayout size
		} else {
			String warning = "Cannot zoom in any more!";
			ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
					Message.WARNING, warning));
		}
	}

	public void zoomOut() {

		if (scaleIndex > 0) {
			scaleIndex--;
			setScale(scaleIndex);
		} else {
			String warning = "Cannot zoom out any more!";
			ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
					Message.WARNING, warning));
		}
	}

	public void zoomReset() {
		scaleIndex = DEFAULT_SCALE_INDEX;
		setScale(scaleIndex);
	}
}
package proteus.gwt.client.app.ui.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import proteus.gwt.client.app.AppController;
import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.GlobalEventHandler;
import proteus.gwt.client.app.event.RedoEvent;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.client.app.event.UndoEvent;
import proteus.gwt.client.app.ui.canvas.edit.AddComponentEdit;
import proteus.gwt.client.app.ui.canvas.edit.RemoveComponentEdit;
import proteus.gwt.client.app.ui.util.Constant;
import proteus.gwt.client.app.ui.util.GraphicUtils;
import proteus.gwt.client.app.ui.util.Line;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.client.app.ui.util.GraphicItem.Style;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;
import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Cursor;
import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Font;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.ClassDef;
import proteus.gwt.shared.modelica.Component;
import proteus.gwt.shared.modelica.ModelicaLoader;
import proteus.gwt.shared.modelica.Templates;
import proteus.gwt.shared.modelica.AnnotationParser.GraphicExpression;
import proteus.gwt.shared.modelica.ClassDef.ComponentClause;
import proteus.gwt.shared.modelica.ClassDef.Element;
import proteus.gwt.shared.modelica.ClassDef.Equation;
import proteus.gwt.shared.modelica.ClassDef.Modification;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.modelica.Placement.Transformation;
import proteus.gwt.shared.modelica.parser.ModelicaParser;
import proteus.gwt.shared.modelica.parser.OMElement;
import proteus.gwt.shared.modelica.parser.OMModification;
import proteus.gwt.shared.modelica.parser.ParseException;
import proteus.gwt.shared.util.Constants;
import proteus.gwt.shared.util.StringReader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Sam - Maryam
 */
public class DiagramCanvas extends Canvas {

	private class MouseEventsHandler extends GlobalEventHandler {
		DiagramCanvas canvas;

		private boolean mouseDown = false;

		@Override
		public void onKeyUp(KeyUpEvent event) {
			int c = event.getNativeKeyCode();

			if (!event.isAnyModifierKeyDown() && c == KeyCodes.KEY_DELETE) {
				CanvasItem items[] = getSelectedItems();
				undoSupport.postEdit(new RemoveComponentEdit(
						DiagramCanvas.this, items));
			} else if (event.isControlKeyDown()) {
				if (c == 'z' || c == 'Z') {
					ProteusGWT.eventBus.fireEvent(new UndoEvent());
				} else if (c == 'y' || c == 'Y') {
					ProteusGWT.eventBus.fireEvent(new RedoEvent());
				}
			}
		}

		public MouseEventsHandler(DiagramCanvas canvas) {
			this.canvas = canvas;
		}

		private boolean hightLight_connectionLine(Point p2d) {
			for (ConnectionItem con : getConnections()) {
				for (Line2D l : con.getLines()) {
					if (isAroundLine(l, p2d)) {
						lines = con.getLines();
						setCursor(Cursor
								.getPredefinedCursor(Cursor.MOVE_CURSOR));
						ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
						return true;
					}
				}
			}
			return false;
		}

		private boolean hightLight_connectorItem(Point p2d) {
			hitItem = hit(p2d);
			if (hitItem != null) {
				if (hitItem.isAroundConnItem(p2d.x, p2d.y)) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
					return true;
				}
			}
			return false;
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			mouseDown = true;
			Point p2d = MiscUtils.scaleLoc(event.getX(), event.getY(),
					getCanvasScale());
			hitItem = hit(p2d);
			if (hitItem != null) {
				hitItem.onMouseDown(event);
			}

			MouseDownHandler dispatchTo = currModeHandler;
			if (dispatchTo != null) {
				dispatchTo.onMouseDown(event);
			}
			// right click
			if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				if (isConnectionInProgress()) {
					popupPanel.setPopupPosition(event.getClientX(),
							event.getClientY());
					popupPanel.show();
				}
				event.stopPropagation();
				event.preventDefault();
				return;
			}
		}

		@Override
		public void onMouseDrag(MouseMoveEvent event) {
			// check whether the item is hit
			Point p2d = MiscUtils.scaleLoc(event.getX(), event.getY(),
					getCanvasScale());
			if (currModeHandler != null) {
				currModeHandler.onMouseDrag(event);
			}
			ConnectionItem[] cons = getConnections();
			Line2D l;
			for (ConnectionItem con : cons) {
				LinkedList<Point2D> corners = con.getCornerPoints();
				for (Point2D p : corners) {
					if (isIntermediatePointMoved(p, p2d.x, p2d.y)) {
						break;
					}
				}
			}

			for (ConnectionItem con : cons) {
				LinkedList<Point2D> corners = con.getCornerPoints();
				for (int i = 0; i < corners.size() - 1; i++) {
					l = new Line2D.Double(corners.get(i), corners.get(i + 1));

					if (isLineMoved(l, p2d.x, p2d.y)) {
						setCursor(Cursor
								.getPredefinedCursor(Cursor.MOVE_CURSOR));
						break;
					}
				}
			}

			ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * proteus.gwt.client.app.event.MouseEventHandler#onMouseMove(com.google
		 * .gwt.event.dom.client.MouseMoveEvent)
		 */
		@Override
		public void onMouseMove(MouseMoveEvent event) {
			if (mouseDown) {
				onMouseDrag(event);
				return;
			}
			Point p2d = MiscUtils.scaleLoc(event.getX(), event.getY(),
					getCanvasScale());

			MouseMoveHandler dispatchTo = currModeHandler;
			if (dispatchTo != null) {
				dispatchTo.onMouseMove(event);
			}

			// hitItem is assigned an Item if the mouse moves over an Item in
			// the canvas.
			boolean mouse_around_ci = false;
			boolean found = false;

			hitItem = hit(p2d);
			if (editable) {
				// mouse_around_ci = hightLight_connectorItem(p2d);
				found = hightLight_connectionLine(p2d);
			}

			// repaint the canvas
			if (isConnectionInProgress() && !found && !mouse_around_ci) {
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			mouseDown = false;
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			mouseDown = false;
			Point p2d = MiscUtils.scaleLoc(event.getX(), event.getY(),
					getCanvasScale());

			hitItem = hit(p2d);
			if (hitItem != null) {
				hitItem.onMouseUp(event);
			}

			MouseUpHandler dispatchTo = currModeHandler;
			if (dispatchTo != null) {
				dispatchTo.onMouseUp(event);
			}

			// check whether the connection is hit
			boolean isHitConnector = false;
			for (CanvasItem item : getCanvasItems()) {
				ComponentCanvasItem cci = (ComponentCanvasItem) item;
				if (cci.isHitConnItem(p2d.x, p2d.y)) {
					isHitConnector = true;
				}
			}
			if (!isHitConnector) {
				ConnectionItem[] cons = getConnections();
				for (ConnectionItem con : cons) {
					// update the codecanvas
					con.updateCodeCanvas();
					LinkedList<Point2D> corners = con.getCornerPoints();
					for (Point2D p : corners) {
						if (isIntermediatePointMoved(p, p2d.x, p2d.y)) {
							con.onMouseUp(event);
							break;
						}
					}
				}
			}
			ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
			ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			setFocus(true);
		}
	}

	enum Position {
		left, right
	}

	private boolean aroundLine = false;

	private Rectangle boundary = new Rectangle(size.width / 2, size.height / 2,
			0, 0);

	Command CancelConnectionAction = new Command() {

		public void execute() {
			popupPanel.hide();
			if (!isConnectionInProgress()) {
				return;
			}
			cancelConnection();
		}
	};

	private CanvasModelListener cml = new CanvasModelAdapter() {
		@Override
		public void selectionChanged(CanvasItem[] selected) {
			update();
		}
	};

	// To map a ComponentClassItem to its corresponding declaration in the class
	// definition
	protected Map<ComponentCanvasItem, Element> declMap = new HashMap<ComponentCanvasItem, Element>();
	// To map a connection to its corresponding connect equation in the class
	private Icon diagram;

	private boolean editable = true;

	protected int insertMgrMode;

	private LinkedList<Line2D> lines = null;

	final private PopupPanel popupPanel = new PopupPanel(true);

	private Point2D selected_corner, _selected_corner;

	private Line2D selected_line, _selected_line;

	private Position start_pose, end_pose;

	// actionmap should be passed into constructor
	public DiagramCanvas(CanvasModel model) {
		super(model);

		this.editable = model.isEditable();
		model.addCanvasModelListener(cml);
		// resistor component canvas item
		modeHandlers = createModeHandlers();
		setMode(0);

		MouseEventsHandler handler = new MouseEventsHandler(this);
		addKeyUpHandler(handler);
		addMouseDownHandler(handler);
		addMouseUpHandler(handler);
		addMouseMoveHandler(handler);
		addMouseOverHandler(handler);
		addMouseOutHandler(handler);

		// create the right click menu
		createPopupMenu();
	}

	@Override
	public void add(CanvasItem item) {
		add(new CanvasItem[] { item });
	}

	@Override
	public void add(CanvasItem[] items) {
		super.add(items);
		// classDef not set yet, i.e. this object is being initialized
		// Don't add any elements to the class, simply return
		if (classDef == null) {
			return;
		}
		for (CanvasItem item : items) {
			if (item instanceof ComponentCanvasItem) {
				ComponentCanvasItem cci = (ComponentCanvasItem) item;
				Component comp = cci.getComponent();
				try {
					StringBuilder sb = new StringBuilder();
					List<ModificationArgument> modifierList = comp.compData
							.getSuperIncludeArgList();
					int i = 0;
					for (ModificationArgument modifier : modifierList) {
						String codeStr = "";
						codeStr += modifier.getStart() == null ? ""
								: ", start=" + modifier.getStart();
						codeStr += modifier.getFixed() == null ? ""
								: ", fixed=" + modifier.getFixed();
						codeStr += modifier.getMin() == null ? "" : ", min="
								+ modifier.getMin();
						codeStr += modifier.getMax() == null ? "" : ",max="
								+ modifier.getMax();
						codeStr += modifier.getDisplayUnit() == null ? ""
								: ",displayUnit=" + modifier.getDisplayUnit();
						codeStr += modifier.getQuantity() == null ? ""
								: ",quantity=" + modifier.getQuantity();
						codeStr += modifier.getNominal() == null ? ""
								: ",nominal=" + modifier.getNominal();
						codeStr += modifier.getStateSelect() == null ? ""
								: ",stateSelect=" + modifier.getStateSelect();
						if (codeStr != "" && codeStr.length() > 0) {
							codeStr = codeStr.substring(1);
							codeStr = "(" + codeStr + ")";
						}
						codeStr = modifier.getName()
								+ codeStr
								+ ((modifier.getValue() == null) ? "" : "="
										+ modifier.getValue());
						if (i++ > 0) {
							sb.append(", ");
						}
						sb.append(codeStr);
					}

					Rectangle2D.Double b = cci.getCodeCanvasBounds();
					String varDecl = Templates.VARDECL.replace("@arg_list@",
							sb.toString());
					varDecl = varDecl.replace("@x0@", "" + b.x);
					varDecl = varDecl.replace("@y0@", "" + b.y);
					// // for the extent for cci
					int[] extSign = cci.getExtentSign();
					varDecl = varDecl.replace("@x1@", ""
							+ (extSign[0] * b.width) / 2);
					varDecl = varDecl.replace("@y1@", ""
							+ (extSign[1] * b.height) / 2);
					varDecl = varDecl.replace("@x2@", ""
							+ (extSign[2] * b.width) / 2);
					varDecl = varDecl.replace("@y2@", ""
							+ (extSign[3] * b.height) / 2);
					varDecl = varDecl.replace("@z@", "" + cci.getRotation());
					varDecl = varDecl.replace("MyTypePrefix", comp.type);
					String declName = cci.name;
					if (comp.compData.getArrayFormList() != null
							&& comp.compData.getArrayFormList().size() > 0) {
						declName += comp.compData.getArrayFormList().toString();
					}
					varDecl = varDecl.replace("foo", declName);
					// System.out.println(varDecl);
					OMElement ome = new ModelicaParser(
							new StringReader(varDecl)).element();

					Element element = (Element) ome.jjtAccept(
							new ModelicaLoader(), null);
					// @sam. to set the extent, rotation inside codecanvas
					declMap.put(cci, element);
					cci.initElement(element);
					ComponentClause compClause = element.compClause;
					if (comp.compData.getProtected_() != null
							&& comp.compData.getProtected_()
									.equals("protected")) {
						classDef.addProtectedElement(element);
					} else {
						classDef.addElement(element);
					}
					cci.updateCodeCanvas();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// } else if (item instanceof GraphicCanvasItem) {
				// addGraphicCanvasItem((GraphicCanvasItem) item);
			} else if (item instanceof ParameterItem) {

			}
		}
		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
	}

	private void updateParameters() {
	}

	private void addToCCI(List<ComponentDTO> modelList) {
		List<ComponentCanvasItem> cciList = new ArrayList<ComponentCanvasItem>();
		int i = 0;
		for (ComponentDTO compInc : modelList) {
			String restriction = compInc.getRestriction();
			if (restriction.equals(Constants.TYPE_TYPE)) {
				continue;
			}
			if (!restriction.equals(Constants.TYPE_CONNECTOR)
					&& !restriction.equals(Constants.TYPE_MODEL)
					&& !restriction.equals(Constants.TYPE_CLASS)) {
				restriction = "model";
			}
			Component c = AppController.newComponentInstance(restriction);
			c.setCompData(compInc);
			Transformation trans = GraphicUtils.getTrans(compInc.getOrigin(),
					compInc.getExtent(), compInc.getRotation());
			ComponentCanvasItem cci = new ComponentCanvasItem(this, c, c
					.getName().getValue(), compInc.getDeclarationName());
			cci.transformation = trans;

			int[] origin = cci.transformation.origin;
			int[] a = cci.transformation.extent;

			// @@sam 18 June. @@
			Rectangle bounds = GraphicUtils.topixel2(Math.min(a[0], a[2])
					+ origin[0], Math.min(a[1], a[3]) - origin[1],
					Math.abs(a[2] - a[0]), Math.abs(a[3] - a[1]));
			cci.setBounds(bounds);
			if (bounds.x < boundary.x)
				boundary.x = bounds.x;
			if (bounds.y < boundary.y)
				boundary.y = bounds.y;
			if ((bounds.x + bounds.width) > (boundary.x + boundary.width))
				boundary.width = (bounds.x + bounds.width) - boundary.x;
			if ((bounds.y + bounds.height) > (boundary.y + boundary.height))
				boundary.height = (bounds.y + bounds.height) - boundary.y;

			int[] ext = cci.transformation.oldExtent;
			// sam flip then rotate
			if (ext != null) {
				// set flip for each components
				if (ext[0] > ext[2] && ext[3] >= ext[1]) {
					CanvasUtils.flipHorizontal(cci);
				} else if (ext[2] >= ext[0] && ext[1] > ext[3]) {
					CanvasUtils.flipVertical(cci);
				} else if (ext[0] > ext[2] && ext[1] > ext[3]) {
					CanvasUtils.flipHorizontal(cci);
					CanvasUtils.flipVertical(cci);
				}
			}

			switch (cci.transformation.rotation) {
			case 0:
				break;
			case 90:
				CanvasUtils.rotateAntiClockwise(new CanvasItem[] { cci });
				break;
			case 180:
				CanvasUtils.rotateAntiClockwise(new CanvasItem[] { cci });
				CanvasUtils.rotateAntiClockwise(new CanvasItem[] { cci });
				break;
			case 270:
				CanvasUtils.rotateAntiClockwise(new CanvasItem[] { cci });
				CanvasUtils.rotateAntiClockwise(new CanvasItem[] { cci });
				CanvasUtils.rotateAntiClockwise(new CanvasItem[] { cci });
				break;
			}
			cciList.add(cci);
			i++;
		}
		add(cciList.toArray(new ComponentCanvasItem[0]));
	}

	private void addToConnectionItem(List<ConnectDTO> list) {
		List<ConnectionItem> connItemList = new ArrayList<ConnectionItem>();
		for (ConnectDTO con : list) {
			// @@sam comment 9 June, 2011 @@
			// most common case: connect(R.p, C.n)
			// second case: the component is itself a connector. e.g.
			// connect(R.p, n)
			// thrid case: more than 2 layers: e.g. connect(R.c.p, C.n)
			ConnectorItem src = null, dest = null;

			String s = con.getStartpin();
			String s1 = con.getEndpin();
			src = findConnectorItem(s);
			dest = findConnectorItem(s1);

			// special case. if one is null and the other one is not
			// null..check if it is expanable connector
			if (src != null && dest == null) {
				dest = makeExpanableConnector(src, con.getEndpin());
			} else if (src == null && dest != null) {
				src = makeExpanableConnector(dest, con.getStartpin());
			}
			if (src == null || dest == null) {
				try {
					throw new Exception();
				} catch (Exception e) {
					if (src == null)
						Window.alert("connector pin is not found: " + s);
					// System.err.println("connector pin is not found: " + s);
					if (dest == null)
						Window.alert("connector pin is not found: " + s1);
					// System.err.println("connector pin is not found: " + s1);
					e.printStackTrace();
				}
			}

			// use the src and dest to creat the connectionItem
			ConnectionItem connection = new ConnectionItem(this, src, dest);
			// connection.getCornerPoints().add
			// this add method will also add a item in connMap
			// parse the connection annotation or corner point

			// text could be null; connection annotation could be null;
			String text = con.getValue();
			if (text != null && text.trim() != "" && text.length() > 0) {
				List<GraphicExpression> graphicExpressions = GraphicExpression
						.parseGraphic(text);
				for (GraphicExpression exp : graphicExpressions) {
					if (exp.name.equals(Line.NAME)) {
						List<NamedArgument> args = exp.namedArguments;
						if (args == null)
							continue;
						Style style = new Style(args);
						connection.setStyle(style);
					}
				}

			} else {
				// This is the case when the default demo has no annotation
				// defined for connection line.
				connection.getCornerPoints().addAll(drawConnection(src, dest));
			}
			Rectangle2D.Double bounds = connection.getBoundary();
			if (bounds.x < boundary.x)
				boundary.x = (int) bounds.getX();
			if (bounds.y < boundary.y)
				boundary.y = (int) bounds.y;
			if ((bounds.getX() + bounds.width) > (boundary.getX() + boundary.width))
				boundary.width = (int) ((bounds.x + bounds.width) - boundary.x);
			if ((bounds.y + bounds.height) > (boundary.y + boundary.height))
				boundary.height = (int) ((bounds.y + bounds.height) - boundary.y);
			connItemList.add(connection);
		}// for connection
		addConnections(connItemList.toArray(new ConnectionItem[0]));
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public ModeHandler[] createModeHandlers() {
		ModeHandler[] mhs = new ModeHandler[4];

		int n = 0;
		mhs[n] = new SelectionManager(this, n, isPreserveAspectRatio());
		selMgrMode = n++;
		mhs[n] = new MoveModeManager(this, n);
		moveMgrMode = n++;
		mhs[n] = new ConnectionManager(this, n);
		connMgrMode = n++;
		mhs[n] = new InsertManager(this, n);
		insertMgrMode = n++;
		return mhs;
	}

	private void createPopupMenu() {
		MenuBar popupMenuBar = new MenuBar(true);
		MenuItem alertItem = new MenuItem("Cancel Connection", true,
				CancelConnectionAction);

		popupPanel.setStyleName("popup");
		alertItem.addStyleName("popup-item");

		popupMenuBar.addItem(alertItem);

		popupMenuBar.setVisible(true);
		popupPanel.add(popupMenuBar);
	}

	public LinkedList<Point2D> drawConnection(ConnectorItem src,
			ConnectorItem dest) {

		LinkedList<Point2D> ps = new LinkedList<Point2D>();

		ComponentCanvasItem srcComp = src.getComponentCanvasItem();
		ComponentCanvasItem destComp = dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double(
				(srcComp.getX() + src.getX() + src.getHitX()), (srcComp.getY()
						+ src.getY() + src.getHitY()));

		Point2D start_otherside;
		if (start.getX() > srcComp.getX()) {// start point is at the right
			start_pose = Position.right;
			start_otherside = new Point2D.Double(start.getX()
					- srcComp.getWidth(), start.getY());
		} else {// start point is at the left
			start_pose = Position.left;
			start_otherside = new Point2D.Double(start.getX()
					+ srcComp.getWidth(), start.getY());
		}

		Point2D end = new Point2D.Double(
				(destComp.getX() + dest.getX() + dest.getHitX()),
				(destComp.getY() + dest.getY() + dest.getHitY()));

		Point2D end_otherside;
		if (end.getX() > destComp.getX()) {// end point is at the right
			end_pose = Position.right;
			end_otherside = new Point2D.Double(
					end.getX() - destComp.getWidth(), end.getY());
		} else { // end point is at the left
			end_pose = Position.left;
			end_otherside = new Point2D.Double(
					end.getX() + destComp.getWidth(), end.getY());
		}

		Point2D p1, p2;
		Point2D.Double p3 = null;
		Point2D.Double p4 = null;

		Line2D l1, l3;

		if (start.getX() == end.getX() || start.getY() == end.getY()) {
			// no need to insert intermediate points
		} else {
			p1 = new Point2D.Double((int) ((start.getX() + end.getX()) / 2),
					start.getY());
			p2 = new Point2D.Double((int) ((start.getX() + end.getX()) / 2),
					end.getY());

			l1 = new Line2D.Double(start, p1);
			l3 = new Line2D.Double(p2, end);

			// System.out.println("l1_cross:" + isPointOnLine(l1.getP1(),
			// l1.getP2(), start_otherside));
			// System.out.println("l3_cross:" + isPointOnLine(l3.getP1(),
			// l3.getP2(), end_otherside));

			if ((srcComp.getBounds().contains(l1.getP2()) || isPointOnLine(
					l1.getP1(), l1.getP2(), start_otherside))
					&& // both l1 and l3 cross the item
					(destComp.getBounds().contains(l3.getP1()) || isPointOnLine(
							l3.getP1(), l3.getP2(), end_otherside))) {
				// System.out.println("l1 & l3 cross");
				if (start_pose.equals(Position.right))
					p1.setLocation(start.getX() + 2 * src.getWidth(),
							start.getY());
				else if (start_pose.equals(Position.left))
					p1.setLocation(start.getX() - 2 * src.getWidth(),
							start.getY());
				if (end_pose.equals(Position.right))
					p4 = new Point2D.Double(end.getX() + 2 * dest.getWidth(),
							end.getY());
				else if (end_pose.equals(Position.left))
					p4 = new Point2D.Double(end.getX() - 2 * dest.getWidth(),
							end.getY());
				p2.setLocation(p1.getX(),
						Math.min(srcComp.getY(), destComp.getY()) - 10);
				p3 = new Point2D.Double(p4.getX(), p2.getY());
			} else if (srcComp.getBounds().contains(l1.getP2())
					|| isPointOnLine(l1.getP1(), l1.getP2(), start_otherside)) {// only
				// l1
				// cross
				// over
				// the
				// item
				// System.out.println("l1 cross");
				if (start_pose.equals(Position.right))
					p1.setLocation(start.getX() + 2 * src.getWidth(), p1.getY());
				else if (start_pose.equals(Position.left))
					p1.setLocation(start.getX() - 2 * src.getWidth(), p1.getY());
				p2.setLocation(p1.getX(), p2.getY());
			} else if (destComp.getBounds().contains(l3.getP1())
					|| isPointOnLine(l3.getP1(), l3.getP2(), end_otherside)) {// only
				// l3
				// cross
				// over
				// the
				// item
				// System.out.println("l3 cross");
				if (end_pose.equals(Position.right))
					p2.setLocation(end.getX() + 2 * dest.getWidth(), p2.getY());
				else if (end_pose.equals(Position.left))
					p2.setLocation(end.getX() - 2 * dest.getWidth(), p2.getY());
				p1.setLocation(p2.getX(), p1.getY());
			}

			ps.add(p1);
			ps.add(p2);
			if (p3 != null && p4 != null) {
				ps.add(p3);
				ps.add(p4);
			}
		}
		return ps;
	}

	public boolean dropOnCanvas(Object data, Point p) {
		if (data != null) {
			CanvasItem ci = null;
			if (data instanceof Component) {
				Component c = (Component) data;
				String r = c.restriction;
				if (r.equals(Constants.TYPE_MODEL)
						|| r.equals(Constants.TYPE_CLASS)
						|| r.equals(Constants.TYPE_BLOCK)
						|| r.equals(Constants.TYPE_RECORD)
						|| r.equals(Constants.TYPE_CONNECTOR)
						|| r.equals(Constants.TYPE_EXPANDABLECONNECTOR)) {
					String name = c.name.getValue();
					name = Character.toLowerCase(name.charAt(0))
							+ (name.length() > 1 ? name.substring(1) : "");
					String declName = createDeclName(c.name.getValue());

					ci = new ComponentCanvasItem(this, c, c.name.getValue(),
							declName);
				} else {
					String msg = "Cannot insert " + c.type
							+ " because it is not a model, "
							+ "class, block, record or connector";
					GWT.log(msg);
				}
			}
			if (ci != null) {
				ci.setLocation(p.x, p.y);
				// debug Data to be removed: TODO
				// if(ci.name.equals("ground0")) {
				// ci.setLocation(200,200);
				// }else if(ci.name.equals("pi0")) {
				// ci.setLocation(400,100);
				// }
				// ci.setLocation(500, 400);
				// Drop this componentCanvasItem on the canvas
				undoSupport.postEdit(new AddComponentEdit(this,
						new CanvasItem[] { ci }));
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());

				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private ConnectorItem findConnectorItem(String s) {
		// @@sam comment 9 June, 2011 @@
		// most common case: connect(R.p, C.n)
		// second case: the component is itself a connector. e.g.
		// connect(R.p, n)
		// thrid case: more than 2 layers: e.g. connect(R.c.p, C.n)
		ConnectorItem conn = null;
		ComponentCanvasItem conncci = null;
		int firstDot = s.indexOf(".");
		int lastDot = s.lastIndexOf(".");
		// second case, when the component itself is a connector
		if (firstDot == -1) {
			String cciName = s;
			conncci = findCorrectCCI(cciName);
			// make a connectorItem with itself.
			// double check the conncci type is a connector
			// conncci
			conn = conncci.getConnectorItem(null, cciName);
		} else if (firstDot == lastDot) {
			// first case, most common case. connect(R.p, C.n)
			String cciName = s.substring(0, firstDot).trim();
			String cciPin = s.substring(firstDot + 1).trim();
			conncci = findCorrectCCI(cciName);
			// find the connectorItem inside this componentcanvasItem
			// get the pin
			// System.err.println(cciName+"::"+cciPin);
			conn = conncci.getConnectorItem(null, cciPin);
		} else if (firstDot != lastDot) {
			// third case, not implemented yet.
			try {
				throw new Exception();
			} catch (Exception e) {
				System.err.println("the connection layer is more than 2!");
				e.printStackTrace();
			}
		}
		return conn;
	}

	private ComponentCanvasItem findCorrectCCI(String cciName) {
		// find the correct include component.
		ComponentCanvasItem conncci = null;
		for (CanvasItem item : getCanvasItems()) {
			ComponentCanvasItem cci = (ComponentCanvasItem) item;
			if (cci.getName().equals(cciName)) {
				conncci = cci;
				break;
			}
		}
		// check exception;
		if (conncci == null) {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.err.println("correct cci was not found!" + cciName);
				e.printStackTrace();
			}
		}
		return conncci;
	}

	public ClassDef getClassDef() {
		return classDef;
	}

	/**
	 * @author Maryam
	 * @function if two connection lines have common src and different
	 *           destination, the black rectangles are highlighted at
	 *           intersection point. This function determines whether the two
	 *           lines of l1 and l2 are in the above situation
	 */
	public ArrayList<Point2D> getIntersectPoint(Line2D l1, Line2D l2) {

		ArrayList<Point2D> p = new ArrayList<Point2D>();

		double x1 = l1.getP1().getX();
		double y1 = l1.getP1().getY();
		double x2 = l1.getP2().getX();
		double y2 = l1.getP2().getY();
		double x3 = l2.getP1().getX();
		double y3 = l2.getP1().getY();
		double x4 = l2.getP2().getX();
		double y4 = l2.getP2().getY();

		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d == 0)
			return null;

		// more than one intersect point
		if ((x1 == x2) && (x3 == x4) && (x1 == x3)) {// l1 and l2 are vertical
			// and overlaps on more
			// than one point
		} else if ((y1 == y2) && (y3 == y4) && (y1 == y3)) {// l1 and l2 are
			// horizontal and
			// overlaps on more
			// than one point
		} else {// only one intersect point
			double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2)
					* (x3 * y4 - y3 * x4))
					/ d;
			double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2)
					* (x3 * y4 - y3 * x4))
					/ d;

			/*
			 * if (xi <= Math.max(x1, x2) && yi <= Math.max(y1, y2) && xi >=
			 * Math.min(x1, x2) && yi >= Math.min(y1, y2) && xi <= Math.max(x3,
			 * x4) && yi <= Math.max(y3, y4) && yi >= Math.min(x3, x4) && yi >=
			 * Math.min(y3, y4))
			 */
			p.add(new Point2D.Double(xi, yi));
		}
		return p;

	}

	@Override
	public int[] getRGBArray(int x, int y, int w, int h) {
		return super.getRGBArray(x, y, w, h);
	}

	@Override
	public CanvasItem[] getSelectedItems() {
		return model.getSelectedItems();
	}

	/**
	 * @author Maryam
	 * @function whether the mouse is around a line
	 */
	public boolean isAroundLine(Line2D l, Point p) {
		double scale = getCanvasScale();

		Rectangle2D rect;
		rect = new Rectangle2D.Double(p.x - 5, p.y - 5, 10, 10);

		if (rect.intersectsLine(l)) {
			aroundLine = true;
		}
		return aroundLine;
	}

	/**
	 * @author Maryam
	 * @function
	 */
	public boolean isInRange(double z1, double z2, double width_height) {
		double differ = Math.abs(Math.abs(z1) - Math.abs(z2));

		if (differ <= 2 * width_height)
			return true;
		else
			return false;
	}

	/**
	 * @author Maryam
	 * @function: whether an intermediate point has been moved
	 */
	public boolean isIntermediatePointMoved(Point2D p, double ex, double ey) {
		_selected_corner = null;

		if (isInRange(ey, p.getY(), 6 / canvasScale)
				&& isInRange(ex, p.getX(), 6 / canvasScale)
				&& getSelectedLine() == null && getSelectedItem() == null) {
			_selected_corner = p;
		}

		setSelectedCorner(_selected_corner);
		if (_selected_corner != null) {
			return true;
		}
		return false;
	}

	/**
	 * @author Maryam
	 * @function whether a line has been moved
	 */
	public boolean isLineMoved(Line2D l, double ex, double ey) {
		_selected_line = null;

		Rectangle2D rect = new Rectangle2D.Double(ex - 5 / canvasScale, ey - 5
				/ canvasScale, 10 / canvasScale, 10 / canvasScale);// TODO
		// correct
		// scale?
		if (rect.intersectsLine(l) && getSelectedCorner() == null
				&& getSelectedItem() == null) {
			_selected_line = l;
		}

		if (selected_line != _selected_line) {
			selected_line = _selected_line;
		}

		if (selected_line != null) {
			setSelectedLine(selected_line);
			return true;
		}
		return false;
	}

	/**
	 * @author Maryam
	 * @function determines whether point is located on the line constructed
	 *           from linePointA and linePointB
	 */
	public boolean isPointOnLine(Point2D linePointA, Point2D linePointB,
			Point2D point) {

		final float EPSILON = 0.001f;

		double by = linePointB.getY();
		double bx = linePointB.getX();
		double ay = linePointA.getY();
		double ax = linePointA.getX();

		double a = (by - ay) / (ax - bx);
		double b = ay - a * ax;
		if ((Math.abs(point.getY() - (a * point.getX() + b)) == 0)
				&& (point.getX() <= Math.max(ax, bx))
				&& (point.getY() <= Math.max(ay, by))
				&& (point.getX() >= Math.min(ax, bx))
				&& (point.getY() >= Math.min(ay, by))) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isPreserveAspectRatio() {
		return true;
	}

	private void loadClassDef(ClassDef classDef) {
		if (!(classDef instanceof Component))
			return;

		Component comp = (Component) classDef;

		if (comp.componentType == Component.COMPONENT_DEMO) {
			
			//add parameters to codecanvas
			List<ParameterDTO> paras = comp.compData.getParameters();
			for (ParameterDTO para : paras) {
				ParameterItem paraItem = new ParameterItem(this, comp, para, "");
				ParameterItem pi = (ParameterItem) paraItem;
				ParameterDTO paraDTO = pi.getPara();
				try {
					String codeStr = "";
					codeStr += paraDTO.getStart() == null ? "" : ", start="
							+ paraDTO.getStart();
					codeStr += paraDTO.getFixed() == null ? "" : ", fixed="
							+ paraDTO.getFixed();
					codeStr += paraDTO.getMin() == null ? "" : ", min="
							+ paraDTO.getMin();
					codeStr += paraDTO.getMax() == null ? "" : ", max="
							+ paraDTO.getMax();
					codeStr += paraDTO.getDisplayUnit() == null ? ""
							: ", displayUnit=\"" + paraDTO.getDisplayUnit()
									+ "\"";
					// codeStr += paraDTO.getQuantity() == null ? "" :
					// ", quantity="
					// + paraDTO.getQuantity();
					codeStr += paraDTO.getNominal() == null ? "" : ", nominal="
							+ paraDTO.getNominal();
					codeStr += paraDTO.getStateSelect() == null ? ""
							: ", stateSelect=" + paraDTO.getStateSelect();

					if (codeStr != "" && codeStr.length() > 0) {
						codeStr = codeStr.substring(1);
						codeStr = "(" + codeStr + ")";
					}
					codeStr = ((paraDTO.getTyping() == null) ? "" : paraDTO
							.getTyping() + " ")
							+ ((paraDTO.getVariability() == null) ? ""
									: paraDTO.getVariability() + " ")
							+ ((paraDTO.getCausality() == null) ? "" : paraDTO
									.getCausality() + " ")
							+ paraDTO.getWholename()
							+ " "
							+ paraDTO.getName()
							+ " "
							+ codeStr
							+ ((paraDTO.getValue() == null) ? "" : "="
									+ paraDTO.getValue());

					OMElement ome = new ModelicaParser(
							new StringReader(codeStr)).element();

					Element element = (Element) ome.jjtAccept(
							new ModelicaLoader(), null);
					classDef.addElement(element);
//					Window.alert(codeStr);
					// System.out.println(codeStr);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			//add components to codecanvas
			ComponentDTO model = comp.compData;
			addToCCI(model.getIncludeComps());

			for (ComponentDTO compExt : model.getExtendComps()) {
				if (compExt.getRestriction().equals(Constants.TYPE_TYPE)) {
					continue;
				}
				addToCCI(compExt.getIncludeComps());
			}

			// add connection items
			addToConnectionItem(model.getConnects());
			for (ComponentDTO compExt : model.getExtendComps()) {
				if (compExt.getRestriction().equals(Constants.TYPE_TYPE)) {
					continue;
				}
				addToConnectionItem(compExt.getConnects());
			}

			if (comp.compData.getWholeName().contains("Examples")) {
				if (boundary.width != 0 && boundary.height != 0) {
					scaleIndex = (int) ((size.width / (boundary.width / 2)) / 0.25) - 1;
					if (scaleIndex > 6)
						scaleIndex = 6;
					if (scaleIndex < 0)
						scaleIndex = 3;
					setScale(scaleIndex);
				}
			}
		}

	}

	private ConnectorItem makeExpanableConnector(ConnectorItem item, String s) {
		ConnectorItem conn = null;
		ComponentCanvasItem conncci = null;
		int firstDot = s.indexOf(".");
		int lastDot = s.lastIndexOf(".");
		if (firstDot == -1) {
		} else if (firstDot == lastDot) {
			// first case, most common case. connect(R.p, C.n)
			String cciName = s.substring(0, firstDot).trim();
			String cciPin = s.substring(firstDot + 1).trim();
			conncci = findCorrectCCI(cciName);
			// find the connectorItem inside this componentcanvasItem
			conn = conncci.getConnectorItem(item, cciPin);
		}
		return conn;
	}

	public void scaleCanvas(Graphics2D g2) {
		Dimension canvasSize = Constant.diagramCanvasSize;
		g2.translate(canvasSize.width / 2, canvasSize.height / 2);
		g2.scale(canvasScale, canvasScale);
		// System.out.println(canvasScale +
		// " Inside scaleCanvas DiagramCanvas");
		g2.translate(-canvasSize.width / 2, -canvasSize.height / 2);
	}

	@Override
	protected void paintCanvasItems(Graphics2D g2) {
		scaleCanvas(g2);
		// drawGridLine(g2);
		// this is to draw the individual component
		if (editable) {
			super.paintCanvasItems(g2);
		} else {
			// this is the paint double click opened component or demoes
			paintDiagram(g2);
		}

		// This is to draw the connections
		ConnectionItem[] connections = getConnections();
		for (ConnectionItem conn : connections) {
			conn.paint(g2);
		}

		if (aroundLine) {
			// around it
			g2.setColor(Color.PINK);
			g2.setStroke(new BasicStroke(2));
			for (Line2D l : lines) {
				g2.draw(l);
			}
			aroundLine = false;
		}
	}

	private void drawIntersectionPoint(Graphics2D g2) {
		// @Maryam: This is to show the black rectangles at the intersections of
		// connection lines
		ConnectionItem[] connections = getConnections();
		LinkedList<Line2D> lines;
		LinkedList<Point2D> points;
		g2.setColor(Color.BLACK);
		for (int m = 0; m < connections.length; m++) {
			ConnectorItem src_comp = connections[m].getSource();
			points = connections[m].getCornerPoints();
			if (points != null) {
				for (int n = 0; n < connections.length; n++) {
					lines = connections[n].getLines();
					if (m != n) {
						for (int p = 0; p < points.size(); p++) {
							for (int k = 0; k < lines.size(); k++) {
								if (isPointOnLine(lines.get(k).getP1(), lines
										.get(k).getP2(), points.get(p))) {
									double px = points.get(p).getX();
									double py = points.get(p).getY();
									double srcWidth = src_comp.getWidth();
									double srcHeight = src_comp.getHeight();
									Rectangle2D rect = new Rectangle2D.Double(
											px - srcWidth / 2 * canvasScale, py
													- srcHeight / 2
													* canvasScale, srcWidth
													* canvasScale, srcHeight
													* canvasScale);
									g2.fillRect((int) rect.getX(),
											(int) rect.getY(),
											(int) rect.getWidth(),
											(int) rect.getHeight());
								}
							}
						}
					}
				}
			}
		}
	}

	private void paintDiagram(Graphics2D g2) {
		Component component = null;
		if (!(classDef instanceof Component))
			return;
		component = (Component) classDef;
		if (component.componentType == Component.COMPONENT_DEMO) {
			diagram = component.getDiagram2(g2);
			Dimension canvasSize = Constant.diagramCanvasSize;

			super.paintCanvasItems(g2);
			g2.saveTransform();
			// @sam minus out -100, -100 as it starts from -100, and -100;
			g2.translate(canvasSize.width / 2 - 100,
					canvasSize.height / 2 - 100);
			if (diagram != null) {
				diagram.setThickness(1);
				if (component.getCompData().getConnects().size() > 0
						|| component.getCompData().getIncludeComps().size() == 0) {
					diagram.paint(g2);
					diagram.paintText(g2, null, new Font("Arial", Font.PLAIN,
							12));
				}
			}
			g2.restoreTransform();
		} else if (component.componentType == Component.DIAGRAM_TYPE) {
			diagram = component.getDiagram(g2);
			Dimension canvasSize = Constant.diagramCanvasSize;
			Dimension diagramSize = Constant.diagramSize;
			double transWidth = (canvasSize.width - diagramSize.width) / 2;
			double transHeight = (canvasSize.height - diagramSize.height) / 2;
			g2.translate(transWidth, transHeight);
			diagram.setThickness(1);
			diagram.paint(g2);
			diagram.paintText(g2, null, new Font("Arial", Font.PLAIN, 12));
			g2.translate(-transWidth, -transHeight);
		}
	}

	@Override
	public void remove(CanvasItem item) {
		remove(new CanvasItem[] { item });
	}

	public ComponentCanvasItem[] getComponentCanvasItem() {
		Set<ComponentCanvasItem> cciSet = declMap.keySet();
		return cciSet.toArray(new ComponentCanvasItem[0]);
	}

	@Override
	public void remove(CanvasItem[] items) {
		for (CanvasItem item : items) {
			if (item instanceof ComponentCanvasItem) {
				ComponentCanvasItem cci = (ComponentCanvasItem) item;
				Element element = declMap.get(cci);
				// 15/03/2011 remove the component canvasitem should remove the
				// code from code as well
				classDef.removeElement(element);
				declMap.remove(cci);
				ConnectionItem[] conns = getConnections();
				for (ConnectionItem conn : conns) {
					ComponentCanvasItem src = conn.src.getComponentCanvasItem();
					ComponentCanvasItem dest = conn.dest
							.getComponentCanvasItem();
					if (src == cci || dest == cci) {
						Equation equation = connectMap.get(conn);
						classDef.removeEquation(equation);
						connectMap.remove(conn);
					}
				}
				// File f = cci.getCustomLibFile();
				// if (f != null) {
				// ((Component) getClassDef()).removeLibFile(f);
				// }
			} else if (item instanceof ConnectionItem) {
				removeConnection((ConnectionItem) item);
			}
		}
		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
		// Should be called after connections are removed. sam: why?
		super.remove(items);
	}

	@Override
	public void repaint() {
		drawWholeCanvas();
		// drawGridCanvas();
	}

	private void drawWholeCanvas() {
		clearCanvas();

		saveTransform();
		paintCanvasItems(this);
		restoreTransform();
	}

	public void setClassDef(ClassDef classDef) {
		connectMap.clear();
		this.classDef = classDef;
		loadClassDef(classDef);
	}

	@Override
	public void setSelectedItems(CanvasItem[] selectedItems) {
		model.setSelectedItems(selectedItems);
	}

	public void update() {
	}

	public void setParameterModification(ComponentCanvasItem cci, String s) {
		// System.out.println("ParaModification: "+s);
		ModelicaParser parser = new ModelicaParser(new StringReader(s));
		try {
			OMModification modification = parser.modification();
			Modification m = (Modification) modification.jjtAccept(
					new ModelicaLoader(), null);
			Element ele = declMap.get(cci);
			ele.getObjectModel().componentClause.componentList
					.getComponentDecls().get(0).getDecl()
					.setModification(modification);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void chang2Protected(ComponentCanvasItem componentCanvasItem) {
		Element e = declMap.get(componentCanvasItem);
		classDef.Change2ProtectedElement(e);
	}

	public void change2Public(ComponentCanvasItem cci) {
		Element e = declMap.get(cci);
		classDef.Change2PublicElement(e);
	}
}

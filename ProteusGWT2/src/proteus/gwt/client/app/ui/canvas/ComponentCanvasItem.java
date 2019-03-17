package proteus.gwt.client.app.ui.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.MouseEventHandler;
import proteus.gwt.client.app.event.OpenComponentEvent;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.ui.util.Constant;
import proteus.gwt.client.app.ui.util.GraphicUtils;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Font;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.Component;
import proteus.gwt.shared.modelica.Connector;
import proteus.gwt.shared.modelica.ClassDef.Annotation;
import proteus.gwt.shared.modelica.ClassDef.Argument;
import proteus.gwt.shared.modelica.ClassDef.ComponentClause;
import proteus.gwt.shared.modelica.ClassDef.ComponentDecl;
import proteus.gwt.shared.modelica.ClassDef.Element;
import proteus.gwt.shared.modelica.ClassDef.Modification;
import proteus.gwt.shared.modelica.Placement.Transformation;
import proteus.gwt.shared.modelica.parser.OMModification;
import proteus.gwt.shared.types.StringProperty;
import proteus.gwt.shared.util.Constants;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;

public class ComponentCanvasItem extends CanvasItem {

	private DialogBox propertyBox;

	public DialogBox attributeBox;

	public class PropertiesAction implements Command {

		@Override
		public void execute() {
			popupMenu.hide();
			// getPropertiesDialog().showBox();
			// new propertyBox design
			// if(propertyBox==null) {
			propertyBox = new PropertyDialogBox(ComponentCanvasItem.this);
			// }
			propertyBox.center();
		}
	}

	public class AttributeAction implements Command {

		@Override
		public void execute() {
			popupMenu.hide();
			// getPropertiesDialog().showBox();
			// new propertyBox design
			// if(propertyBox==null) {
			attributeBox = new AttributeDialogBox(ComponentCanvasItem.this);
			// }
			attributeBox.center();
		}
	}

	public class OpenComponentAction implements Command {

		@Override
		public void execute() {
			popupMenu.hide();
			ProteusGWT.eventBus.fireEvent(new OpenComponentEvent(
					ComponentCanvasItem.this.component));
		}
	}

	public class DisconnectAction implements Command {

		@Override
		public void execute() {
			popupMenu.hide();
			Window.alert("action not implemented yet");
		}
	}

	public class MouseEventListener extends MouseEventHandler {
		boolean clearFlag = true;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * proteus.gwt.client.app.event.MouseEventHandler#onMouseDown(com.google
		 * .gwt.event.dom.client.MouseDownEvent)
		 */
		@Override
		public void onMouseDown(MouseDownEvent event) {
			super.onMouseDown(event);

			if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				// should show the menuItem
				showPopup(event);
			}
			event.stopPropagation();
			event.preventDefault();
		}

		private void showPopup(MouseDownEvent event) {
			PopupPanel pm = getPopupMenu();
			if (pm != null) {
				pm.setPopupPosition(event.getClientX(), event.getClientY());
				pm.show();
			}
		}
	}

	private Icon icon;

	private Icon diagram;

	private DiagramCanvas dc;

	private Component component;

	private List<ConnectorItem> connItems;
	// private List<Text> labels = new ArrayList<Text>();//maryam just added
	protected final int HANDLE_SIZE = 6;
	private int t_out, t_in, t_un;
	public static final int NW = 1, NE = 2, SW = 3, SE = 4, // @Maryam: I
			// changed all the
			// values to
			// positive ones
			N = 5, E = 6, S = 7, W = 8;

	private int xoff, yoff;
	private ConnectorItem hitConnItem, _hitConnItem;

	// variability of component
	// causality
	// properties
	// dynamic typing
	public StringProperty varibilityProperty;
	public StringProperty finalProperty;
	public StringProperty protectedProperty;
	public StringProperty replaceableProperty;
	public StringProperty typingProperty;
	public StringProperty causalityProperty;

	private HashMap<String, Command> actions;

	private List<proteus.gwt.client.app.ui.canvas.ComponentCanvasItem> compItems;

	private boolean preserveAspectRatio;

	/**
	 * @return the actions
	 */
	public HashMap<String, Command> getActions() {
		return actions;
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	public void setActions(HashMap<String, Command> actions) {
		this.actions = actions;
	}

	public Command getAction(String key) {
		return actions.get(key);
	}

	public ComponentCanvasItem(DiagramCanvas dc, Component c, String prefix,
			String declName) {
		super(dc, prefix);
		this.dc = dc;
		preserveAspectRatio = dc.isPreserveAspectRatio();
		this.component = c;
		this.name = declName;

		ParameterProperty pp = getpp(Constants.DECL_NAME);
		if (pp != null) {
			pp.setValue(declName);
		}

		// if itself is a connector add itself to its connector list
		if (GraphicUtils.isConnectorComp(component)) {
			Transformation connTrans = new Transformation();
			int connWidth = 40;// Math.abs(trans.extent[2]-trans.extent[0]);
			int connHeight = 40;// Math.abs(trans.extent[3]-trans.extent[1]);

			connTrans.extent = new int[] { -connWidth, connHeight, connWidth,
					-connHeight };
			connTrans.extent = new int[] { -connWidth, -connHeight, connWidth,
					connHeight };
			c.addselfConnector(declName, connTrans);
		}

		// generate the icon.
		icon = component.getIcon(dc, declName);
		connItems = component.getConnectorItems(dc);
		initcci();
	}

	private void initcci() {
		int width = icon.getIconWidth();
		int height = icon.getIconHeight();

		for (ConnectorItem connItem : connItems) {
			connItem.notifyAdd(this);
			if (connItem instanceof ConnectorItem.Input) {
				t_in++;
			} else if (connItem instanceof ConnectorItem.Output) {
				t_out++;
			} else {
				t_un++;
			}
			// set the flip and rotation information;
			// @sam comment@ should comment here then
			if (connItem.connector.getTransformation() != null) {
				int[] ext = connItem.connector.getTransformation().oldExtent;
				// flip then rotate
				if (ext != null) {
					// set flip for each components
					if (ext[2] < ext[0] && ext[3] >= ext[1]) {
						CanvasUtils.flipHorizontal(connItem);
					} else if (ext[2] >= ext[0] && ext[3] < ext[1]) {
						CanvasUtils.flipVertical(connItem);
					} else if (ext[2] < ext[0] && ext[3] < ext[1]) {
						CanvasUtils.flipHorizontal(connItem);
						CanvasUtils.flipVertical(connItem);
					}
				}

				switch (connItem.connector.getRotation()) {
				case 0:
					break;
				case 90:
					CanvasUtils.rotateAntiClockwise(connItem);
					break;
				case 180:
					CanvasUtils.rotateAntiClockwise(connItem);
					CanvasUtils.rotateAntiClockwise(connItem);
					break;
				case 270:
					CanvasUtils.rotateAntiClockwise(connItem);
					CanvasUtils.rotateAntiClockwise(connItem);
					CanvasUtils.rotateAntiClockwise(connItem);
					break;
				case -1:
					// this item doesn't have rotation attribute
					break;
				default:
					connItem.setState(State.A, State.A);
					break;
				}

			}
			// Icon connectorIcon = connItem.connector.getIcon();
			// set rotation
		}
		xoff = t_in > 0 ? 15 : 0;
		yoff = t_un > 0 ? 15 : 0;

		setBounds(0, 0, width, height);
		setResizable(true);

		actions = new HashMap<String, Command>();
		actions.put(Constant.DISCONNECTIONACTION, new DisconnectAction());
		actions.put(Constant.OPENCOMPONENTACTION, new OpenComponentAction());
		actions.put(Constant.PROPERTIESACTION, new PropertiesAction());
		actions.put(Constant.ATTRIBUTEACTION, new AttributeAction());
		// add mouseListener
		addMouseEventHandler(new MouseEventListener());
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		Rectangle nb = new Rectangle();
		getNormBounds(nb);

		if (icon instanceof Icon.ModelicaImpl) {
			Icon.ModelicaImpl im = (Icon.ModelicaImpl) icon;
			im.setParentCoords(im.getExtent());
			im.setIconSize(nb.width, nb.height);
		} else {
			icon.setIconSize(nb.width, nb.height);
		}
		setConnItemBounds(x, y, width, height);
	}

	public void setConnItemBounds(int x, int y, int width, int height) {
		int in = 0, out = 0, un = 0;
		if (connItems != null) {
			for (ConnectorItem connItem : connItems) {
				// set bounds
				boolean boundsSet = false;
				Icon compCanvasItemIcon = getComponent().getIcon();
				if (compCanvasItemIcon instanceof Icon.ModelicaImpl) {
					Icon.ModelicaImpl im = (Icon.ModelicaImpl) compCanvasItemIcon;

					im.setParentCoords(((Icon.ModelicaImpl) icon).getExtent());
					im.setIconSize(im.getIconWidth(), im.getIconHeight());
					int[] parentExtent = im.getExtent();
					int[] extent = connItem.connector.getExtent();
					int[] origin = connItem.connector.getOrigin();
					if (extent != null && parentExtent != null) {
						Rectangle pr = new Rectangle(Math.min(parentExtent[2],
								parentExtent[0]), Math.min(parentExtent[3],
								parentExtent[1]), Math.abs(parentExtent[2]
								- parentExtent[0]), Math.abs(parentExtent[3]
								- parentExtent[1]));
						Rectangle r = new Rectangle(Math.min(extent[2],
								extent[0]) + origin[0], -origin[1]
								+ Math.min(extent[3], extent[1]),
								Math.abs(extent[2] - extent[0]),
								Math.abs(extent[3] - extent[1]));

						if (getCanvas().is_resized) {

						} else {
							double wRatio = (double) width / (double) pr.width;
							double hRatio = (double) height
									/ (double) pr.height;
							Rectangle2D.Double rrd = new Rectangle2D.Double(
									((r.x - pr.x) * wRatio),
									((r.y - pr.y) * hRatio),
									(r.width * wRatio), (r.height * hRatio));

							connItem.setBounds(rrd);
							Rectangle rr = new Rectangle(
									(int) ((r.x - pr.x) * wRatio),
									(int) ((r.y - pr.y) * hRatio),
									(int) (r.width * wRatio),
									(int) (r.height * hRatio));
							connItem.set_bounds(rr);
						}

						boundsSet = true;
					} else if (extent != null) {
						// 09 Oct 13 lt : deal with connectors that has no
						// parent
						// extent
						Rectangle r = new Rectangle(Math.min(extent[2],
								extent[0]) + origin[0], Math.min(extent[3],
								extent[1]) + origin[1], Math.abs(extent[2]
								- extent[0]), Math.abs(extent[3] - extent[1]));

						connItem.setBounds(r);
					}
				}

				if (boundsSet) {
					continue;
				}

				if (connItem instanceof ConnectorItem.Input) {
					if (connItem.getState().equals(State.A)) {
						connItem.setX(0);
						connItem.setY(yoff + in * 10);
					} else if (connItem.getState().equals(State.D)) {
						connItem.setX(xoff + in * 10);
						connItem.setY(getHeight() - 15);
					} else if (connItem.getState().equals(State.F)) {
						connItem.setX(getWidth() - 15);
						connItem.setY(yoff + in * 10);
					} else if (connItem.getState().equals(State.E)) {
						connItem.setX(xoff + in * 10);
						connItem.setY(0);
					}
					in++;
				} else if (connItem instanceof ConnectorItem.Output) {
					if (connItem.getState().equals(State.A)) {
						connItem.setX(getWidth() - 15);
						connItem.setY(yoff + out * 10);
					} else if (connItem.getState().equals(State.D)) {
						connItem.setX(getWidth() / 2);
						connItem.setX(xoff + out * 10);
						connItem.setY(0);
					} else if (connItem.getState().equals(State.F)) {
						connItem.setX(0);
						connItem.setY(yoff + out * 10);
					} else if (connItem.getState().equals(State.E)) {
						connItem.setX(xoff + out * 10);
						connItem.setY(getHeight() - 15);
					}
					out++;
				} else if (connItem instanceof ConnectorItem.Unknown) {
					if (connItem.getState().equals(State.A)) {
						connItem.setX(xoff + un * 10);
						connItem.setY(0);
					} else if (connItem.getState().equals(State.D)) {
						connItem.setX(0);
						connItem.setY(yoff + un * 10);
					} else if (connItem.getState().equals(State.F)) {
						connItem.setX(xoff + un * 10);
						connItem.setY(getHeight() - 10);
					} else if (connItem.getState().equals(State.E)) {
						connItem.setX(getWidth() - 10);
						connItem.setY(yoff + un * 10);
					}
					un++;
				}
				connItem.setState(this.getState(), this.getExState());
			}
		}
	}

	public String getName() {
		// ParameterProperty pp = getpp(Constants.DECL_NAME);
		// if (pp.getPropertyValue() != null) {
		// return pp.getPropertyValue().getValue();
		// }
		// return pp.getValue();
		// return pp.getName();
		return name;
	}

	public void notifyAdd(Canvas canvas) {
		super.notifyAdd(canvas);
		DiagramCanvas cc = (DiagramCanvas) canvas;
		// actions.put("StepInsideAction", cc.getAction("StepInsideAction"));
		// actions.put("EncapsulateAction", cc.getAction("EncapsulateAction"));
		// actions.put("ExtractAction", cc.getAction("ExtractAction"));
		if (connItems != null) {
			for (ConnectorItem connItem : connItems) {
				connItem.notifyAdd(this);
			}
		}
	}

	/**
	 * @return the components
	 */
	public List<ComponentCanvasItem> getCompItems() {
		if (compItems != null) {
			return compItems;
		}
		compItems = new ArrayList<ComponentCanvasItem>();

		for (Component comp : component.getComponents()) {
			ComponentCanvasItem cci = new ComponentCanvasItem(dc, comp,
					comp.name.getValue(), comp.getCompData()
							.getDeclarationName());

			compItems.add(cci);
		}
		return compItems;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public void drawSelectionBox(Graphics2D g2, CanvasItem item, int resize) {
		Rectangle r = new Rectangle();
		item.getNormBounds(r);

		r.x -= HANDLE_SIZE;
		r.y -= HANDLE_SIZE;
		r.width += HANDLE_SIZE;
		r.height += HANDLE_SIZE;

		if (item.isResizable()) {

			g2.setColor(Color.LIGHT_GRAY);
			g2.drawRect(r.x + HANDLE_SIZE / 2, r.y + HANDLE_SIZE / 2, r.width,
					r.height);
			// NW
			g2.setColor(resize == NW ? Color.BLACK : Color.WHITE);
			g2.fillRect(r.x, r.y, HANDLE_SIZE, HANDLE_SIZE);
			// NE
			g2.setColor(resize == NE ? Color.BLACK : Color.WHITE);
			g2.fillRect(r.x + r.width, r.y, HANDLE_SIZE, HANDLE_SIZE);
			// SW
			g2.setColor(resize == SW ? Color.BLACK : Color.WHITE);
			g2.fillRect(r.x, r.y + r.height, HANDLE_SIZE, HANDLE_SIZE);
			// SE
			g2.setColor(resize == SE ? Color.BLACK : Color.WHITE);
			g2.fillRect(r.x + r.width, r.y + r.height, HANDLE_SIZE, HANDLE_SIZE);
			if (!preserveAspectRatio) {
				// N
				g2.setColor(resize == N ? Color.BLACK : Color.WHITE);
				g2.fillRect(r.x + r.width / 2, r.y, HANDLE_SIZE, HANDLE_SIZE);
				// E
				g2.setColor(resize == E ? Color.BLACK : Color.WHITE);
				g2.fillRect(r.x + r.width, r.y + r.height / 2, HANDLE_SIZE,
						HANDLE_SIZE);
				// S
				g2.setColor(resize == S ? Color.BLACK : Color.WHITE);
				g2.fillRect(r.x + r.width / 2, r.y + r.height, HANDLE_SIZE,
						HANDLE_SIZE);
				// W
				g2.setColor(resize == W ? Color.BLACK : Color.WHITE);
				g2.fillRect(r.x, r.y + r.height / 2, HANDLE_SIZE, HANDLE_SIZE);
			}

			g2.setColor(Color.BLACK);
			// NW
			g2.drawRect(r.x, r.y, HANDLE_SIZE, HANDLE_SIZE);
			// NE
			g2.drawRect(r.x + r.width, r.y, HANDLE_SIZE, HANDLE_SIZE);
			// SW
			g2.drawRect(r.x, r.y + r.height, HANDLE_SIZE, HANDLE_SIZE);
			// SE
			g2.drawRect(r.x + r.width, r.y + r.height, HANDLE_SIZE, HANDLE_SIZE);
			if (!preserveAspectRatio) {
				// N
				g2.drawRect(r.x + r.width / 2, r.y, HANDLE_SIZE, HANDLE_SIZE);
				// E
				g2.drawRect(r.x + r.width, r.y + r.height / 2, HANDLE_SIZE,
						HANDLE_SIZE);
				// S
				g2.drawRect(r.x + r.width / 2, r.y + r.height, HANDLE_SIZE,
						HANDLE_SIZE);
				// W
				g2.drawRect(r.x, r.y + r.height / 2, HANDLE_SIZE, HANDLE_SIZE);
			}
		} else {
			// draw the selection bouding box here.
			g2.setColor(Color.BLACK);
			g2.drawRect(r.x + HANDLE_SIZE / 2, r.y + HANDLE_SIZE / 2, r.width,
					r.height);
		}
	}

	public void paint(Graphics2D g2) {
		// drawTime = MiscUtils.getTimeStampMilli();
		super.paint(g2);
		g2.saveTransform();
		Rectangle2D.Double bounds = getBounds();
		g2.translate(bounds.x, bounds.y);
		if (!GraphicUtils.isConnectorComp(component)) {
			// paint icon
			g2.saveTransform();
			flip(g2);
			g2.rotate(-getAngleRadian(), icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
			icon.setThickness(3);// @sam workout for the stroke intensity
			icon.paint(g2);
			g2.restoreTransform();

			// paint text
			g2.saveTransform();
			rotate_flipLabel(g2);
			icon.paintText(g2, this, new Font(Font.VERDANA, Font.PLAIN, 40));
			g2.restoreTransform();
		}
		// paintString = getName()+" It takes " + (MiscUtils.getTimeStampMilli()
		// - drawTime)
		// + "ms to icon<br/>";
		// drawTime = MiscUtils.getTimeStampMilli();
		// paint connectorItem
		double[] scalesize = icon.getconnScale();
		for (ConnectorItem c : connItems) {
			bounds = c.getBounds();
			g2.saveTransform();
			g2.translate(bounds.x, bounds.y);
			// System.out.println(c.name+ bounds.x+",  "+bounds.y);
			c.setScalesize(scalesize);
			c.paint(g2);
			g2.restoreTransform();
		}
		// paintString += getName()+" It takes " +
		// (MiscUtils.getTimeStampMilli() - drawTime)
		// + "ms to connector<br/>";
		// drawTime = MiscUtils.getTimeStampMilli();
		// highlight the connector around the mouse
		if (aroundCon != null) {
			Color c = g2.getColor();
			g2.setColor(Color.GREEN);
			g2.fill(aroundCon);
			g2.setColor(c);
		}
		g2.restoreTransform();
		// paintString += name+"It takes " + (MiscUtils.getTimeStampMilli() -
		// drawTime)
		// + "ms to around con<br/>";
		// ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(Message.ERROR,
		// paintString));
	}

	/**
	 * @author Maryam This is the place to define how to traverse from rotate
	 *         states to flip states in the middle. The state should be defined
	 *         based on normal state (A)
	 */
	private void flip(Graphics2D g2) {
		if (state.equals(Item.State.A)) {
		} else if (state.equals((Item.State.B))) {
			g2.scale(-1, 1);
			g2.translate(-icon.getWidth(), 0);
		} else if (state.equals((Item.State.C))) {
			g2.scale(1, -1);
			g2.translate(0, -icon.getHeight());
		} else if (state.equals((Item.State.H))) {// A-->C-->rotate 90 CW
			g2.scale(1, -1);
			g2.translate(0, -icon.getHeight());
			g2.rotate(Math.PI * 3 / 2, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
		} else if (state.equals((Item.State.G))) {// A-->C-->rotate 90 CCW
			g2.scale(1, -1);
			g2.translate(0, -icon.getHeight());
			g2.rotate(Math.PI / 2, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
		}
	}

	private void rotate_flipLabel(Graphics2D g2) {

		switch (state) {
		case A:
			break;
		case B:
			break;
		case C:
			g2.scale(1, -1);
			g2.translate(0, -icon.getHeight());
			break;
		case D:
			g2.rotate(-Math.PI / 2, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
			break;
		case E:
			g2.rotate(-Math.PI * 3 / 2, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
			break;
		case F:
			g2.rotate(-Math.PI, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
			break;
		case G:
			g2.rotate(Math.PI / 2, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
			break;
		case H:
			g2.rotate(Math.PI * 3 / 2, icon.getX() + icon.getIconWidth() / 2,
					icon.getY() + icon.getIconHeight() / 2);
			break;
		default:
			break;
		}
	}

	@Override
	public CanvasItem copy() {
		return null;
	}

	private Rectangle aroundCon;

	private StringProperty originMod;

	private StringProperty extentMod;

	private StringProperty rotationMod;

	private ComponentCanvasItem hitItem, _hitItem;

	public ParameterProperty getpp(String name) {
		if (component != null) {
			for (ParameterProperty pp : component.getParameterList()) {
				if (pp.getName().equals(name)) {
					return pp;
				}
			}
		}
		return null;
	}

	public boolean isHitItem(double x, double y) {
		Canvas ca = getCanvas();
		_hitItem = null;
		ComponentCanvasItem cci = null;
		Rectangle2D rect;
		Point2D p;

		cci = (ComponentCanvasItem) this;
		rect = cci.getBounds();

		p = new Point2D.Double(x, y);

		if (rect.contains(p)) {
			_hitItem = cci;
		}

		if (hitItem != _hitItem) {
			hitItem = _hitItem;
		}

		if (hitItem != null) {
			ca.setSelectedItem(cci);
			return true;
		} else
			return false;
	}

	public void clearConnectorCover() {
		aroundCon = null;
	}

	/**
	 * @author Maryam
	 * @function if the mouse is around connectorItem
	 */
	public boolean isAroundConnItem(double x, double y) {

		Canvas ca = getCanvas();
		if (ca == null) {
			return false;
		}

		if (connItems == null)
			return false;

		for (ConnectorItem c : connItems) {
			if (isInRange(y, c.getY() + (int) bounds.getY(), c.getHeight())
					&& isInRange(x, (int) bounds.getX() + c.getX(),
							c.getWidth())) {

				aroundCon = c.getBoundsRect();
				// TODO after rotation, still shows
				// the normal location
				return true;
			} else {
				aroundCon = null;
			}
		}
		return false;
	}

	public boolean isHitConnItem(double x, double y) {
		_hitConnItem = null;

		for (ConnectorItem c : connItems) {
			if (isInRange(y, c.getY() + bounds.getY(), c.getHeight())
					&& isInRange(x, c.getX() + bounds.getX(), c.getWidth())) {
				_hitConnItem = c;
				break;
			}
		}

		if (hitConnItem != _hitConnItem) {
			hitConnItem = _hitConnItem;
			// repaint();
		}

		if (hitConnItem != null) {
			DiagramCanvas cc = (DiagramCanvas) getCanvas();
			cc.connectorSelected(hitConnItem);
			return true;
		} else
			return false;
	}

	public Component getComponent() {
		return component;
	}

	public ConnectorItem[] getConnectorItems() {
		return connItems.toArray(new ConnectorItem[0]);
	}

	public int getConnectorItemIndex(ConnectorItem connItem) {
		for (int i = 0; i < connItems.size(); i++) {
			if (connItems.get(i) == connItem) {
				return i;
			}
		}
		return -1;
	}

	public ConnectorItem getConnectorItem(ConnectorItem item, String name) {
		for (ConnectorItem ci : connItems) {
			if (((String) ci.getConnector().name).equals((String) name)) {
				return ci;
			}
		}
		// no correct connector has been found
		// special case expanable connector.
		if (component.restriction.equals(Constants.TYPE_EXPANDABLECONNECTOR)
				&& item != null) {
			// @@sam@@set the connector position to the middle of cci.
			Rectangle2D.Double rect = this.bounds;

			ConnectorItem connItem = ConnectorItem.Factory.create(
					new Connector(name, null, null, ""), dc);
			connItem.notifyAdd(this);
			connItem.setX(rect.width / 2);
			connItem.setY(rect.height / 2);
			connItems.add(connItem);
			return connItem;
		}

		return null;
	}

	// @sam why this method should only be called via CanvasUtils
	public void setState(State state, State ex_state) {
		super.setState(state, ex_state);

		// for (ConnectorItem item : getConnectorItems()) {
		// item.setState(state, ex_state, item);
		// }
		// Sam: it updates codecanvas
		Rectangle2D.Double b = ComponentCanvasItem.this.getBounds();
		if (originMod != null)
			originMod.setValue("{" + b.x + ", " + b.y + "}");
		if (rotationMod != null)
			rotationMod.setValue(getRotation() + "");
	}

	public void initElement(Element element) {
		ComponentClause compClause = element.compClause;
		Annotation annotation = compClause.declList.get(0).comment.annotation;
		ComponentDecl decl = compClause.declList.get(0);

		// #####element
		if (element.redeclareProperty != null) {
		}
		if (element.finalProperty != null)
			this.finalProperty = element.finalProperty;
		if (element.typingProperty != null)
			this.typingProperty = element.typingProperty;
		if (element.replaceableProperty != null)
			this.replaceableProperty = element.replaceableProperty;
		if (element.protectedProperty != null)
			this.protectedProperty = element.protectedProperty;

		// ####compClause
		if (compClause.variability != null)
			this.varibilityProperty = compClause.variability;
		if (compClause.causality != null)
			this.causalityProperty = compClause.causality;

		// set the default value when loading
		String initProtected_ = component.compData.getProtected_();
		element.protectedProperty.setValue(component.compData.getProtected_());
		element.finalProperty.setValue(component.compData.getFinal_());
		element.typingProperty.setValue(component.compData.getTyping());
		compClause.variability.setValue(component.compData.getVariability());
		compClause.causality.setValue(component.compData.getCausality());

		if (decl.varName != null) {
		}

		for (Argument arg : decl.modification.arguments) {
			ParameterProperty pp = getpp(arg.name);
			if (arg.modification != null) {
				List<Argument> innerArgs = arg.modification.arguments;
				if (innerArgs != null) {
					for (Argument a : innerArgs) {
						if (a.name.equals("start")) {
							OMModification innerM = a.modification == null ? null
									: a.modification.getObjectModel();
							if (innerM != null && pp.getStart() != null) {
								// System.err.println(arg.name);
								// pp.setPropertyStart(MiscUtils
								// .getModification(new Modification(
								// innerM)));
								// // set default value first
								// pp.getPropertyStart().setValue(pp.getStart());
							}
						}
					}
				}
			}
			OMModification argM = arg.modification == null ? null
					: arg.modification.getObjectModel();
			if (argM != null && pp.isHeader()) {
				// System.err.println(arg.name);
				// pp.setPropertyValue(MiscUtils.getModification(new
				// Modification(
				// argM)));
				// // set default value first
				// if (pp.getValue() != null && pp.getValue() != "") {
				// pp.getPropertyValue().setValue(pp.getValue());
				// }
			}
		}

		// decl.modification
		OMModification originM = annotation == null ? null
				: (OMModification) annotation.map
						.get("Placement.transformation.origin");
		// should be updated when resized, and fliped
		OMModification extentM = annotation == null ? null
				: (OMModification) annotation.map
						.get("Placement.transformation.extent");
		OMModification rotationM = annotation == null ? null
				: (OMModification) annotation.map
						.get("Placement.transformation.rotation");

		if (originM != null)
			originMod = MiscUtils.getModification(new Modification(originM));
		if (extentM != null)
			extentMod = MiscUtils.getModification(new Modification(extentM));
		if (rotationM != null)
			rotationMod = MiscUtils
					.getModification(new Modification(rotationM));

		this.addMouseEventHandler(new MouseEventHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				super.onMouseUp(event);
				updateCodeCanvas();
				ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
			}
		});
	}

	@Override
	public void updateCodeCanvas() {
		// if item itself is an connectorItem: self Connector is handled in the
		// below case
		// else if has conItems
		ConnectorItem[] conItems = getConnectorItems();
		if (conItems != null && conItems.length > 0) {
			for (ConnectorItem conItem : conItems) {
				ConnectionItem[] connectionItems = canvas
						.getConnections(conItem);
				if (connectionItems != null && connectionItems.length > 0) {
					for (ConnectionItem connectionItem : connectionItems) {
						connectionItem.updateCodeCanvas();
					}
				}
			}
		}
		// canvas.getconn
		Rectangle2D.Double b = getCodeCanvasBounds();
		if (originMod != null)
			originMod.setValue("{" + b.x + ", " + b.y + "}");
		if (rotationMod != null)
			rotationMod.setValue(getRotation() + "");
		if (extentMod != null) {
			Rectangle2D.Double normBound = getBounds();
			int[] esign = getExtentSign();
			int extentW = (int) (normBound.width / 2);
			int extentH = (int) (normBound.height / 2);
			extentMod.setValue("{{" + extentW * esign[0] + "," + extentH
					* esign[1] + "},{" + extentW * esign[2] + ", " + extentH
					* esign[3] + "}}");
		}
	}

	public Rectangle2D.Double getCodeCanvasBounds() {
		Rectangle2D.Double r = new Rectangle2D.Double();

		r.width = bounds.width;
		r.height = bounds.height;
		Point c = new Point(Constant.diagramCanvasSize.width / 2,
				Constant.diagramCanvasSize.height / 2);
		// corresponds to DiagramCanvas loadClassDef Section
		r.x = (bounds.x - c.x + bounds.width / 2);
		r.y = -(bounds.y - c.y + bounds.height / 2);

		return r;
	}

	public Icon getDiagram() {
		return diagram;
	}

	public void setParaModifier(String s) {
		// System.err.println("para modification: " + s);
		dc.setParameterModification(this, s);
	}

	public void change2Protected() {
		dc.chang2Protected(this);
	}

	public void change2Public() {
		dc.change2Public(this);
	}
}

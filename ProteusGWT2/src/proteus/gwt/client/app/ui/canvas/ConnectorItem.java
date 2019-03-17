package proteus.gwt.client.app.ui.canvas;

import java.util.Arrays;
import java.util.List;

import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.Connector;

public abstract class ConnectorItem extends Item {
	public static class Factory {
		public static ConnectorItem create(Connector conn, Graphics2D g2) {
			String type = (String) conn.name;
			if (knownInputTypes.contains(type)) {
				return new Input(conn, g2);
			} else if (knownOutputTypes.contains(type)) {
				return new Output(conn, g2);
			}
			return new Unknown(conn, g2);
		}
	}

	public static class Input extends ConnectorItem {
		private static final Color overColor = Color.red,
				normColor = Color.blue;// overColor is used for the conItems

		// above the item )if available)
		private Point2D.Double hitPoint = new Point2D.Double();
		private boolean hit;

		private Icon icon;

		public Input(Connector conn, Graphics2D g2) {
			super(conn);
			icon = conn.getIcon(g2);
		}

		/**
		 * @author Maryam This is the place to define how to traverse from
		 *         rotate states to flip states in the middle. The state should
		 *         be defined based on normal state (A)
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
				g2.rotate(Math.PI * 3 / 2, icon.getX() + icon.getIconWidth()
						/ 2, icon.getY() + icon.getIconHeight() / 2);
			} else if (state.equals((Item.State.G))) {// A-->C-->rotate 90 CCW
				g2.scale(1, -1);
				g2.translate(0, -icon.getHeight());
				g2.rotate(Math.PI / 2, icon.getX() + icon.getIconWidth() / 2,
						icon.getY() + icon.getIconHeight() / 2);
			}
		}

		public boolean hit(int x, int y) {
			hit = connRect.contains(x, y);
			return hit;
		}

		public void paint(Graphics2D g2) {
			if (icon != null) {
				System.out.println("input icon != null");
				g2.saveTransform();
				icon.paint(g2);
				g2.restoreTransform();
			} else {
				g2.setColor(Color.gray);
				if (state.equals(State.A) || state.equals(State.F)) {// (state.equals(ANGLE_0)
					g2.fillRect(0, 4, 15, 2);
				} else {
					g2.fillRect(4, 0, 2, 15);
				}

				g2.setColor(hit ? overColor : normColor);
				connRect.width = connRect.height = 6;// width and height of all
				// connItems are 6
				// pixels

				if (state.equals(State.A)) {// (state.equals(ANGLE_0)) {
					connRect.x = 0;
					connRect.y = 2;
				} else if (state.equals(State.D)) {// (state.equals(ANGLE_90)) {
					connRect.x = 2;
					connRect.y = 9;
				} else if (state.equals(State.F)) {// (state.equals(ANGLE_180))
					// {
					connRect.x = 9;
					connRect.y = 2;
				} else if (state.equals(State.E)) {// (state.equals(ANGLE_270))
					// {
					connRect.x = 2;
					connRect.y = 0;
				}

				hitPoint.x = connRect.x;
				hitPoint.y = connRect.y;

				g2.fillRect(connRect.x, connRect.y, connRect.width,
						connRect.height);
			}
		}

		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			connRect.width = width;
			connRect.height = height;

			hitPoint.x = 0;
			hitPoint.y = 0;
		}
	}

	public static class Output extends ConnectorItem {
		private static final Color overColor = Color.red,
				normColor = Color.blue;

		private Rectangle connRect = new Rectangle();

		private boolean hit;

		private Point hitPoint = new Point();

		private Icon icon;

		public Output(Connector conn, Graphics2D g2) {
			super(conn);
			setSize(15, 10);
			icon = conn.getIcon(g2);
		}

		/**
		 * @author Maryam This is the place to define how to traverse from
		 *         rotate states to flip states in the middle. The state should
		 *         be defined based on normal state (A)
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
				g2.rotate(Math.PI * 3 / 2, icon.getX() + icon.getIconWidth()
						/ 2, icon.getY() + icon.getIconHeight() / 2);
			} else if (state.equals((Item.State.G))) {// A-->C-->rotate 90 CCW
				g2.scale(1, -1);
				g2.translate(0, -icon.getHeight());
				g2.rotate(Math.PI / 2, icon.getX() + icon.getIconWidth() / 2,
						icon.getY() + icon.getIconHeight() / 2);
			}
		}

		public boolean hit(int x, int y) {
			hit = connRect.contains(x, y);
			return hit;
		}

		public void paint(Graphics2D g2) {
			if (icon != null) {
				System.out.println("output icon != null");
				g2.saveTransform();
				icon.paint(g2);
				g2.restoreTransform();
			} else {
				g2.setColor(Color.gray);
				if (state.equals(State.A) || state.equals(State.F)) {// (state.equals(ANGLE_0)
					// ||
					// state.equals(ANGLE_180))
					// {
					g2.fillRect(0, 4, 15, 2);
				} else {
					g2.fillRect(4, 0, 2, 15);
				}
				g2.setColor(hit ? overColor : normColor);
				connRect.width = connRect.height = 6;

				if (state.equals(State.A)) {// (state.equals(ANGLE_0)) {
					connRect.x = 9;
					connRect.y = 2;
				} else if (state.equals(State.D)) {// (state.equals(ANGLE_90)) {
					connRect.x = 2;
					connRect.y = 0;
				} else if (state.equals(State.F)) {// (state.equals(ANGLE_180))
					// {
					connRect.x = 0;
					connRect.y = 2;
				} else if (state.equals(State.E)) {// (state.equals(ANGLE_270))
					// {
					connRect.x = 2;
					connRect.y = 9;
				}

				hitPoint.x = connRect.x + connRect.width / 2;
				hitPoint.y = connRect.y + connRect.height / 2;
				g2.fillRect(connRect.x, connRect.y, connRect.width,
						connRect.height);
			}
		}

		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			connRect.width = width;
			connRect.height = height;
			hitPoint.x = width / 2;
			hitPoint.y = height / 2;
		}
	}

	public static class Unknown extends ConnectorItem {
		private static final Color overColor = Color.red,
				normColor = Color.gray;

		private boolean hit;

		private Point2D.Double hitPoint = new Point2D.Double();
		private Icon icon;

		public Unknown(Connector conn, Graphics2D g2) {
			super(conn);
			setSize(10, 15);
			icon = conn.getIcon(g2);
		}

		public Point2D.Double getHitPoint() {
			return new Point2D.Double(getHitX(), getHitY());
		}

		public double getHitX() {
			return hitPoint.x;
		}

		public double getHitY() {
			return hitPoint.y;
		}

		public boolean hit(int x, int y) {
			hit = connRect.contains(x, y);
			return hit;
		}

		public void setScalesize(double[] scalesize) {
			this.scaleSize = scalesize;
			if (connector != null && icon != null) {
				int[] extent = connector.getExtent();
				double width = Math.abs(extent[2] - extent[0]);
				double height = Math.abs(extent[3] - extent[1]);

				// magic number 0.33 ... 66/200 = 0.33;
				double magicN = Icon.ModelicaImpl.defaultSize/200.0;
				int xcon = (int) (width * (scaleSize[0] / magicN));
				int ycon = (int) (height * (scaleSize[1] / magicN));

				icon.setConnCoordis(new int[] { 0, 0, xcon, ycon });
			}
		}

		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			connRect.width = width;
			connRect.height = height;
			hitPoint.x = width / 2;
			hitPoint.y = height / 2;
		}

		public void setBounds(double x, double y, double width, double height) {
			super.setBounds(x, y, width, height);
			connRect.width = width;
			connRect.height = height;
			hitPoint.x = width / 2;
			hitPoint.y = height / 2;
		}

		private void flip(double width, double height, Graphics2D g2) {
			if (state.equals(Item.State.A)) {
			} else if (state.equals((Item.State.B))) {
				g2.scale(-1, 1);
				g2.translate(-width, 0);
			} else if (state.equals((Item.State.C))) {
				g2.scale(1, -1);
				g2.translate(0, -height);
			} else if (state.equals((Item.State.H))) {// A-->C-->rotate 90 CW
				g2.scale(1, -1);
				g2.translate(0, -height);
				g2.rotate(Math.PI * 3 / 2, width / 2.0, height / 2.0);
			} else if (state.equals((Item.State.G))) {// A-->C-->rotate 90 CCW
				g2.scale(1, -1);
				g2.translate(0, -height);
				g2.rotate(Math.PI / 2, width / 2.0, height / 2.0);
			}
		}

		/**
		 * @author Maryam This is the place to define how to traverse from
		 *         rotate states to flip states in the middle. The state should
		 *         be defined based on normal state (A)
		 */
		private void flip(Graphics2D g2, Icon icon) {
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
				g2.rotate(Math.PI * 3 / 2, icon.getX() + icon.getIconWidth()
						/ 2, icon.getY() + icon.getIconHeight() / 2);
			} else if (state.equals((Item.State.G))) {// A-->C-->rotate 90 CCW
				g2.scale(1, -1);
				g2.translate(0, -icon.getHeight());
				g2.rotate(Math.PI / 2, icon.getX() + icon.getIconWidth() / 2,
						icon.getY() + icon.getIconHeight() / 2);
			}
		}

		public void paint(Graphics2D g2) {
			if (icon != null) {
				double width = bounds.width;
				double height = bounds.height;
				flip(width, height, g2);
				g2.rotate(-getAngleRadian(), width / 2, height / 2);
				icon.paint(g2);
			} else {
				g2.setColor(Color.gray);
				if (state.equals(State.A) || state.equals(State.F)) {
					g2.fillRect(4, 0, 2, 15);
				} else {
					g2.fillRect(0, 4, 15, 2);
				}

				g2.setColor(hit ? overColor : normColor);
				connRect.width = connRect.height = 6;
				if (state.equals(State.E)) {// (state.equals(ANGLE_270)) {
					connRect.x = 9;
					connRect.y = 2;
				} else if (state.equals(State.A)) {// (state.equals(ANGLE_0)) {
					connRect.x = 2;
					connRect.y = 0;
				} else if (state.equals(State.D)) {// (state.equals(ANGLE_90)) {
					connRect.x = 0;
					connRect.y = 2;
				} else if (state.equals(State.F)) {// (state.equals(ANGLE_180))
					// {
					connRect.x = 2;
					connRect.y = 9;
				}
				hitPoint.x = connRect.x + connRect.width / 2;
				hitPoint.y = connRect.y + connRect.height / 2;
				g2.fillRect(connRect.x, connRect.y, connRect.width,
						connRect.height);
			}
		}

	}

	public static List<String> knownInputTypes = Arrays.asList(new String[] {
			"RealInput", "BooleanInput", "PositivePin" });

	public static List<String> knownOutputTypes = Arrays.asList(new String[] {
			"RealOutput", "BooleanOutput", "NegativePin" });

	protected ComponentCanvasItem cci;

	protected Connector connector;

	private static Rectangle2D.Double connRect = new Rectangle2D.Double();

	protected double[] scaleSize = { Icon.ModelicaImpl.defaultSize/200.0, Icon.ModelicaImpl.defaultSize/200.0 };

	public ConnectorItem(Connector connector) {
		this.connector = connector;
	}

	/**
	 *@author Maryam
	 */
	private void flip_horizontal(int w, int h, double xc, double yc,
			Canvas canvas, Point2D[] p) {

		// System.out.println("----------------");
		// System.out.println("flip horizontal");
		// maybe leftmost point of the connected connectorItem?

		// initial part before update
		Point2D ip = null;// intermediate point
		Point2D connectionPoint = null;// could be either start or end point of
		// a connection
		boolean horizontal = false;// horizontal: when intermediate point and
		// connection point are horizontally
		// connected
		boolean vertical = false;// vertical: when intermediate point and
		// connection point are vertically connected

		ConnectionItem[] conns = canvas.getConnections(this);
		ComponentCanvasItem comp = getComponentCanvasItem();

		if (conns != null) {
			for (int i = 0; i < conns.length; i++) {
				if (conns[i].getSource().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getFirst();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
				} else if (conns[i].getDestination().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getLast();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
				}

				//determin whether it is vertical connected or horizontal connected
				if (ip != null) {
					if ((int) connectionPoint.getX() == (int) ip.getX()) {
						vertical = true;
					} else if (((int) connectionPoint.getY() == (int) ip.getY())
							&& !vertical) {
						horizontal = true;
					}
				} else {
					Point2D start_point = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
					Point2D end_point = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
					if ((int) start_point.getX() == (int) end_point.getX())
						vertical = true;
					else if ((int) start_point.getY() == (int) end_point.getY()
							&& !vertical)
						horizontal = true;
				}
			}
		}

		
		double x = bounds.x;
		double y = bounds.y;
		// figure out the position
		double cw = getWidth();

		if (xc < w / 2) {// left
			x = w - x - cw;
			setX((int) x);
			setY((int) y);
			set_bounds(new Rectangle((int) x, (int) y, (int) bounds.height,
					(int) bounds.width));

			update(conns, comp, connectionPoint, ip, horizontal, vertical);
			if (conns != null)
				for (int i = 0; i < conns.length; i++) {
					if (conns[i] != null)
						canvas.updateIntermediatePointsAfterRotate_Flip(
								conns[i], p[i], w - 2 * x - cw, 0);// uncomment
					// new
				}
		} else if (xc > w / 2) {// right
			x = w - x - cw;
			setX((int) x);
			setY((int) y);
			set_bounds(new Rectangle((int) x, (int) y, (int) bounds.height,
					(int) bounds.width));

			update(conns, comp, connectionPoint, ip, horizontal, vertical);
			if (conns != null)
				for (int i = 0; i < conns.length; i++) {
					if (conns[i] != null)
						canvas.updateIntermediatePointsAfterRotate_Flip(
								conns[i], p[i], w - 2 * x - cw, 0);// uncomment
					// new
				}
		} else if (x == w / 2) {
		}
		// }
	}

	public double getHitY() {
		return 0;
	}

	public double getHitX() {
		return 0;
	}

	/**
	 *@author Maryam
	 */
	private void flip_vertical(int w, int h, double xc, double yc,
			Canvas canvas, Point2D[] p) {

		// initial part before update

		Point2D ip = null;// intermediate point
		Point2D connectionPoint = null;// could be either start or end point of
		// a connection
		boolean horizontal = false;// horizontal: when intermediate point and
		// connection point are horizontally
		// connected
		boolean vertical = false;// vertical: when intermediate point and
		// connection point are vertically connected

		ConnectionItem[] conns = canvas.getConnections(this);
		ComponentCanvasItem comp = getComponentCanvasItem();

		if (conns != null) {
			for (int i = 0; i < conns.length; i++) {
				if (conns[i].getSource().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getFirst();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
				} else if (conns[i].getDestination().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getLast();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
				}

				if (ip != null) {
					if ((int) connectionPoint.getX() == (int) ip.getX())
						vertical = true;// start and first intermediate points
					// are conns[i]ected vertically before
					// update
					else if (((int) connectionPoint.getY() == (int) ip.getY())
							&& !vertical)
						horizontal = true;// start and first intermediate points
					// are conns[i]ected horizontally
					// before update
				} else {
					Point2D start_point = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
					Point2D end_point = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
					if ((int) start_point.getX() == (int) end_point.getX())
						vertical = true;
					else if ((int) start_point.getY() == (int) end_point.getY()
							&& !vertical)
						horizontal = true;
				}
			}
		}

		// left most point of connected connectorItem maybe!
		double x = bounds.x;
		double y = bounds.y;
		double ch = getHeight();

		// figure out the position
		if (yc < h / 2) {
			y = h - y - ch;
			setX((int) x);
			setY((int) y);
			set_bounds(new Rectangle((int) x, (int) y, (int) bounds.height,
					(int) bounds.width));

			update(conns, comp, connectionPoint, ip, horizontal, vertical);
			if (conns != null)
				for (int i = 0; i < conns.length; i++) {
					if (conns[i] != null)
						canvas.updateIntermediatePointsAfterRotate_Flip(
								conns[i], p[i], 0, -(h - 2 * y - ch));// uncomment
					// new
				}
		} else {
			y = h - y - ch;
			setX(x);
			setY(y);
			set_bounds(new Rectangle((int) x, (int) y, (int) bounds.height,
					(int) bounds.width));

			update(conns, comp, connectionPoint, ip, horizontal, vertical);
			if (conns != null)
				for (int i = 0; i < conns.length; i++) {
					if (conns[i] != null)
						canvas.updateIntermediatePointsAfterRotate_Flip(
								conns[i], p[i], 0, -(h - 2 * y - ch));// uncomment
					// new
				}
		}
	}

	public ComponentCanvasItem getComponentCanvasItem() {
		return cci;
	}

	public Connector getConnector() {
		return connector;
	}

	public abstract boolean hit(int x, int y);

	public void notifyAdd(ComponentCanvasItem compCanvasItem) {
		this.cci = compCanvasItem;
		this.name = compCanvasItem.getName() + "."
				+ (connector.prefix != null ? connector.prefix : "")
				+ (String) connector.name;
		// 13 Jan ,2011 disable the tooltip at the moment.
		// setToolTip(new PropertiesWindow(getComponentCanvasItem().getCanvas(),
		// name, this));
	}

	public void notifyRemove(ComponentCanvasItem compCanvasItem) {
		this.cci = null;
	}

	public abstract void paint(Graphics2D g);

	/**
	 *@author Maryam
	 */
	private void rotate_ccw(int w, int h, Canvas canvas, Point2D[] p) {

		// initial part before update

		Point2D ip = null;// intermediate point
		Point2D connectionPoint = null;// could be either start or end point of
		// a connection
		boolean horizontal = false;// horizontal: when intermediate point and
		// connection point are horizontally
		// connected
		boolean vertical = false;// vertical: when intermediate point and
		// connection point are vertically connected

		ConnectionItem[] conns = canvas.getConnections(this);
		ComponentCanvasItem comp = getComponentCanvasItem();

		if (conns != null) {

			for (int i = 0; i < conns.length; i++) {
				if (conns[i].getSource().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getFirst();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
				} else if (conns[i].getDestination().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getLast();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
				}

				if (ip != null) {
					if ((int) connectionPoint.getX() == (int) ip.getX())
						vertical = true;// start and first intermediate points
					// are connected vertically before
					// update
					else if (((int) connectionPoint.getY() == (int) ip.getY())
							&& !vertical)
						horizontal = true;// start and first intermediate points
					// are connected horizontally before
					// update
				} else {
					Point2D start_point = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
					Point2D end_point = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
					if ((int) start_point.getX() == (int) end_point.getX())
						vertical = true;
					else if ((int) start_point.getY() == (int) end_point.getY()
							&& !vertical)
						horizontal = true;
				}
			}
		}

		// figure out the position of the pin
		// coordinates of left most point of connected connectorItem
		double x = bounds.x;
		double y = bounds.y;
		double ch = getHeight();

		double tmpx = x;
		double tmpy = y;
		// x = tmpy;
		// y = w - tmpx - ch; original code
		// sam edit here

		x = (y + getHeight() / 2.0 - h / 2.0) - getHeight() / 2.0 + w / 2.0;
		y = -(tmpx + getWidth() / 2.0 - w / 2.0) - getWidth() / 2.0 + h / 2.0;

		setBounds(x, y, bounds.height, bounds.width);
		set_bounds(new Rectangle((int) x, (int) y, (int) bounds.height,
				(int) bounds.width));
		// updateNormBounds(x, y, normBounds.height, normBounds.width);//remove
		// later, extra already available in setBounds()

		update(conns, comp, connectionPoint, ip, horizontal, vertical);
		if (conns != null) {
			int dx, dy;
			dx = (int) (tmpy - tmpx);
			dy = (int) (w - tmpx - ch - tmpy);
			for (int i = 0; i < conns.length; i++) {
				if (conns[i] != null)
					canvas.updateIntermediatePointsAfterRotate_Flip(conns[i],
							p[i],  (tmpy - tmpx), (w - tmpx - ch - tmpy));
			}
		}
	}

	/**
	 *@author Maryam
	 */
	private void rotate_cw(int w, int h, Canvas canvas, Point2D[] p) {

		// System.out.println("rotate_cw");

		// --------------------------------initial part before update

		Point2D ip = null;// intermediate point
		Point2D connectionPoint = null;// could be either start or end point of
		// a connection
		boolean horizontal = false;// horizontal: when intermediate point and
		// connection point are horizontally
		// connected
		boolean vertical = false;// vertical: when intermediate point and
		// connection point are vertically connected

		ConnectionItem[] conns = canvas.getConnections(this);
		ComponentCanvasItem comp = getComponentCanvasItem();

		if (conns != null) {
			for (int i = 0; i < conns.length; i++) {
				if (conns[i].getSource().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getFirst();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());

				} else if (conns[i].getDestination().equals(this)) {
					if (conns[i].getCornerPoints() != null
							&& conns[i].getCornerPoints().size() != 0)
						ip = conns[i].getCornerPoints().getLast();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
				}

				if (ip != null) {
					if ((int) connectionPoint.getX() == (int) ip.getX())
						vertical = true;// start and first intermediate points
					// are conns[i]ected vertically before
					// update
					else if (((int) connectionPoint.getY() == (int) ip.getY())
							&& !vertical)
						horizontal = true;// start and first intermediate points
					// are conns[i]ected horizontally
					// before update
				} else {
					Point2D start_point = new Point2D.Double(comp.getX()
							+ conns[i].src.getX() + conns[i].src.getHitX(),
							comp.getY() + conns[i].src.getY()
									+ conns[i].src.getHitY());
					Point2D end_point = new Point2D.Double(comp.getX()
							+ conns[i].dest.getX() + conns[i].dest.getHitX(),
							comp.getY() + conns[i].dest.getY()
									+ conns[i].dest.getHitY());
					if ((int) start_point.getX() == (int) end_point.getX())
						vertical = true;
					else if ((int) start_point.getY() == (int) end_point.getY()
							&& !vertical)
						horizontal = true;
				}
			}
		}
		// maybe left most point of connected connectorItem
		double x = bounds.x;
		double y = bounds.y;

		// System.out.println("before rotate");
		// System.out.println("x, y, w, h, W, H: " + x + ", " + y + ", " +
		// getWidth() + ", " + getHeight()+", "+w +", "+h);

		// figure out the position of the pin
		double cw = getWidth();
		double tmpx = x;
		double tmpy = y;
		// x = h - y - cw;
		// y = tmpx; this is original copy
		// sam edit here add "+w-getHeight()"
		x = -(y + getHeight() / 2.0 - h / 2.0) - getHeight() / 2.0 + w / 2.0;
		y = (tmpx + getWidth() / 2.0 - w / 2.0) - getWidth() / 2.0 + h / 2.0;

		// if(w>h) System.out.println("w:h"+w+", "+h);
		// else System.err.println("w:h"+w+", "+h);
		// y = (tmpx + getWidth() / 2 - h / 2) - getHeight() / 2 + w / 2;

		setBounds(x, y, bounds.height, bounds.width);
		set_bounds(new Rectangle((int) x, (int) y, (int) bounds.height,
				(int) bounds.width));

		// System.out.println("after rotate");
		// System.out.println("x, y, w, h, W, H : " + x + ", " + y + ", " + cw +
		// ", " + getHeight()+", "+w+", "+h);
		//		
		update(conns, comp, connectionPoint, ip, horizontal, vertical);
		if (conns != null) {
			int dx, dy;
			;
			for (int i = 0; i < conns.length; i++) {
				dx = (int) (h - y - cw - tmpx);
				dy = (int) (tmpx - tmpy);
				if (conns[i] != null)
					canvas.updateIntermediatePointsAfterRotate_Flip(conns[i],
							p[i], dx, dy);
			}
		}
	}

	public void setState(State state, State ex_state, ConnectorItem ci) {
		this.state = state;
		this.ex_state = ex_state;
		updateState(state, ex_state, ci);
	}

	/**
	 *@author Maryam
	 *@function
	 */
	private void update(ConnectionItem[] conns, ComponentCanvasItem comp,
			Point2D connectionPoint, Point2D ip, boolean horizontal,
			boolean vertical) {

		// System.out.println("----update---");

		ConnectionItem conn;
		if (conns == null) {
			return;
		}

		for (int k = 0; k < conns.length; k++) {
			conn = conns[k];
			if (conn == null)
				return;
			if (conn != null && conn.name == null)
				return;

			//the code below is not executed because conn.name is null by default..
			if (conn != null) {
				if (conn.getSource().equals(this)) {
					if (conn.getCornerPoints() != null)
						ip = conn.getCornerPoints().getFirst();// added newly
					comp = conn.src.getComponentCanvasItem();
					connectionPoint = new Point2D.Double(comp.getX()
							+ conn.src.getX() + conn.src.getHitX(), comp.getY()
							+ conn.src.getY() + conn.src.getHitY());
				} else if (conn.getDestination().equals(this)) {
					if (conn.getCornerPoints() != null)
						ip = conn.getCornerPoints().getLast();
					comp = conn.dest.getComponentCanvasItem();

					connectionPoint = new Point2D.Double(comp.getX()
							+ conn.dest.getX() + conn.dest.getHitX(), comp
							.getY()
							+ conn.dest.getY() + conn.dest.getHitY());
				}

				if (ip != null) {// added newly
					if (horizontal) {
						ip.setLocation(ip.getX(), connectionPoint.getY());
						horizontal = false;
					} else if (vertical) {
						ip.setLocation(connectionPoint.getX(), ip.getY());
						vertical = false;
					}
				} else {
					Point2D start_point = new Point2D.Double(comp.getX()
							+ conn.src.getX() + conn.src.getHitX(), comp.getY()
							+ conn.src.getY() + conn.src.getHitY());
					Point2D end_point = new Point2D.Double(comp.getX()
							+ conn.dest.getX() + conn.dest.getHitX(), comp
							.getY()
							+ conn.dest.getY() + conn.dest.getHitY());
					if ((int) start_point.getX() == (int) end_point.getX())
						vertical = true;
					else if ((int) start_point.getY() == (int) end_point.getY()
							&& !vertical)
						horizontal = true;
				}
			}
		}
	}

	/*
	 * public void getFlowType() { connector.getExtent(). }
	 */
	/**
	 *@author Maryam
	 */
	private void updateState(State state, State ex_state, ConnectorItem ci) {

		// System.out.println("updateState()");
		ComponentCanvasItem comp = ci.getComponentCanvasItem();

		Canvas canvas = comp.getCanvas();
		ConnectionItem[] conItems = canvas.getConnections(ci);

		Point2D[] p;
		if (conItems != null) {
			p = new Point2D[conItems.length];
			for (int i = 0; i < conItems.length; i++) {
				if (conItems[i] != null
						&& conItems[i].getCornerPoints().size() != 0) {
					if (conItems[i].getSource().equals(ci))
						p[i] = conItems[i].getCornerPoints().getFirst();
					else if (conItems[i].getDestination().equals(ci))
						p[i] = conItems[i].getCornerPoints().getLast();
				}
			}
		} else
			p = null;

		int w = (int) comp.getBounds().getWidth();
		int h = (int) comp.getBounds().getHeight();

		// center of connector
		double xc = (bounds.x + bounds.getWidth() / 2);
		double yc = bounds.y + bounds.getHeight() / 2;

		if (ex_state.equals(State.A)) {
			if (state.equals(State.B)) {// A --> B (FLIP_HORIZONTAL)
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.C)) {// A --> C (FLIP_VERTICAL)
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.D)) {// A --> D (ROTATE_CCW)
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.E)) {// A --> E (ROTATE_CW)
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.B)) {
			if (state.equals(State.A)) {// B --> A (FLIP_HORIZONTAL)
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.F)) {// B --> F (FLIP_VERTICAL)
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.H)) {// B --> H (ROTATE_CCW)
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.G)) {// B --> G (ROTATE_CW)
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.C)) {
			if (state.equals(State.F)) {// C --> F (FLIP_HORIZONTAL)
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.A)) {// C --> A (FLIP_VERTICAL)
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.G)) {// C --> G (ROTATE_CCW)
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.H)) {// C --> H (ROTATE_CW)
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.D)) {
			if (state.equals(State.G)) {// D --> G (FLIP_HORIZONTAL)
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.H)) {// D --> H (FLIP_VERTICAL)
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.F)) {// D --> F
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.A)) {// D -->A
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.E)) {
			if (state.equals(State.H)) {// E --> H
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.G)) {// E --> G
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.A)) {// E --> A
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.F)) {// E --> F
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.F)) {
			if (state.equals(State.C)) {// F --> C (FLIP_HORIZONTAL)
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.B)) {// F --> B (FLIP_VERTICAL)
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.E)) {// F --> E
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.D)) {// F --> D
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.G)) {
			if (state.equals(State.D)) {// G --> D
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.E)) {// G --> E
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.B)) {// G --> B
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.C)) {// G --> C
				rotate_cw(w, h, canvas, p);
			}
		} else if (ex_state.equals(State.H)) {
			if (state.equals(State.E)) {// H --> E
				flip_horizontal(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.D)) {// H --> D
				flip_vertical(w, h, xc, yc, canvas, p);
			} else if (state.equals(State.C)) {// H --> C
				rotate_ccw(w, h, canvas, p);
			} else if (state.equals(State.B)) {// H --> B
				rotate_cw(w, h, canvas, p);
			}
		}
	}

	public void setScalesize(double[] scalesize) {
		this.scaleSize = scalesize;
	}

}
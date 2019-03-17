package proteus.gwt.client.app.ui.canvas;

import java.util.LinkedList;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.MouseEventHandler;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.ui.util.GraphicItem;
import proteus.gwt.client.app.ui.util.GraphicUtils;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.client.app.ui.util.GraphicConstants.LinePattern;
import proteus.gwt.client.app.ui.util.GraphicItem.Style;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.ClassDef.Annotation;
import proteus.gwt.shared.modelica.ClassDef.Equation;
import proteus.gwt.shared.modelica.ClassDef.Modification;
import proteus.gwt.shared.modelica.parser.OMModification;
import proteus.gwt.shared.types.StringProperty;

import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * 
 * @author Maryam
 * 
 */
public class ConnectionItem extends CanvasItem {

	private Rectangle2D.Double boundary;
	public BasicStroke conn_stroke = new BasicStroke(1.0f);
	// Maryam: intermediate point and corner point have been used
	// interchangeably; they are the same
	private LinkedList<Point2D> cornerPoints = new LinkedList<Point2D>();
	public final ConnectorItem dest;
	protected Color lineColor = Color.BLACK;
	protected float lineThickness = 1.0f;
	public LinePattern pattern = LinePattern.SOLID;

	private StringProperty pointsMod;

	public final ConnectorItem src;

	public ConnectionItem(Canvas canvas, ConnectorItem src, ConnectorItem dest) {
		super(canvas, "");
		this.canvas = canvas;
		this.src = src;
		this.dest = dest;
		// boundary = getBound();
	}

	@Override
	public CanvasItem copy() {
		return null;
	}

	public Rectangle2D.Double getBound() {
		// return super.getBounds();
		ComponentCanvasItem srcComp = src.getComponentCanvasItem();
		ComponentCanvasItem destComp = dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double(srcComp.getX() + src.getX()
				+ src.getHitX(), srcComp.getY() + src.getY() + src.getHitY());

		Point2D end = new Point2D.Double(destComp.getX() + dest.getX()
				+ dest.getHitX(), destComp.getY() + dest.getY()
				+ dest.getHitY());

		return GraphicUtils.getNormBoundingBox(start.getX(), start.getY(), end
				.getX(), end.getY(), 5);
	}

	/**
	 * @return the boundary
	 */
	public Rectangle2D.Double getBoundary() {
		boundary = getBound();
		if (boundary == null)
			System.err.println("this is null boundary");
		LinkedList<Point2D> cornerPts = getCornerPoints();
		if (cornerPts.size() != 0) {
			double x = boundary.x, y = boundary.y, width = boundary.width, height = boundary.height;
			for (Point2D p1 : cornerPts) {
				double x0 = p1.getX(), y0 = p1.getY();
				if (x0 <= x) {
					width += x - x0;
					if (y0 <= y) {
						height += y - y0;
						x = x0;
						y = y0;
					} else if (y0 >= (y + height)) {
						height = y0 - y;
						x = x0;
					} else {
						x = x0;
					}
				} else if (x0 >= (x + width)) {
					width = x0 - x;
					if (y0 <= y) {
						height += y - y0;
						y = y0;
					} else if (y0 >= (y + height)) {
						height = y0 - y;
					}
				} else {
					if (y0 <= y) {
						height += y - y0;
						y = y0;
					} else if (y0 >= (y + height)) {
						height = y0 - y;
					}
				}
			}
			boundary = new Rectangle2D.Double(x, y, width, height);
		}
		return boundary;
	}

	public LinkedList<Point2D> getCornerPoints() {
		return cornerPoints;
	}

	public void addCornerPoints() {

	}

	public ConnectorItem getDestination() {
		return dest;
	}

	/**
	 * @return the lineColor
	 */
	public Color getLineColor() {
		return lineColor;
	}

	// This is used in DiagramCanvas.paint() when it is needed to highlight
	// crossed over points of different lines
	// as black rectangles
	public LinkedList<Line2D> getLines() {
		LinkedList<Line2D> lines = new LinkedList<Line2D>();

		Line2D l;

		ComponentCanvasItem srcComp = src.getComponentCanvasItem();
		ComponentCanvasItem destComp = dest.getComponentCanvasItem();

		Point2D start = new Point2D.Double(srcComp.getX() + src.getX()
				+ src.getHitX(), srcComp.getY() + src.getY() + src.getHitY());

		Point2D end = new Point2D.Double(destComp.getX() + dest.getX()
				+ dest.getHitX(), destComp.getY() + dest.getY()
				+ dest.getHitY());

		if (cornerPoints == null) {
			// System.out.println("null");
			l = new Line2D.Double(start, end);
			lines.add(l);
		} else if (cornerPoints != null && cornerPoints.size() == 0) {
			// System.out.println("not null but size 0");
			l = new Line2D.Double(start, end);
			lines.add(l);
		} else {
			l = new Line2D.Double(start, cornerPoints.getFirst());
			lines.add(l);
			for (int i = 0; i < cornerPoints.size() - 1; i++) {
				l = new Line2D.Double(cornerPoints.get(i), cornerPoints
						.get(i + 1));
				lines.add(l);
			}
			l = new Line2D.Double(cornerPoints.getLast(), end);
			lines.add(l);
		}
		return lines;
	}

	public ConnectorItem getSource() {
		return src;
	}

	public ComponentCanvasItem getSourceCCI() {
		return src.getComponentCanvasItem();
	}

	public Point2D.Double getStartPoint() {
		ComponentCanvasItem srcComp = src.getComponentCanvasItem();
		Point2D.Double start = new Point2D.Double(srcComp.getX() + src.getX()
				+ src.getHitX(), srcComp.getY() + src.getY() + src.getHitY());

		return start;
	}

	public Point2D.Double getEndPoint() {
		ComponentCanvasItem destComp = dest.getComponentCanvasItem();
		Point2D.Double end = new Point2D.Double(destComp.getX() + dest.getX()
				+ dest.getHitX(), destComp.getY() + dest.getY()
				+ dest.getHitY());
		return end;
	}

	public ComponentCanvasItem getDestCCI() {
		return dest.getComponentCanvasItem();
	}

	/**
	 * @author Sam
	 * @param equation
	 */
	public void initConnect(Equation equation) {
		Annotation annotation = equation.comment.annotation;
		OMModification pointsM = annotation == null ? null
				: (OMModification) annotation.map.get("Line.points");

		if (pointsM != null)
			pointsMod = MiscUtils.getModification(new Modification(pointsM));

		this.addMouseEventHandler(new MouseEventHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				updateCodeCanvas();
				ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
			}
		});
	}

	// @Override
	public void paint(Graphics2D g2) {
		g2.setColor(getLineColor());
		g2.setStroke(conn_stroke);
//		System.out.println("+++++++========++++++++++");
		Point2D start = getStartPoint();
		Point2D end = getEndPoint();
//		System.out.println("start p: " + start);
		int npoints = cornerPoints.size() + 2;

	/**sam adjusting the start or end points does not align with the first or last corner point problem */
		if(cornerPoints.size()>0) {
			Point2D pStart = cornerPoints.getFirst();
			Point2D pEnd = cornerPoints.getLast();
			if(pStart!=null) {
				if(Math.abs(pStart.getX()-start.getX())<Math.abs(pStart.getY()-start.getY())) {
					cornerPoints.getFirst().setLocation(start.getX(), pStart.getY());
				}else {
					cornerPoints.getFirst().setLocation(pStart.getX(), start.getY());
				}
			}
			if(pEnd!=null) {
				if(Math.abs(pEnd.getX()-end.getX())<Math.abs(pEnd.getY()-end.getY())) {
					cornerPoints.getLast().setLocation(end.getX(), pEnd.getY());
				}else {
					cornerPoints.getLast().setLocation(pEnd.getX(), end.getY());
				}
			}
		}
		
		
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];

		xpoints[0] = Math.round((float) start.getX());
		ypoints[0] = Math.round((float) start.getY());
		int i = 1;

		for (Point2D p : cornerPoints) {
			xpoints[i] = Math.round((float) p.getX());
			ypoints[i++] = Math.round((float) p.getY());
//			System.out.println("middle p: " + p);
		}
		
//		System.out.println("end p: " + end);

		xpoints[i] = Math.round((float) end.getX());
		ypoints[i] = Math.round((float) end.getY());
		g2.drawPolyline(xpoints, ypoints, npoints);
	}

	private void drawTestbound(Graphics2D g2) {
		g2.saveTransform();
		g2.setColor(Color.red);
		Rectangle2D.Double xrect = getBoundary();
		g2.drawRect(xrect.x, xrect.y, xrect.width, xrect.height);
		g2.restoreTransform();
	}

	/**
	 * @param boundary
	 *            the boundary to set
	 */
	public void setBoundary(Rectangle2D.Double boundary) {
		this.boundary = boundary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see proteus.gwt.client.app.ui.canvas.Item#setBounds(double, double,
	 * double, double)
	 */
	@Override
	public void setBounds(double x, double y, double width, double height) {
		super.setBounds(x, y, width, height);
	}

	/**
	 * @param lineColor
	 *            the lineColor to set
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public void setStyle(Style style) {
		lineColor = style.color;
		switch (style.pattern) {
		case 0:
		case 1:
		default:
			pattern = LinePattern.SOLID;
			break;
		case 2:
			pattern = LinePattern.DASH;
			break;
		case 3:
			pattern = LinePattern.DOT;
			break;
		case 4:
			pattern = LinePattern.DASHDOT;
			break;
		case 5:
			pattern = LinePattern.DASHDOTDOT;
			break;
		}
		lineThickness = style.thickness;
		Point[] points = style.points;
		if (points != null) {
			for (Point p : points) {
				Point p1 = GraphicUtils.toPixel(p.x, p.y);
				getCornerPoints().add(new Point2D.Double(p1.getX(), p1.getY()));
			}
			// update boundary
			getBoundary();
		}
		conn_stroke = GraphicItem.createStroke(pattern, lineThickness);
	}

	/**
	 * author Sam
	 */
	@Override
	public void updateCodeCanvas() {
		LinkedList<Point2D> points = getCornerPoints();

		String replaceString = "";
		Point2D p = null;
		if (points.size() > 0) {
			p = GraphicUtils.fromPixel(points.get(0));
			replaceString = "{" + p.getX() + "," + p.getY() + "}";
			for (int i = 1; i < points.size(); i++) {
				p = GraphicUtils.fromPixel(points.get(i));
				replaceString += ", {" + p.getX() + "," + p.getY() + "}";
			}

			if (pointsMod != null) {
				pointsMod.setValue("{" + replaceString + "}");
			}
		} else {
			// to make it simple, try to use the start point for the intermedian
			// points
			Point2D start = getStartPoint();
			p = GraphicUtils.fromPixel(start);
			replaceString = "{" + p.getX() + "," + p.getY() + "}";

			if (pointsMod != null) {
				pointsMod.setValue("{" + replaceString + "}");
			}
		}
	}
}

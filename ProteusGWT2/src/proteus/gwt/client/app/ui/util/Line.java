package proteus.gwt.client.app.ui.util;

import gwt.g2d.client.graphics.shapes.ShapeBuilder;

import java.util.ArrayList;
import java.util.List;

import proteus.gwt.client.app.ui.canvas.ParameterProperty;
import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;

public class Line extends FilledShape {

	enum ArrowHead {
		HEIGHT(10), WIDTH(6);
		int n;

		ArrowHead(int n) {
			this.n = n;
		}

		public int value() {
			return n;
		}
	}

	public class Arrow {
		Point start;
		Point end;
		int [] xpts = new int[4], ypts=new int[4];
		public Arrow(Point start, Point end) {
			this.start = start;
			this.end = end;
			double direction = Math.atan2(end.y - start.y, end.x - start.x);
			xpts[0]=0;
			ypts[0]=0;
			Point p1 = rotate(ArrowHead.WIDTH.value() / 2, ArrowHead.HEIGHT
					.value(), direction);
			xpts[1]=p1.x;
			ypts[1]=p1.y;
			Point p2 = rotate(-ArrowHead.WIDTH.value() / 2, ArrowHead.HEIGHT
					.value(), direction);
			xpts[2]=p2.x;
			ypts[2]=p2.y;
		}

		public Point rotate(int x, int y, double dir) {
			Point p = new Point();
			double r = Math.sqrt(x * x + y * y);
			double theta = Math.atan2(y, x);
			p.setLocation(Math.round(r * Math.cos(theta + dir + Math.PI / 2)),
					Math.round(r * Math.sin(theta + dir + Math.PI / 2)));
			return p;
		}

		public void draw(Graphics2D g) {
			g.translate(end.x, end.y);
			g.fillPolygon(xpts, ypts, 4);
		}
	}

	public static final String NAME = "Line";


	public int arrowSize = 1;


	public LinePattern pattern = LinePattern.SOLID;

	private Point[] points;

	public Smooth smooth;
	
	public boolean startArrow, endArrow;

	/**
	 * @return the startArrow
	 */
	public boolean isStartArrow() {
		return startArrow;
	}

	/**
	 * @param startArrow the startArrow to set
	 */
	public void setStartArrow(boolean startArrow) {
		this.startArrow = startArrow;
	}

	/**
	 * @return the endArrow
	 */
	public boolean isEndArrow() {
		return endArrow;
	}

	/**
	 * @param endArrow the endArrow to set
	 */
	public void setEndArrow(boolean endArrow) {
		this.endArrow = endArrow;
	}

	public float thickness = 1.0f;

	public int[] xpoints, ypoints;


	private BasicStroke l_stroke;

	public Line(GraphicCanvas graphicCanvas, List<NamedArgument> args,
			List<ParameterProperty> paraList) {
		super(graphicCanvas, args);
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("visible")) {
					String propName = arg.expression.getObjectModel().toCode();
					String value = "";
					if (paraList != null) {
						for (ParameterDTO para : paraList) {
							if (para.getName().equals(propName)) {
								value = para.getValue();
								break;
							}
						}
					}

					// default value is visible=true;
					if (value.equalsIgnoreCase("false")) {
						visible = false;
					} else {
						visible = true;
					}

				} 
			}
		}
	}
	
	public Line(GraphicCanvas graphicCanvas, List<NamedArgument> args) {
		super(graphicCanvas, args);
	}


	public Point[] getPoints() {
		// public LinkedList<Point2D> getPoints() {
		return points;
	}

	public Style getStyle() {
		Style style = new Style();
		style.color = lineColor; // sam
		switch (pattern) {
		case SOLID:
			style.pattern = 1;
			break;
		case DASH:
			style.pattern = 2;
			break;
		case DOT:
			style.pattern = 3;
			break;
		case DASHDOT:
			style.pattern = 4;
			break;
		case DASHDOTDOT:
			style.pattern = 5;
			break;
		}
		style.thickness = thickness;

		// @sam conflict with the newest package
		// style.arrow = 0;
		// style.arrow |= endArrow ? 1 : 0;
		// style.arrow |= startArrow ? 2 : 0;

		return style;
	}

	public void paint(Graphics2D g2) {
		if (!visible)
			return;
		if (points != null&&points.length>1) {
			g2.setColor(lineColor);
			g2.setStroke(l_stroke);
			Point start1 = null, end1 = null, start2 = null, end2 = null;
			int npoints = points.length;
			int[] xpoints = new int[npoints];
			int[] ypoints = new int[npoints];
			for (int i = 0; i < points.length; i++) {
				Point p  = transformPoint(new Point(points[i].x, points[i].y));
				xpoints[i] = p.x;
				ypoints[i] = p.y;
			}
			g2.drawPolyline(xpoints, ypoints, npoints);

			start1 = new Point((int)xpoints[0], (int)ypoints[0]);
			end1 = new Point((int)xpoints[1], (int)ypoints[1]);
			
			if (startArrow) {
				if (start1 != null && end1 != null) {
					g2.saveTransform();
					new Arrow(start1, end1).draw(g2);
					g2.restoreTransform();
				}
			}
			start2 = new Point((int)xpoints[npoints-2], (int)ypoints[npoints-2]);
			end2 = new Point((int)xpoints[npoints-1], (int)ypoints[npoints-1]);
			if (endArrow) {
				if (start2 != null && end2 != null) {
					g2.saveTransform();
					new Arrow(start2, end2).draw(g2);
					g2.restoreTransform();
				}
			}
		}
	}

	public void setPoints(Point[] points) {
		this.points = points;
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
		thickness = style.thickness;
		// 23 June, 2011. Only Test for Filled Arrow
		if (style.arrow[0] == 2) {
			startArrow = true;
		}
		if (style.arrow[1] == 2) {
			endArrow = true;
		}
		points = style.points;
		l_stroke= GraphicItem.createStroke(pattern, thickness);
	}

	public String[] toArgs() {
		List<String> argList = new ArrayList<String>();
		if (points != null) {
			argList.add("points=" + GraphicUtils.encodePoints(getPoints()));
		}
		Style style = getStyle();
		argList.add("style(" + Utils.join(style.toArgs(), ", ") + ")");
		String[] args = argList.toArray(new String[0]);
		return args;
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(NAME + "(");
		String[] args = toArgs();
		sb.append(Utils.join(args, ", "));
		sb.append(")");
		return sb.toString();
	}
}
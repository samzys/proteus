package proteus.gwt.client.app.ui.util;

import gwt.g2d.client.graphics.shapes.ShapeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;
import proteus.gwt.shared.graphics.Rectangle;
public class Polygon extends FilledShape {

	public static final String NAME = "Polygon";

	public Smooth smooth;

	public int[] xpoints;

	public int[] ypoints;

	public int npoints;

	private Point[] points;

	public Polygon(GraphicCanvas canvas) {
		this(canvas, null);
	}

	public Polygon(GraphicCanvas canvas, int[] xpoints, int[] ypoints,
			int npoints) {
		super(canvas);
		this.xpoints = new int[xpoints.length];
		System.arraycopy(xpoints, 0, this.xpoints, 0, xpoints.length);
		this.ypoints = new int[ypoints.length];
		System.arraycopy(ypoints, 0, this.ypoints, 0, ypoints.length);
		this.npoints = npoints;
		points = new Point[npoints];
		for (int i = 0; i < ypoints.length; i++) {
			points[i] = new Point(xpoints[i], ypoints[i]);
		}
		setPoints(points);
	}

	public Polygon(GraphicCanvas canvas, List<NamedArgument> args) {
		super(canvas, args);
		if (args != null) {
			for (NamedArgument arg2 : args) {
				if (arg2.name.equals("points")) {
					String s = arg2.expression.getObjectModel().toCode();
					setPoints(GraphicUtils.parsePoints(s));
				}
			}
		}
	}

	public Polygon(GraphicCanvas canvas, List<NamedArgument> args,
			List<ParameterDTO> paraList) {
		super(canvas, args);
		if (args != null) {
			for (NamedArgument arg2 : args) {
				if (arg2.name.equals("visible")) {
					String propName = arg2.expression.getObjectModel().toCode();
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

				} else if (arg2.name.equals("points")) {
					String s = arg2.expression.getObjectModel().toCode();
					setPoints(GraphicUtils.parsePoints(s));
				}
			}
		}
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}

	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(NAME + "(");
		String[] args = toArgs();
		sb.append(Utils.join(args, ", "));
		sb.append(")");
		return sb.toString();
	}

	public String[] toArgs() {
		List<String> argList = new ArrayList<String>();
		argList.addAll(Arrays.asList(super.toArgs()));
		if (points != null) {
			argList.add("points=" + GraphicUtils.encodePoints(getPoints()));
		}
		String[] args = argList.toArray(new String[0]);
		return args;
	}

	public Point[] getPoints() {
		return points;
	}

	public void setViewRect(Rectangle viewRect) {
		super.setViewRect(viewRect);

		npoints = points.length;
		xpoints = new int[npoints];
		ypoints = new int[npoints];
		for (int i = 0; i < npoints; i++) {
			Point p = transformPoint(new Point(points[i].x, points[i].y));
			p = transformPoint2(p);
			xpoints[i] = p.x;
			ypoints[i] = p.y;
		}
	}

	@Override
	public void paint(Graphics2D g2) {
		if (!visible)
			return;
		if (xpoints != null && ypoints != null) {
			int xmin = xpoints[0], ymin = ypoints[0], xmax = xpoints[0], ymax = ypoints[0];
			
			for (int i = 1; i < xpoints.length; i++) {
				if (xpoints[i] < xmin) {
					xmin = xpoints[i];
				} else if (xpoints[i] > xmax) {
					xmax = xpoints[i];
				}
//				// y points
				if (ypoints[i] < ymin) {
					ymin = ypoints[i];
				} else if (ypoints[i] > ymax) {
					ymax = ypoints[i];
				}
			}
			// get the bound
			Rectangle t = new Rectangle(xmin, ymin, Math.abs(xmax - xmin), Math.abs(ymax - ymin));
			g2.saveTransform();
			if (doFill(g2, t)) {
				g2.fillPolygon(xpoints, ypoints, npoints);
			}
			g2.setColor(lineColor);
			g2.setStroke(_stroke);
			g2.drawPolygon(xpoints, ypoints, npoints, t);
			g2.restoreTransform();
		}
	}
	
}
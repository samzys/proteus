package proteus.gwt.client.app.ui.util;

import gwt.g2d.client.graphics.Gradient;
import gwt.g2d.client.graphics.KnownColor;
import gwt.g2d.client.graphics.LinearGradient;
import gwt.g2d.client.graphics.RadialGradient;
import gwt.g2d.client.graphics.shapes.ShapeBuilder;

import java.util.ArrayList;
import java.util.List;

import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;
import proteus.gwt.shared.graphics.Rectangle;

/**
 * @author sam
 * 
 */
public abstract class GraphicItem implements GraphicConstants {

	protected GraphicCanvas canvas;
	protected Rectangle viewRect;
	protected Rectangle connRect;
	protected boolean isConnItem = false;

	public GraphicItem(GraphicCanvas canvas) {
		this.canvas = canvas;
		viewRect = new Rectangle(-100, -100, 200, 200);
		connRect = new Rectangle(-100, -100, 200, 200);
	}

	public Rectangle getViewRect() {
		return viewRect;
	}

	public void setViewRect(Rectangle viewRect) {

		if (viewRect == null) {
			this.viewRect.x = this.viewRect.y = this.viewRect.width = this.viewRect.height = 0;
		} else {
			this.viewRect.x = viewRect.x;
			this.viewRect.y = viewRect.y;
			this.viewRect.width = viewRect.width;
			this.viewRect.height = viewRect.height;
		}
	}

	public void setConnRect(Rectangle parentRect) {
		if (parentRect != null) {
			this.connRect.x = parentRect.x;
			this.connRect.y = parentRect.y;
			this.connRect.width = parentRect.width;
			this.connRect.height = parentRect.height;
			isConnItem = true;
		}
	}

	public abstract String toCode();

	public abstract void paint(Graphics2D canvas);

	public static BasicStroke createStroke(LinePattern pattern,
			float lineThickness) {
		BasicStroke s = null;
		switch (pattern) {
		case SOLID:
			s = new BasicStroke(lineThickness);
			break;
		case DASH:
			s = new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_BEVEL, 0.0f, new float[] {
							lineThickness * 4, lineThickness * 2 }, 0.0f);
			break;
		case DOT:
			s = new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_BEVEL, 0.0f, new float[] { lineThickness,
							lineThickness * 2 }, 0.0f);
			break;
		case DASHDOT:
			s = new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_BEVEL, 0.0f, new float[] {
							lineThickness * 4, lineThickness * 5,
							lineThickness, lineThickness * 2 }, 0.0f);
			break;
		case DASHDOTDOT:
			s = new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_BEVEL, 0.0f, new float[] {
							lineThickness * 4, lineThickness * 8,
							lineThickness, lineThickness * 2 }, 0.0f);
			break;
		case NONE:
		default:
			s = new BasicStroke(1.0f);
			break;
		}
		return s;
	}

	public Point transformPoint(Point original) {
		return transformPoint(original, viewRect.x, viewRect.y, viewRect.x
				+ viewRect.width, viewRect.y + viewRect.height);
	}

	public Point transformPoint2(Point point) {
		if (isConnItem && viewRect.width != 0 && viewRect.height != 0) {
			double xscale = (double) connRect.width / viewRect.width;
			double yscale = (double) connRect.height / viewRect.height;

			Point p = new Point();
			p.x = (int) (point.x * xscale);
			p.y = (int) (point.y * yscale);
			return p;
		} else
			return point;
	}

	public Point transformPoint(Point original, int x1, int y1, int x2, int y2) {
		Point p = new Point();
		p.x = original.x - x1;
		// equivalent to height = y2 - y1; height - (original.y - y1);
		p.y = y2 - original.y;
		return p;
	}

	public Rectangle transformRect(Rectangle original) {
		return transformRect(original, viewRect.x, viewRect.y, viewRect.x
				+ viewRect.width, viewRect.y + viewRect.height);
	}

	public Rectangle transformRect2(Rectangle original) {
		if (isConnItem) {
			Rectangle r = new Rectangle();
			double xscale = (double) connRect.width / viewRect.width;
			double yscale = (double) connRect.height / viewRect.height;
			r.x = Math.round((float) (original.x * xscale));
			r.y = Math.round((float) (original.y * yscale));
			r.width = Math.round((float) (original.width * xscale));
			r.height = Math.round((float) (original.height * yscale));

			r.x = (int) (original.x * xscale);
			r.y = (int) (original.y * yscale);
			r.width = (int) (original.width * xscale);
			r.height = (int) (original.height * yscale);
			return r;
		} else
			return original;
	}

	// Transforms the original rectangle defined in a right-handed coordinate
	// space (x1, y1), (x2, y2), assuming x1 <= x2, y1 <= y2 into left-handed
	// coordinate system with origin at (x1, y1). E.g. (x1, y1) -> (0, 0), (x1 +
	// a, y1 + b) -> (a, height - b)
	public static Rectangle transformRect(Rectangle original, int x1, int y1,
			int x2, int y2) {
		Rectangle r = new Rectangle();
		r.x = original.x - x1;
		// equivalent to height = y2 - y1; height - (original.y +
		// original.height - y1);
		r.y = y2 - (original.y + original.height);
		r.width = original.width;
		r.height = original.height;
		return r;
	}

	public static class Style implements GraphicConstants {
		public static final int GRADIENT_STYLE_COUNT = 4;

		public Color color = Color.BLACK;

		public Color lineColor;

		public int gradient = 0;

		public Color fillColor = Color.BLACK;

		public int pattern = 0;

		public float thickness = 1.0f;

		public int[] arrow = { 0, 0 };

		public int linePattern = 0;

		public int fillPattern = 0;
		public Point[] points;

		public Style() {
		}

		public Style(List<NamedArgument> args) {
			for (Object obj : args) {
				NamedArgument arg = (NamedArgument) obj;
				String s = arg.expression.getObjectModel().toCode().trim();
				if (arg.name.equals("color")) {
					color = GraphicUtils.parseColor(s);
				} else if (arg.name.equals("fillColor")) {
					fillColor = GraphicUtils.parseColor(s);
					if (fillPattern == 0) {
						// if FillPattern is FillPattern.None then change it to
						// FillPattern.SOLID
						fillPattern = 1;
					}
				} else if (arg.name.equals("pattern")) {
					pattern = GraphicUtils.parseLinePattern(s);
				} else if (arg.name.equals("linePattern")) {
					linePattern = GraphicUtils.parseLinePattern(s);
				} else if (arg.name.equals("fillPattern")) {
					fillPattern = GraphicUtils.parseFillPattern(s);

				} else if (arg.name.equals("thickness")) {
					thickness = Float.parseFloat(s);
				} else if (arg.name.equals("arrow")) {
					arrow = GraphicUtils.parseArrow(s);
				} else if (arg.name.equals("gradient")) {
					gradient = Integer.parseInt(s);
				} else if (arg.name.equals("lineColor")) {
					lineColor = GraphicUtils.parseColor(s);
				} else if (arg.name.equals("points")) {
					points = GraphicUtils.parsePoints(s);
				}
			}
		}


		public String[] toArgs() {
			List<String> argList = new ArrayList<String>();
			if (color != null) {
				argList.add("color=" + GraphicUtils.encodeColor(color));
			}
			if (pattern > 0) {
				argList.add("pattern=" + pattern);
			}
			if (!Utils.isEqual(thickness, 1.0)) {
				argList.add("thickness=" + thickness);
			}
			if (arrow != null) {
				argList.add("arrow=" + GraphicUtils.encodeArrow(arrow));
			}
			if (fillColor != null) {
				argList.add("fillColor=" + GraphicUtils.encodeColor(fillColor));
			}
			if (fillPattern > 0) {
				argList.add("fillPattern=" + fillPattern);
			}
			if (gradient != 0) {
				argList.add("gradient=" + gradient);
			}
			return argList.toArray(new String[0]);
		}
	}
}

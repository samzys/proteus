package proteus.gwt.client.app.ui.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.ui.util.GraphicConstants.Arrow;
import proteus.gwt.client.app.ui.util.GraphicConstants.FillPattern;
import proteus.gwt.client.app.ui.util.GraphicConstants.LinePattern;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.Component;
import proteus.gwt.shared.modelica.Placement.Transformation;
import proteus.gwt.shared.util.Constants;
import proteus.gwt.shared.util.Utils;

public class GraphicUtils {

	public static Point2D fromPixel(Point2D point2d) {
		Point2D.Double p = new Point2D.Double();
		Point c = new Point(Constant.diagramCanvasSize.width / 2,
			Constant.diagramCanvasSize.height / 2);
		p.x = point2d.getX() - c.x;
		p.y = -(point2d.getY() - c.y);
		return p;
	}

	public static Rectangle topixel2(int x, int y, int abs, int abs2) {
		Rectangle r = new Rectangle();
		r.width = abs;
		r.height = abs2;
		Point c = new Point(Constant.diagramCanvasSize.width / 2,
				Constant.diagramCanvasSize.height / 2);
		r.x = (int) (c.x + x);
		r.y = (int) (c.y + y);
		return r;
	}

	public static Point toPixel(int x, int y) {
		double xscale = (double) Constant.diagramCanvasSize.width / 300;
		double yscale = (double) Constant.diagramCanvasSize.height / 300;
		// Point c = new Point(canvasSize.width / 2, canvasSize.height / 2);

		xscale = yscale = 1;
		Point c = new Point(Constant.diagramCanvasSize.width / 2,
				Constant.diagramCanvasSize.height / 2);
		c.x = (int) (c.x + x * xscale);
		c.y = (int) (c.y - y * yscale);
		return c;
	}

	public static String[] splitPoints(String s, char token) {
		LinkedList<String> result = new LinkedList<String>();
		int j = 0, bb = 0;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == '{')
				bb++;
			else if (s.charAt(i) == '}')
				bb--;

			if (s.charAt(i) == token && bb == 0) {
				result.add(s.substring(j, i));
				j = i + 1;
			} else if (i == s.length() - 1 && bb == 0) {
				result.add(s.substring(j, i + 1));
				j = i + 1;
			}
		}

		String[] array = new String[result.size() == 0 ? 1 : result.size()];
		if (result.size() == 0) {
			array[0] = s;
			return array;
		} else {
			return result.toArray(array);
		}
	}

	public static String removeBrackets(String s) {
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < sb.length(); ++i) {
			if (sb.charAt(i) == '{' || sb.charAt(i) == '}') {
				sb.deleteCharAt(i--);
			}
		}
		return sb.toString();
	}

	// 09 oct 09 lt : parse like "{-100, 100}"
	public static Point parsePoint(String s) {
		s = s.substring(1, s.length() - 1);
		String[] pa_toks = splitPoints(s, ',');
		if (pa_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		Point p = new Point((int) Double.parseDouble(removeBrackets(pa_toks[0])
				.trim()), (int) Double.parseDouble(removeBrackets(pa_toks[1])
				.trim()));
		return p;
	}

	public static Point[] parsePoints(String s) {
		// public static LinkedList<Point2D> parsePoints(String s) {
		s = s.substring(1, s.length() - 1);
		String[] pa_toks = splitPoints(s, ',');
		// List<Point> plist = new ArrayList<Point>();
		LinkedList<Point2D> plist = new LinkedList<Point2D>();
		for (String pa_tok : pa_toks) {
			pa_tok = removeBrackets(pa_tok);
			String[] p_toks = pa_tok.split(",");
			if (p_toks.length != 2) {
				throw new RuntimeException("parse error");
			}
			Point p = new Point((int) Double.parseDouble(p_toks[0].trim()),
					(int) Double.parseDouble(p_toks[1].trim()));
			// Point2D p = new
			// Point2D.Double(Double.parseDouble(p_toks[0].trim()),
			// Double.parseDouble(p_toks[1].trim()));
			plist.add(p);
		}
		return plist.toArray(new Point[0]);
		// return plist;
	}

	public static String encodePoints(Point[] points) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		List<String> pointList = new ArrayList<String>();
		for (Point p : points) {
			pointList.add(p.x + "," + p.y);
		}
		sb.append(Utils.join(pointList.toArray(new String[0]), "; "));
		sb.append("]");
		return sb.toString();
	}

	public static final Color[] PALETTE = { Color.BLACK, Color.RED,
			Color.GREEN, Color.BLUE, Color.CYAN, Color.YELLOW, Color.WHITE,
			Color.lightGray, Color.gray };

	public static Color parseColor(String s) {
		if (s.equals("\"Black\"")) {
			return Color.BLACK;
		} else {
			try { // Is it an int?
				int n = Integer.parseInt(s);
				if (n >= 0 && n < PALETTE.length) {
					return PALETTE[n];
				}
				return Color.BLACK;
			} catch (NumberFormatException e) {
				// thru a call (DynamicSelect(...), see for e.g.
				// BooleanExpression in Sources). For now, treat that as a
				// special case and use black
				if (s.startsWith("DynamicSelect")) {
					return Color.BLACK;
				}
				s = s.substring(1, s.length() - 1);
				String[] c_toks = s.split(",");
				if (c_toks.length != 3) {
					throw new RuntimeException("parse error");
				}
				int r = MiscUtils.clamp(Integer.parseInt(c_toks[0].trim()), 0,
						255);
				int g = MiscUtils.clamp(Integer.parseInt(c_toks[1].trim()), 0,
						255);
				int b = MiscUtils.clamp(Integer.parseInt(c_toks[2].trim()), 0,
						255);
				return new Color(r, g, b);
			}
		}
	}

	public static int[] parseExtent(String s) {
		s = s.substring(1, s.length() - 1);
		String[] pa_toks = splitPoints(s, ',');
		if (pa_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		pa_toks[0] = removeBrackets(pa_toks[0]);
		pa_toks[1] = removeBrackets(pa_toks[1]);
		String[] p_toks;
		p_toks = pa_toks[0].split(",");
		if (p_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		int x1 = (int) Double.parseDouble(p_toks[0].trim()), y1 = (int) Double
				.parseDouble(p_toks[1].trim());
		p_toks = pa_toks[1].split(",");
		if (p_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		int x2 = (int) Double.parseDouble(p_toks[0].trim()), y2 = (int) Double
				.parseDouble(p_toks[1].trim());

		return new int[] { x1, y1, x2, y2 };
	}

	public static Rectangle parseRectangle(String s) {
		s = s.substring(1, s.length() - 1);
		String[] pa_toks = splitPoints(s, ',');
		if (pa_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		pa_toks[0] = removeBrackets(pa_toks[0]);
		pa_toks[1] = removeBrackets(pa_toks[1]);
		String[] p_toks;
		p_toks = pa_toks[0].split(",");
		if (p_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		int x1 = (int) Double.parseDouble(p_toks[0].trim()), y1 = (int) Double
				.parseDouble(p_toks[1].trim());
		p_toks = pa_toks[1].split(",");
		if (p_toks.length != 2) {
			throw new RuntimeException("parse error");
		}
		int x2 = (int) Double.parseDouble(p_toks[0].trim()), y2 = (int) Double
				.parseDouble(p_toks[1].trim());
		return new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2
				- x1), Math.abs(y2 - y1));
	}

	public static String encodeRectangle(Rectangle rect) {
		return "[" + rect.x + "," + rect.y + ";" + (rect.x + rect.width) + ","
				+ (rect.y + rect.height) + "]";
	}

	public static int parseLinePattern(String s) {
		String name = s.substring(12).toUpperCase();
		return LinePattern.valueOf(name).ordinal();
	}

	public static int parseFillPattern(String s) {
		String name = s.substring(12).toUpperCase();
		return FillPattern.valueOf(name).ordinal();
	}

	public static String parseString(String s) {
		s = s.substring(1, s.length() - 1);
		return s;
	}

	public static Rectangle2D.Double getNormBoundingBox(double x1, double y1,
			double x2, double y2, int Gap) {
		/** this bounding box is always parallel to the coordinate system ****/
		Rectangle2D.Double rect = null;
		int GAP = 5;
		if ((x1 - x2) == 0) {
			rect = new Rectangle2D.Double(x1 - GAP, Math.min(y1, y2) - GAP,
					GAP * 2, Math.abs(y1 - y2) + GAP * 2);
		} else if ((y1 - y2) == 0) {
			rect = new Rectangle2D.Double(Math.min(x1, x2) - GAP, y1 - GAP,
					Math.abs(x1 - x2) + GAP * 2, GAP * 2);
		} else {
			rect = new Rectangle2D.Double(Math.min(x1, x2) - GAP, Math.min(y1,
					y2)
					- GAP, Math.abs(x1 - x2) + GAP * 2, Math.abs(y1 - y2) + GAP
					* 2);
		}

		return rect;
	}

	public static String encodeString(String s) {
		return "\"" + s + "\"";
	}

	public static int[] parseArrow(String s) {
		int[] arrowPoint = new int[2];
		if (true) {
			if (s.startsWith("{") && s.endsWith("}")) {
				s = s.substring(1, s.length() - 1);
			}
			String[] st = s.split(",");
			for (int j = 0; j < st.length; j++) {
				String[] st2 = st[j].split(".");
				int index = st[j].lastIndexOf(".");
				String arr = st[j].substring(index + 1);
				arrowPoint[j] = Arrow.valueOf(arr.toUpperCase()).ordinal();
			}

			return arrowPoint;
		}
		return null;
	}

	public static boolean isConnectorComp(Component component) {
		if (component.restriction.equals(Constants.TYPE_CONNECTOR)
				|| component.restriction
						.equals(Constants.TYPE_EXPANDABLECONNECTOR)) {
			return true;
		} else
			return false;
	}

	public static Transformation getTrans(String origin, String extent,
			String rotation) {
		Transformation transformation = new Transformation();
		String m = origin;
		if (m != null) {
			int[] tO = MiscUtils.parseOrigin(m);
			// sam. y is not converted here. and will be converted later when it
			// was used.
			transformation.origin = new int[] { tO[0], tO[1] };
		} else {
			transformation.origin = new int[2];
		}
		m = extent;
		if (m != null) {
			int[] tR = GraphicUtils.parseExtent(m);
			// @sam 24 May.
			transformation.extent = new int[] { tR[0], -tR[1], tR[2], -tR[3] };
			transformation.oldExtent = GraphicUtils.parseExtent(m);
		}
		m = rotation;
		if (m != null) {
			transformation.rotation = Integer.parseInt(m);
		} else {
			transformation.rotation = 0;
		}
		return transformation;
	}

	public static String encodeArrow(int[] arrow) {
		return "{" + arrow[0] + "," + arrow[1] + "}";
	}

	public static String encodeColor(Color color) {
		if (color.equals(Color.BLACK)) {
			return "\"Black\"";
		} else {
			// One of palette?
			for (int i = 0; i < PALETTE.length; i++) {
				if (color.equals(PALETTE[i])) {
					return "" + i;
				}
			}
			// return "{" + color.getRed() + "," + color.getGreen() + ","
			// + color.getBlue() + "}";
			// TODO encode color
			return null;
		}
	}

}

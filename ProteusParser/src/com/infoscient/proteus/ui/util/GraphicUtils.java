package com.infoscient.proteus.ui.util;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.infoscient.proteus.Utils;
import com.infoscient.proteus.ui.util.GraphicConstants.Arrow;
import com.infoscient.proteus.ui.util.GraphicConstants.FillPattern;
import com.infoscient.proteus.ui.util.GraphicConstants.LinePattern;

public class GraphicUtils {
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
		if (true) { // for version 3.1
			s = s.substring(1, s.length() - 1);
			String[] pa_toks = splitPoints(s, ',');
			if (pa_toks.length != 2) {
				throw new RuntimeException("parse error");
			}
			Point p = new Point((int) Double.parseDouble(removeBrackets(
					pa_toks[0]).trim()),
					(int) Double.parseDouble(removeBrackets(pa_toks[1]).trim()));
			return p;
		}
		return null;
	}



	public static Point[] parsePoints(String s) {
		if (true) { // for version 3.1
			s = s.substring(1, s.length() - 1);
			String[] pa_toks = splitPoints(s, ',');
			List<Point> plist = new ArrayList<Point>();
			for (String pa_tok : pa_toks) {
				pa_tok = removeBrackets(pa_tok);
				String[] p_toks = pa_tok.split(",");
				if (p_toks.length != 2) {
					throw new RuntimeException("parse error");
				}
				Point p = new Point((int) Double.parseDouble(p_toks[0].trim()),
						(int) Double.parseDouble(p_toks[1].trim()));
				plist.add(p);
			}
			return plist.toArray(new Point[0]);
		} else { // old parsing code
			s = s.substring(1, s.length() - 1);
			String[] pa_toks = s.split(";");
			List<Point> plist = new ArrayList<Point>();
			for (String pa_tok : pa_toks) {
				String[] p_toks = pa_tok.split(",");
				if (p_toks.length != 2) {
					throw new RuntimeException("parse error");
				}
				Point p = new Point((int) Double.parseDouble(p_toks[0].trim()),
						(int) Double.parseDouble(p_toks[1].trim()));
				plist.add(p);
			}
			return plist.toArray(new Point[0]);
		}
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

	public static final Color[] PALETTE = { Color.black, Color.red,
			Color.green, Color.blue, Color.cyan, Color.magenta, Color.yellow,
			Color.white, Color.lightGray, Color.gray, Color.darkGray };

	public static Color parseColor(String s) {
		if (s.equals("\"Black\"")) {
			return Color.black;
		} else {
			try { // Is it an int?
				int n = Integer.parseInt(s);
				if (n >= 0 && n < PALETTE.length) {
					return PALETTE[n];
				}
				return Color.black;
			} catch (NumberFormatException e) {
				// TODO: There's this facility to dynamically select the color
				// thru a call (DynamicSelect(...), see for e.g.
				// BooleanExpression in Sources). For now, treat that as a
				// special case and use black
				if (s.startsWith("DynamicSelect")) {
					return Color.black;
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

	public static String encodeColor(Color color) {
		if (color.equals(Color.black)) {
			return "\"Black\"";
		} else {
			// One of palette?
			for (int i = 0; i < PALETTE.length; i++) {
				if (color.equals(PALETTE[i])) {
					return "" + i;
				}
			}
			return "{" + color.getRed() + "," + color.getGreen() + ","
					+ color.getBlue() + "}";
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

	public static java.awt.Rectangle parseRectangle(String s) {
		if (true) { // for modelica v3.1
			//15/03/2011@sam  amend for annotaion contains DynamicSelect
			if(s.contains("DynamicSelect")){
				//remove DynamicSelect option
				s = s.substring(s.indexOf("("));
				//remove bracket 
				s = s.substring(1, s.length() - 1);
				//get the first extent
				String [] tokens = splitPoints(s, ',');
				s = tokens[0];
				
			}
			
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
			System.err.println();
			int x1 = (int) Double.parseDouble(p_toks[0].trim()), y1 = (int) Double
					.parseDouble(p_toks[1].trim());
			p_toks = pa_toks[1].split(",");
			if (p_toks.length != 2) {
				throw new RuntimeException("parse error");
			}
			int x2 = (int) Double.parseDouble(p_toks[0].trim()), y2 = (int) Double
					.parseDouble(p_toks[1].trim());
			return new java.awt.Rectangle(Math.min(x1, x2), Math.min(y1, y2),
					Math.abs(x2 - x1), Math.abs(y2 - y1));
		} else {
			s = s.substring(1, s.length() - 1);
			String[] pa_toks = s.split(";");
			if (pa_toks.length != 2) {
				throw new RuntimeException("parse error");
			}
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
			return new java.awt.Rectangle(Math.min(x1, x2), Math.min(y1, y2),
					Math.abs(x2 - x1), Math.abs(y2 - y1));
		}
	}

	public static String encodeRectangle(java.awt.Rectangle rect) {
		return "[" + rect.x + "," + rect.y + ";" + (rect.x + rect.width) + ","
				+ (rect.y + rect.height) + "]";
	}

	public static int parseLinePattern(String s) {
		if (true) {
			String name = s.substring(12).toUpperCase();
			return LinePattern.valueOf(name).ordinal();
		} else {
			return Integer.parseInt(s);
		}
	}

	public static int parseFillPattern(String s) {
		if (true) {
			String name = s.substring(12).toUpperCase();
			FillPattern fp[] = FillPattern.values();
			boolean enumContains =false;
			for (int i = 0; i < fp.length; i++) {
				if(fp[i].equals(name)){
					enumContains = true;
					break;
				}
			}
			int index = 0;
			if(enumContains){
			index =FillPattern.valueOf(name).ordinal();
			}
			return index;
		} else {
			return Integer.parseInt(s);
		}
	}

	public static String parseString(String s) {
		s = s.substring(1, s.length() - 1);
		return s;
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
			StringTokenizer st = new StringTokenizer(s, ",");
			for (int i = 0; i < 2; i++) {
				String s1 = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(s1, ".");
				st2.nextToken();
				String s2 = st2.nextToken();

				arrowPoint[i] = Arrow.valueOf(s2.toUpperCase()).ordinal();
			}
			return arrowPoint;
		} else {
			return arrowPoint;
		}
	}

	public static String encodeArrow(int[] arrow) {
		return "{" + arrow[0] + "," + arrow[1] + "}";
	}
}

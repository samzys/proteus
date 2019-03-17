package proteus.gwt.client.app.ui.util;

import java.util.Date;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.LogMessagePanelEvent;
import proteus.gwt.client.app.ui.MessagePanel.Message;
import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.modelica.ClassDef.Expression;
import proteus.gwt.shared.modelica.ClassDef.Modification;
import proteus.gwt.shared.modelica.parser.ModelicaParser;
import proteus.gwt.shared.modelica.parser.OMExpression;
import proteus.gwt.shared.modelica.parser.OMModification;
import proteus.gwt.shared.types.StringProperty;
import proteus.gwt.shared.util.StringReader;
import proteus.gwt.shared.graphics.Rectangle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class MiscUtils {

	public static Point scaleLoc(double x, double y, double s) {
		Point p = new Point();
		Dimension canvasSize = Constant.diagramCanvasSize;
		p.x = (int) ((x - canvasSize.width / 2) / s + canvasSize.width / 2);
		p.y = (int) ((y - canvasSize.height / 2) / s + canvasSize.height / 2);
		return p;
	}

	public static String getTime() {

		return DateTimeFormat.getFormat(
				DateTimeFormat.PredefinedFormat.TIME_FULL).format(new Date())
				+ "<br/>";
	}

	public static long getTimeStampMilli() {
		Date date = new Date();
		return date.getTime();
	}

	// Returns a 2-element array [x, y]
	public static int[] parseOrigin(String origin) {
		Point point = GraphicUtils.parsePoint(origin);
		int[] res = new int[2];
		res[0] = point.x;
		res[1] = point.y;
		return res;
	}

	// @sam 3d animation
	public static String parseString(String s) {
		String s1 = s.substring(1, s.length() - 1);
		if (s1 != null)
			return s1;
		else
			return "";
	}

	public static int[] parseExtentPoints(String extent) {
		// int [] ext = new int[4];
		return GraphicUtils.parseExtent(extent);
		// return ext;
	}

	// Returns a 4-element array [x1, y1, x2, y2]
	public static int[] parseExtent(String extent) {
		Rectangle rect = GraphicUtils.parseRectangle(extent);
		int[] res = new int[4];
		res[0] = rect.x;
		res[1] = rect.y;
		res[2] = rect.width + rect.x;
		res[3] = rect.height + rect.y;
		return res;

	}

	public static String URLTool(String url) {
		String urlContext = GWT.getHostPageBaseURL();

		if (urlContext.endsWith("/")) {
			urlContext = urlContext.substring(0, urlContext.length() - 1);
		}

		String path = url;

		int i = path.lastIndexOf("resources/");
		if (i == -1) {
			// probably not from our site
			return path;
		}

		// all resource files will be stored in the 'resource' folder
		String relativePath = path.substring(i);
		return urlContext + "/" + relativePath;
	}

	public static String getImagePath(String relative) {
		if (relative != null) {
			int index = relative.indexOf("/Images");
			if (index != -1)
				return relative.substring(index);
			else
				return null;
		} else
			return null;
	}

	public static String getAbsolutePath(String relative, String base) {
		if (relative.startsWith(".") && base != null) {
			String s = relative;
			String b = base.substring(0, base.lastIndexOf('/'));
			while (s.startsWith("./")) {
				s = s.substring(2);
			}
			while (s.startsWith("../")) {
				s = s.substring(3);
				b = b.substring(0, b.lastIndexOf('/'));
			}
			return b + '/' + s;
		} else {
			return relative;
		}
	}

	public static int clamp(int n, int min, int max) {
		return Math.max(min, Math.min(max, n));
	}

	public static StringProperty getModification(final Modification md) {
		final OMModification m = md.getObjectModel();
		// Over-ride getObjectModel because the custom set method
		// replaces the object model, which is not accessible from
		// an Expression object
		md.expression = new Expression(null) {
			public OMExpression getObjectModel() {
				return m.expression;
			}
		};
		md.expression.value = new StringProperty("") {

			/*
			 * (non-Javadoc)
			 * 
			 * @see proteus.gwt.shared.types.StringProperty#getValue()
			 */
			@Override
			public String getValue() {
				return super.getValue();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * proteus.gwt.shared.types.StringProperty#setValue(java.lang.String
			 * )
			 */
			@Override
			public void setValue(String value) {
				super.setValue(value);
				String s = (String) value;
				// System.err.println("inside this get modification: "+s);
				ModelicaParser parser = new ModelicaParser(new StringReader(s));
				try {
					m.expression = parser.expression();
					md.expression = new Expression(m.expression);
				} catch (Exception e) {
					m.expression = null;
					e.printStackTrace();
				}
			}
		};

		return md.expression.value;
	}
}

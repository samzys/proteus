package proteus.gwt.client.app.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;

public class Ellipse extends FilledShape {
	public static final String NAME = "Ellipse";

	public BorderPattern borderPattern = BorderPattern.NONE;
	private Rectangle extent;

	public double startAngle = 0.0, endAngle = 2 * Math.PI;

	public Ellipse(GraphicCanvas canvas) {
		this(canvas, null);
	}

	public Ellipse(GraphicCanvas canvas, List<NamedArgument> args) {
		super(canvas, args);
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);
				} else if (arg.name.equals("startAngle")) {
					String s = arg.expression.getObjectModel().toCode();
					startAngle = Double.parseDouble(s);
				} else if (arg.name.equals("endAngle")) {
					String s = arg.expression.getObjectModel().toCode();
					endAngle = Double.parseDouble(s);
				}
			}
		}
	}

	public Ellipse(GraphicCanvas canvas, List<NamedArgument> args,
			List<ParameterDTO> paraList) {
		super(canvas, args);
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
				} else if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);
				} else if (arg.name.equals("startAngle")) {
					String s = arg.expression.getObjectModel().toCode();
					startAngle = Double.parseDouble(s);
				} else if (arg.name.equals("endAngle")) {
					String s = arg.expression.getObjectModel().toCode();
					endAngle = Double.parseDouble(s);
				}
			}

		}
	}

	public BorderPattern getBorderPattern() {
		return borderPattern;
	}

	public Rectangle getExtent() {
		return extent;
	}
 @Override
	public void paint(Graphics2D g2) {
		if (!visible)
			return;
		if (extent != null) {
			Rectangle t = transformRect2(transformRect(extent));

			if (doFill(g2, t)) {
				g2.fillEllipse(t.x, t.y, t.width, t.height);
			} 
				g2.setColor(lineColor);
				g2.setStroke(_stroke);
				g2.drawEllipse(t.x, t.y, t.width, t.height);
		}
	}

	public void setBorderPattern(BorderPattern borderPattern) {
		this.borderPattern = borderPattern;
	}

	public void setExtent(Rectangle extent) {
		this.extent = extent;
	}

	public String[] toArgs() {
		List<String> argList = new ArrayList<String>();
		argList.addAll(Arrays.asList(super.toArgs()));
		if (extent != null) {
			argList.add("extent=" + GraphicUtils.encodeRectangle(getExtent()));
		}
		if (!Utils.isZero(startAngle)) {
			argList.add("startAngle=" + startAngle);
		}
		if (!Utils.isEqual(endAngle, 2 * Math.PI)) {
			argList.add("endAngle=" + endAngle);
		}
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
package proteus.gwt.client.app.ui.util;

import gwt.g2d.client.graphics.shapes.ShapeBuilder;

import java.util.ArrayList;
import java.util.List;

import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;

public class Rectangle extends FilledShape {
	public static final String NAME = "Rectangle";

	protected BorderPattern borderPattern = BorderPattern.NONE;

	protected proteus.gwt.shared.graphics.Rectangle extent = new proteus.gwt.shared.graphics.Rectangle(
			-100, -100, 0, 0);

	protected int radius = 0;

	protected int x, y, width, height;

	public Rectangle(GraphicCanvas canvas) {
		this(canvas, null);
	}

	public Rectangle(GraphicCanvas canvas, int x, int y, int width, int height) {
		super(canvas);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		extent.x = x;
		extent.y = y;
		extent.width = width;
		extent.height = height;
	}

	public Rectangle(GraphicCanvas canvas, List<NamedArgument> args) {
		super(canvas, args);
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);
				} else if (arg.name.equals("radius")) {
					String s = arg.expression.getObjectModel().toCode();
					radius = (int) Double.parseDouble(s);
				}
			}
		}
	}

	public Rectangle(GraphicCanvas canvas, List<NamedArgument> args,
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
				} else if (arg.name.equals("radius")) {
					String s = arg.expression.getObjectModel().toCode();
					radius = (int) Double.parseDouble(s);
				}
			}
		}
	}

	public proteus.gwt.shared.graphics.Rectangle getExtent() {
		return extent;
	}

	public int getRadius() {
		return radius;
	}
	@Override
	public void paint(Graphics2D g2) {
		if (!visible)
			return;

		if (extent != null) {
			proteus.gwt.shared.graphics.Rectangle t = transformRect2(transformRect(extent));
			if (doFill(g2, t)) {
				g2.fillRect(t.x, t.y, t.width, t.height);
			} 
				g2.setColor(lineColor);
				g2.setStroke(_stroke);
				g2.drawRect(t.x, t.y, t.width, t.height);
		}
	}

	public void setExtent(proteus.gwt.shared.graphics.Rectangle extent) {
		this.extent = extent;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public String[] toArgs() {
		List<String> argList = new ArrayList<String>();
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
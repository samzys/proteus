package proteus.gwt.client.app.ui.util;

import gwt.g2d.client.graphics.Gradient;
import gwt.g2d.client.graphics.KnownColor;
import gwt.g2d.client.graphics.LinearGradient;
import gwt.g2d.client.graphics.RadialGradient;

import java.util.ArrayList;
import java.util.List;

import proteus.gwt.client.app.ui.canvas.ParameterProperty;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.types.StringProperty;
import proteus.gwt.shared.util.Utils;
import proteus.gwt.shared.graphics.Rectangle;

public abstract class FilledShape extends GraphicItem {
	protected Color lineColor = Color.BLACK;

	protected Color fillColor = Color.BLACK;

	protected LinePattern pattern = LinePattern.SOLID;

	protected FillPattern fillPattern = FillPattern.NONE;

	protected float lineThickness = 1.0f;

	protected int gradient;

	protected boolean visible = true;

	protected List<ParameterProperty> paraList;

	public FilledShape(GraphicCanvas canvas) {
		super(canvas);
	}

	public FilledShape(GraphicCanvas canvas, List<NamedArgument> args) {
		super(canvas);
		if (args != null) {
			Style style = new Style(args);
			setStyle(style);
		}
		if (_stroke == null) {
			createStroke();
		}
	}

	public void setStyle(Style style) {
		if (style.lineColor != null)
			lineColor = style.lineColor;
		else
			lineColor = style.color;
		fillColor = style.fillColor;
		for (FillPattern p : FillPattern.values()) {
			if (p.ordinal() == style.fillPattern) {
				fillPattern = p;
				break;
			}
		}
		gradient = style.gradient;
	}

	protected String getParameterProperty(String propName) {

		if (paraList != null) {
			for (ParameterProperty para : paraList) {
				if (para.getName().equals(propName)) {
					if(para.getValue()==null) return propName;
					return para.getValue();
				}
			}
		}
		return null;
	}

	public Style getStyle() {
		Style style = new Style();
		style.color = lineColor;
		style.fillColor = fillColor;
		style.fillPattern = fillPattern.ordinal();

		style.gradient = gradient;
		return style;
	}

	public String[] toArgs() {
		List<String> argList = new ArrayList<String>();
		Style style = getStyle();
		argList.add("style(" + Utils.join(style.toArgs(), ", ") + ")");
		return argList.toArray(new String[0]);
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public LinePattern getPattern() {
		return pattern;
	}

	protected BasicStroke _stroke;

	protected void createStroke() {
		_stroke = createStroke(pattern, lineThickness);
	}

	public void setPattern(LinePattern pattern) {
		this.pattern = pattern;
		createStroke();
	}

	public FillPattern getFillPattern() {
		return fillPattern;
	}

	public void setFillPattern(FillPattern fillPattern) {
		this.fillPattern = fillPattern;
	}

	public float getLineThickness() {
		return lineThickness;
	}

	public void setLineThickness(float lineThickness) {
		this.lineThickness = lineThickness;
		createStroke();
		// canvas.repaint();
	}

	public int getGradient() {
		return gradient;
	}

	public void setGradient(int gradient) {
		this.gradient = gradient;
	}

	protected Gradient gradPaint = null;
	protected boolean isFill = false;

	public boolean setGradient(Graphics2D g2, int x, int y, int width,
			int height, Color c) {
		gwt.g2d.client.graphics.Color c1;
		c1 = new gwt.g2d.client.graphics.Color(fillColor.getRed(), fillColor
				.getGreen(), fillColor.getBlue(), fillColor.getAlpha());
		if (gradPaint == null) {
			switch (fillPattern) {
			case HORIZONTAL:
			case HORIZONTALCYLINDER:
				gradPaint = new LinearGradient(x, y, x, y + height);
				gradPaint.addColorStop(0, KnownColor.BLACK);
				gradPaint.addColorStop(0.5, c1);
				gradPaint.addColorStop(1, KnownColor.BLACK);
				isFill = true;
				break;
			case VERTICAL:
			case VERTICALCYLINDER:
				gradPaint = new LinearGradient(x, y, x + width, y);
				gradPaint.addColorStop(0, KnownColor.BLACK);
				gradPaint.addColorStop(0.5, c1);
				gradPaint.addColorStop(1, KnownColor.BLACK);
				isFill = true;
				break;
			case SPHERE:
				gradPaint = new RadialGradient(x + width / 2, y + height / 2,
						0, x + width / 2, y + height / 2, (width+ height)/2);
				gradPaint.addColorStop(0, c1);
				gradPaint.addColorStop(1, KnownColor.BLACK);
				isFill = true;
				break;
			case BACKWARD:
				g2.setColor(fillColor);
				System.err.println("fillpattern.backward. Pls check");
				isFill = true;
				break;
			case CROSS:
				g2.setColor(fillColor);
				System.err.println("fillpattern.cross. Pls check");
				isFill = true;
				break;
			case SOLID:
				g2.setColor(fillColor);
				isFill = true;
				break;
			case NONE:
			default:
				break;
			}
		}
		if (gradPaint != null)
			g2.setPaint(gradPaint);
		return isFill;
	}

	protected boolean doFill(Graphics2D g2, Rectangle t) {
		return setGradient(g2, t.x, t.y, t.width, t.height, fillColor);
	}
}

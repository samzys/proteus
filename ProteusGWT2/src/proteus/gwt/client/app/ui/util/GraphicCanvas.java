package proteus.gwt.client.app.ui.util;

import gwt.g2d.client.graphics.shapes.ShapeBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.ui.canvas.ComponentCanvasItem;
import proteus.gwt.client.app.ui.canvas.Item.State;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Font;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.modelica.AnnotationParser.GraphicExpression;

public class GraphicCanvas {
	public static final String PROPERTY_PROP_COMP = "PropertyComponent";
	private int[] connCoords = { -100, -100, 100, 100 };

	private Graphics2D g2;

	private GraphicItem[] graphicItems;

	private int iconHeight;

	private int iconWidth;

	private int[] parentCoords = { -100, -100, 100, 100 };

	private GraphicItem[] textItems;
	public float thickness = 3.0f;
	private double xscale = 1.0, yscale = 1.0;
	private Font font;
	private String paintString;
	private long drawTime;

	public GraphicCanvas(Graphics2D g2) {
		this.g2 = g2;
		// the graphicsItems will be set later
		// setGraphicItems(items);
	}

	public GraphicCanvas(Graphics2D g2, String[] str) {
		this.g2 = g2;
		if (str != null) {
			setDocument(str);
		}
	}

	/**
	 * @return the connCoords
	 */
	public int[] getConnCoords() {
		return connCoords;
	}

	public int[] getParentCoords() {
		return parentCoords;
	}

	private Dimension getSize() {
		return new Dimension(iconWidth, iconHeight);
	}

	/**
	 * @return the textItems
	 */
	public GraphicItem[] getTextItems() {
		return textItems;
	}

	public float getThickness() {
		return thickness;
	}

	public double[] getScales() {
		return new double[] { xscale, yscale };
	}

	public void paint(Graphics2D g2) {

		g2.saveTransform();
		// scale down will reduce color
		g2.scale(xscale, yscale); // this is the scale for the whole one
		// g2.setLineWidth(30/xscale);
		g2.setStroke(new BasicStroke(10));
		paintString += "It takes " + (MiscUtils.getTimeStampMilli() - drawTime)
				+ "ms to PAINT DIAGRAM<br/>";
		drawTime = MiscUtils.getTimeStampMilli();
		if (graphicItems != null) {
			for (GraphicItem item : graphicItems) {
				item.setViewRect(new proteus.gwt.shared.graphics.Rectangle(
						parentCoords[0], parentCoords[1], parentCoords[2]
								- parentCoords[0], parentCoords[3]
								- parentCoords[1]));
				item.paint(g2);
			}
		}
		g2.restoreTransform();
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void paintText(Graphics2D g2, ComponentCanvasItem cci) {
		if (textItems == null)
			return;
		for (GraphicItem item : textItems) {
			if (!(item instanceof Text))
				return;
			Text text = (Text) item;
			g2.saveTransform();
			g2.scale(xscale, yscale);
			g2.setStroke(new BasicStroke(10));
			if (item != null) {
				item.setViewRect(new proteus.gwt.shared.graphics.Rectangle(
						parentCoords[0], parentCoords[1], parentCoords[2]
								- parentCoords[0], parentCoords[3]
								- parentCoords[1]));
				// getT has to be called after setViewRect
				proteus.gwt.shared.graphics.Rectangle t = text.getT();
				if (cci != null)
					rotate_flipLabel(g2, cci.state, t);
				if (font != null)
					text.setFont(font);
				item.paint(g2);
			}
			g2.restoreTransform();

		}
	}

	public void repaint() {
		if (g2 != null) {
		}
	}

	private void rotate_flipLabel(Graphics2D g2, State state,
			proteus.gwt.shared.graphics.Rectangle t) {
		switch (state) {
		case A:
			break;
		case B:
			break;
		case C:
			g2.translate(t.x, t.y);
			g2.scale(1, -1);
			g2.translate(0, -t.getHeight());
			g2.translate(-t.x, -t.y);
			break;
		case D:
			// g2.rotate(-Math.PI, t.x + t.width / 2, t.y + t.height / 2);
			break;
		case E:
			break;
		case F:
			g2.rotate(-Math.PI, t.x + t.width / 2, t.y + t.height / 2);
			break;
		case G:
			break;
		case H:
			// g2.scale(-1, 1);
			// g2.translate(t.getWidth(), 0);
			// g2.rotate(Math.PI/2, t.x+t.width/2, t.y+t.height/2);
			break;
		default:
			break;
		}
	}

	/**
	 * @param connCoords
	 *            the connCoords to set
	 */
	public void setConnCoords(int[] connCoords) {
		this.connCoords = connCoords;
		for (GraphicItem item : graphicItems) {
			item.setConnRect(new proteus.gwt.shared.graphics.Rectangle(Math
					.min(connCoords[0], connCoords[2]), Math.min(connCoords[1],
					connCoords[3]), Math.abs(connCoords[2] - connCoords[0]),
					Math.abs(connCoords[3] - connCoords[1])));
		}
	}

	public void setDocument(String[] str) {
		try {
			List<GraphicItem> gilistRect = new LinkedList<GraphicItem>();
			List<GraphicItem> textList = new ArrayList<GraphicItem>();
			List<GraphicExpression> graphicExpressions = null;

			for (int i = str.length - 1; i >= 0; i--) {
				String text = str[i];

				if (text == null || text.length() < 2 || text.length() == 0) {
					continue;
				}
				graphicExpressions = GraphicExpression.parseGraphic(text);
				// this will set the value for graphicExpressions

				// 09 Oct 05 lt : fix for 3.1 graphic annotation
				for (GraphicExpression exp : graphicExpressions) {
					GraphicItem item = null;
					if (exp.name.equals(Line.NAME)) {
						// involves line class, where para arrow maybe an issue.
						item = new Line(this, exp.namedArguments);
					} else if (exp.name.equals(Polygon.NAME)) {
						item = new Polygon(this, exp.namedArguments);
					} else if (exp.name.equals(Rectangle.NAME)) {
						item = new Rectangle(this, exp.namedArguments);
					} else if (exp.name.equals(Ellipse.NAME)) {
						item = new Ellipse(this, exp.namedArguments);
					} else if (exp.name.equals(Text.NAME)) {
						final Text t = new Text(this, exp.namedArguments);
						textList.add(t);
						continue;
					} else if (exp.name.equals(Bitmap.NAME)) {
						// String sourceFilePath = null;
						item = new Bitmap(this, exp.namedArguments);
						// sourceFilePath);
					}
					if (item != null) {
						gilistRect.add(item);
					}
				}
			}
			setGraphicItems(gilistRect.toArray(new GraphicItem[0]));
			setTextItems(textList.toArray(new GraphicItem[0]));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void setGraphicItems(GraphicItem[] graphicItems) {
		this.graphicItems = graphicItems;

	}

	public void setParentCoords(int[] parentCoords) {
		this.parentCoords = parentCoords;
	}

	public void setSize(int iconWidth, int iconHeight) {
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}

	/**
	 * @param textItems
	 *            the textItems to set
	 */
	public void setTextItems(GraphicItem[] textItems) {
		this.textItems = textItems;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
		if (graphicItems != null) {
			for (GraphicItem item : graphicItems) {
				if (item instanceof Rectangle) {
					((Rectangle) item).setLineThickness(thickness);
				} else if (item instanceof Line) {
					((Line) item).setLineThickness(thickness);
				}
			}
		}

		if (textItems != null) {
			for (GraphicItem item : textItems) {
				if (item instanceof Text) {
					// @sam workout for setting the font size for icon and
					// diagram
					((Text) item).setFontSize((int) thickness * 10);
				}
			}
		}
	}

	// 09 Oct 12 lt : fix for coordinate system conversion
	public void updateScale() {
		int parentWidth = Math.abs(parentCoords[2] - parentCoords[0]);
		int parentHeight = Math.abs(parentCoords[3] - parentCoords[1]);

		Dimension size = getSize();
		if (parentWidth == 0 || parentHeight == 0) {
			xscale = size.width / 200.0;
			yscale = size.height / 200.0;
		} else {
			xscale = (double) size.width / parentWidth;
			yscale = (double) size.height / parentHeight;
		}
		repaint();
	}

}
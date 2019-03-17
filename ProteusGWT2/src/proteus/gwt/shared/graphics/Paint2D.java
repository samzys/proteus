package proteus.gwt.shared.graphics;

import gwt.g2d.client.graphics.Gradient;
import gwt.g2d.client.graphics.ImageLoader;
import gwt.g2d.client.graphics.LineCap;
import gwt.g2d.client.graphics.LineJoin;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.graphics.TextAlign;
import gwt.g2d.client.graphics.TextBaseline;
import gwt.g2d.client.graphics.canvas.CanvasPixelArray;
import gwt.g2d.client.graphics.canvas.ImageDataAdapter;
import gwt.g2d.client.graphics.shapes.Shape;
import gwt.g2d.client.graphics.shapes.ShapeBuilder;
import gwt.g2d.client.graphics.visitor.EllipseVisitor;
import gwt.g2d.client.math.Matrix;
import gwt.g2d.client.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.client.app.ui.util.GraphicConstants.LinePattern;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.util.Utils;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;

public class Paint2D extends Surface implements Graphics2D {

	private final static int CLEARGAP = 4;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gwt.g2d.client.graphics.Surface#getLineWidth()
	 */
	@Override
	public double getLineWidth() {
		return super.getLineWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gwt.g2d.client.graphics.Surface#setLineWidth(double)
	 */
	@Override
	public Surface setLineWidth(double lineWidth) {
		return super.setLineWidth(lineWidth);
	}

	private static final double toRadians(double degrees) {
		return degrees * Math.PI / 180;
	}

	private static final double toRadians(int degrees) {
		return degrees * Math.PI / 180;
	}

	private Color bgColor = Color.WHITE;

	private Color color;

	private Cursor cursor;

	private BasicStroke stroke;

	private Font font;

	private LinePattern linePattern = LinePattern.SOLID;

	private boolean clearFlag = false;

	private int width;

	private int height;

	public Paint2D(int i, int j) {
		super(i, j);
		this.width = i;
		this.height = j;
	}

	public void clearCanvas() {
		clear();
		setPaint(bgColor);
		fillRect(1, 1, width-1, height-1);
	}

	@Override
	public void draw(Line2D l) {
		drawLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
	}

	public void drawArc(int x, int y, int width, int height, int startAngle,
			int endAngle) {
		if (clearFlag) {
			clearRect(x, y, width, height);
		} else {
			if (width != height) {
				width = height = (width + height) / 2;
			}
			super.strokeShape(new ShapeBuilder().drawArc(x, y, width,
					startAngle, endAngle, true).build());
		}
	}

	/**
	 * @see EllipseVisitor#EllipseVisitor(double, double, double, double)
	 */
	public void drawEllipse(double x, double y, double width, double height) {
		if (clearFlag) {
			clearRect(x, y, width, height);
		} else
			super.strokeShape(new ShapeBuilder().drawEllipse(x, y, width,
					height).build());
	}

	public void fillEllipse(double x, double y, double width, double height) {
		super.fillShape(new ShapeBuilder().drawEllipse(x, y, width, height)
				.build());
	}

	protected Map<String, ImageElement> imageElementMap = new HashMap<String, ImageElement>();

	private float[] dashArray;

	public void drawImage(final Image image, final int x, final int y) {
		final String url = MiscUtils.URLTool(image.getUrl());

		if (imageElementMap.containsKey(url)) {
			ImageElement ie = imageElementMap.get(url);
			drawImage(ie, x, y);
		} else {
			ImageLoader.loadImages(new String[] { url },
					new ImageLoader.CallBack() {
						public void onImagesLoaded(ImageElement[] imageElements) {
							for (ImageElement imageElement : imageElements) {
								imageElementMap.put(url, imageElement);
								drawImage(imageElementMap.get(url), x, y);
							}
						}
					});
		}
	}

	public void paintImage(ImageElement image, int x, int y, int width,
			int height) {
		super.drawImage(image, x, y, width, height);
	}

	public void drawImage(Image image, final int x, final int y,
			final int width, final int height, Object observer) {
		final String url = MiscUtils.URLTool(image.getUrl());
		if (imageElementMap.containsKey(url)) {
			ImageElement ie = imageElementMap.get(url);
			paintImage(ie, x, y, width, height);
		} else {
			ImageLoader.loadImages(new String[] { url },
					new ImageLoader.CallBack() {
						public void onImagesLoaded(ImageElement[] imageElements) {
							for (ImageElement imageElement : imageElements) {
								imageElementMap.put(url, imageElement);
							}
							ProteusGWT.eventBus
									.fireEvent(new RepaintCanvasEvent());
						}
					});
		}
	}

	public void drawLine(double x1, double y1, double x2, double y2) {
		if (clearFlag) {
			clearLine(x1, y1, x2, y2);
		} else {
			if (dashArray == null) {
				super.strokeShape(new ShapeBuilder().drawLineSegment(x1, y1,
						x2, y2).build());
			} else if (dashArray.length == 2) {
				super.strokeShape(new ShapeBuilder().drawDashedLine(x1, y1, x2,
						y2, dashArray[0], dashArray[1]).build());
			} else if (dashArray.length == 4) {
				super.strokeShape(new ShapeBuilder().drawDashedLine(x1, y1, x2,
						y2, dashArray[0], dashArray[1]).build());
				super.strokeShape(new ShapeBuilder().drawDashedLine(x1, y1, x2,
						y2, dashArray[2], dashArray[3]).build());
			}
		}
	}

	private void clearLine(double x1, double y1, double x2, double y2) {
		if ((x1 - x2) == 0) {
			super.clearRectangle(x1 - CLEARGAP, Math.min(y1, y2) - CLEARGAP,
					CLEARGAP * 2, Math.abs(y1 - y2) + CLEARGAP * 2);
		} else if ((y1 - y2) == 0) {
			super.clearRectangle(Math.min(x1, x2) - CLEARGAP, y1 - CLEARGAP,
					Math.abs(x1 - x2) + CLEARGAP * 2, CLEARGAP * 2);
		} else {
			double length = Math.abs(x1 - x2);
			double theta = Math.atan2(Math.abs(y1 - y2), Math.abs(x1 - x2));
			saveTransform();
			if (x1 < x2) {
				super.translate(x1, y1);
				super.rotate(-theta);
				clearRect(0, 0, x1 + length, 0);
			} else {
				super.translate(x2, y2);
				super.rotate(-theta);
				clearRect(0, 0, x2 + length, 0);
			}
			restoreTransform();
		}
	}

	public void drawOval(int x, int y, int width, int height) {
		drawArc(x, y, width, height, 0, 360);
	}

	public void drawPolygon(int[] xpoints, int[] ypoints, int npoints,
			Rectangle t) {
		if (clearFlag) {
			clearRect(t.x, t.y, t.width, t.height);
		} else {
			if (npoints < 2) {
				return;
			}
			ShapeBuilder sb = new ShapeBuilder();
			sb.moveTo(xpoints[0], ypoints[0]);
			for (int i = 1; i < npoints; i++) {
				sb.drawLineTo(xpoints[i], ypoints[i]);
			}
			super.strokeShape(sb.build());
		}
	}

	public void StrokeShape(ShapeBuilder sb) {
		super.strokeShape(sb.build());
	}

	public void drawPolyline(ShapeBuilder sb, double[] xpoints,
			double[] ypoints, int npoints) {
		if (npoints < 2) {
			return;
		}

		sb.moveTo(xpoints[0], ypoints[0]);
		double x = xpoints[0], y = ypoints[0];

		if (dashArray == null) {
			for (int i = 1; i < npoints; i++) {
				sb.drawLineSegment(x, y, xpoints[i], ypoints[i]);
				x = xpoints[i];
				y = ypoints[i];
			}
		} else if (dashArray.length == 2 || dashArray.length == 4) {
			for (int i = 1; i < npoints; i++) {
				sb.drawDashedLine(x, y, xpoints[i], ypoints[i], dashArray[0],
						dashArray[1]);
				x = xpoints[i];
				y = ypoints[i];
			}
		}
	}

	
	public void drawPolyline(int[] xpoints, int[] ypoints, int npoints) {
		if (npoints < 2) {
			return;
		}
		ShapeBuilder sb = new ShapeBuilder();
		sb.moveTo(xpoints[0], ypoints[0]);
		int x = xpoints[0], y = ypoints[0];

		if (dashArray == null) {
			for (int i = 1; i < npoints; i++) {
				sb.drawLineSegment(x, y, xpoints[i], ypoints[i]);
				x = xpoints[i];
				y = ypoints[i];
			}
		} else if (dashArray.length == 2 || dashArray.length == 4) {
			for (int i = 1; i < npoints; i++) {
				sb.drawDashedLine(x, y, xpoints[i], ypoints[i], dashArray[0],
						dashArray[1]);
				x = xpoints[i];
				y = ypoints[i];
			}
		}
		super.strokeShape(sb.build());
	}

	public void drawRect(double x, double y, double width, double height) {
		if (clearFlag) {
			clearRect(x, y, width, height);
		} else {
			super.strokeRectangle(x, y, width, height);
		}
	}

	private void clearRect(double x, double y, double width, double height) {
		super.clearRectangle(x - CLEARGAP, y - CLEARGAP, width + CLEARGAP * 2,
				height + CLEARGAP * 2);
	}

	public void drawString(String str, int x, int y) {
		if (clearFlag) {
			int[] extent = getStringSize(str);
			clearRect(x, y - 30, extent[0], 35);
		} else
			super.fillText(str, x, y);
	}

	@Override
	public void fill(Rectangle r) {
		if (clearFlag) {

		} else
			fillRectangle(r.x, r.y, r.width, r.height);
	}

	@Override
	public void fill(Shape s) {
	}

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int endAngle) {
		if (width != height) {
			width = height = (width + height) / 2;
		}
		if (clearFlag) {

		} else
			super.fillShape(new ShapeBuilder().drawArc(x, y, width, startAngle,
					endAngle, true).build());
	}

	public void fillOval(int x, int y, int width, int height) {
		if (clearFlag) {
			// TODO
		} else
			fillArc(x, y, width, height, 0, 360);
	}

	public void fillPolygon(int[] xpoints, int[] ypoints, int npoints) {
		if (clearFlag) {

		} else {

			if (npoints < 2) {
				return;
			}

			ShapeBuilder sb = new ShapeBuilder();
			sb.moveTo(xpoints[0], ypoints[0]);
			for (int i = 1; i < npoints; i++) {
				sb.drawLineTo(xpoints[i], ypoints[i]);
			}
			super.fillShape(sb.build());
		}
	}

	public void fillRect(double x, double y, double width, double height) {
		if (clearFlag) {
			clearRect(x, y, width, height);
		} else
			super.fillRectangle(x, y, width, height);
	}

	public void fillRect(int x, int y, int width, int height) {
		if (clearFlag) {
			// TODO
		} else
			super.fillRectangle(x, y, width, height);
	}

	public Color getBackground() {
		return bgColor;
	}

	@Override
	public Rectangle getClipBounds() {
		return null;
	}

	public Color getColor() {
		return color;
	}

	public int[] getImageSize(final Image image) {
		int[] size = { 8, 8 };

		return size;
	}

	@Override
	public int[] getRGBArray(int x, int y, int w, int h) {
		ImageDataAdapter imageData = this.getImageData(x, y, w, h);
		CanvasPixelArray pxArray = imageData.getPixelData();

		int size = pxArray.getLength() / 4; // w*h
		int[] array = new int[size];
		for (int j = 0; j < h; ++j) {
			for (int i = 0; i < w; ++i) {
				int index = j * w + i;
				int r = pxArray.getData(index * 4);
				int g = pxArray.getData(index * 4 + 1);
				int b = pxArray.getData(index * 4 + 2);
				int a = pxArray.getData(index * 4 + 3);
//				System.out.println(r + ", " + g + ", " + b + ", " + a);
				array[index] = new Color(r, g, b, a).getRGB();
			}
		}
		return array;
	}

	public int[] getStringSize(String str) {
		int width = (int) (super.measureText(str) + 0.5);
		// TextMeasurer tmh = new TextMeasurer();
		return new int[] { width, 11 };
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	@Override
	public void preloadImages(String[] urls, LoadImagesCallback callback) {

	}

	private void restoreContext() {
		restoreTransform();
	}

	public void restoreTransform() {
		super.restore();
	}

	@Override
	public Surface rotate(double theta, double x, double y) {
		translate(x, y);
		rotate(theta);
		translate(-x, -y);
		return this;
	}

	private void saveContext() {
		saveTransform();
	}

	public void saveTransform() {
		super.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gwt.g2d.client.graphics.Surface#scale(double)
	 */
	@Override
	public Surface scale(double scale) {
		return super.scale(scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gwt.g2d.client.graphics.Surface#scale(double, double)
	 */
	@Override
	public Surface scale(double x, double y) {
		Surface sur = super.scale(x, y);
		if (color != null)
			setColor(color);
		return sur;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gwt.g2d.client.graphics.Surface#scale(gwt.g2d.client.math.Vector2)
	 */
	@Override
	public Surface scale(Vector2 scales) {
		return super.scale(scales);
	}

	public void setAlign(TextAlign align) {
		super.setTextAlign(align);
	}

	public void setBackground(Color color) {
		bgColor = color;
	}

	public void setBaseline(TextBaseline baseline) {
		super.setTextBaseline(baseline);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {

	}

	public void setColor(Color c) {
		this.color = c;
		gwt.g2d.client.graphics.Color c1 = new gwt.g2d.client.graphics.Color(c
				.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		super.setFillStyle(c1);
		super.setStrokeStyle(c1);
	}

	public void setCursor(Cursor cursor) {
		if (this.cursor == cursor) {
			return;
		}

		switch (cursor.getCursor()) {
		case Cursor.MOVE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "move");
			break;
		case Cursor.CROSSHAIR_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "crosshair");
			break;
		case Cursor.HAND_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "hand");
			break;
		case Cursor.E_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "e-resize");
			break;
		case Cursor.N_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "n-resize");
			break;
		case Cursor.NE_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "ne-resize");
			break;
		case Cursor.NW_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "nw-resize");
			break;
		case Cursor.S_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "s-resize");
			break;
		case Cursor.SE_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "se-resize");
			break;
		case Cursor.SW_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "sw-resize");
			break;
		case Cursor.W_RESIZE_CURSOR:
			DOM.setStyleAttribute(this.getElement(), "cursor", "w-resize");
			break;
		case Cursor.CUSTOM:
			DOM.setStyleAttribute(this.getElement(), "cursor", cursor
					.getCursorName());
			break;
		case Cursor.DEFAULT_CURSOR:
		default:
			DOM.setStyleAttribute(this.getElement(), "cursor", "default");
			break;
		}
	}

	@Override
	public void setFont(Font font) {
		this.font = font;
		super.setFont(font.toString());
	}

	public void setPaint(Color color) {
		setColor(color);
	}

	@Override
	public void setStroke(BasicStroke s) {
		this.stroke = s;
		super.setLineWidth(s.width);
		super.setMiterLimit(s.getMiterLimit());
		switch (s.getEndCap()) {
		case BasicStroke.CAP_BUTT:
			super.setLineCap(LineCap.BUTT);
			break;
		case BasicStroke.CAP_ROUND:
			super.setLineCap(LineCap.ROUND);
			break;
		case BasicStroke.CAP_SQUARE:
			super.setLineCap(LineCap.SQUARE);
			break;
		}
		switch (s.getLineJoin()) {
		case BasicStroke.JOIN_ROUND:
			super.setLineJoin(LineJoin.ROUND);
			break;
		case BasicStroke.JOIN_BEVEL:
			super.setLineJoin(LineJoin.BEVEL);
			break;
		case BasicStroke.JOIN_MITER:
			super.setLineJoin(LineJoin.MITER);
			break;
		}
		dashArray = s.getDashArray();
	}

	@Override
	public Surface transform(Matrix matrix) {
		return super.transform(matrix);
	}

	@Override
	public void setPaint(Gradient gradient) {
		super.setFillStyle(gradient);
	}

	@Override
	public void clearRectangle(Rectangle2D.Double rectangle) {
		super.clearRectangle(rectangle.x, rectangle.y, rectangle.width,
				rectangle.height);
	}

	@Override
	public void draw(Shape s) {
	}
}

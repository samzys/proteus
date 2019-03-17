/**
 * 
 */
package proteus.gwt.shared.graphics;

import gwt.g2d.client.graphics.Gradient;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.graphics.TextAlign;
import gwt.g2d.client.graphics.TextBaseline;
import gwt.g2d.client.graphics.shapes.Shape;
import gwt.g2d.client.graphics.shapes.ShapeBuilder;
import proteus.gwt.shared.graphics.geom.Line2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

public interface Graphics2D {

	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle);

	public void drawImage(Image img, int x, int y);

	public void paintImage(ImageElement image, int x, int y, int width,
			int height);

	public void drawImage(Image img, int x, int y, int width, int height,
			Object observer);

	public void drawEllipse(double x, double y, double width, double height);


	public void fillEllipse(double x, double y, double width, double height);


	public void drawLine(double x1, double y1, double x2, double y2);

	public void drawOval(int x, int y, int width, int height);

	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints,
			Rectangle t);

	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);

	public void drawRect(double x, double y, double width, double height);

	public void drawString(String str, int x, int y);

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle);

	public void fillOval(int x, int y, int width, int height);

	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints);


	public void fillRect(int x, int y, int width, int height);

	public void fillRect(double x, double y, double width, double height);

	public Color getColor();

	public void setColor(Color c);

	public void setPaint(Color color); // same as setColor()

	public void setFont(Font font);

	public void draw(Shape s);

	public void fill(Shape s);

	public Color getBackground();

	// public AlphaComposite getComposite();
	//
	// public BasicStroke getStroke();

	public void saveTransform();

	public Surface rotate(double theta);

	public Surface rotate(double theta, double x, double y);

	public Surface scale(double sx, double sy);

	public Surface translate(double x, double y);

	public void setBackground(Color color);

	// public void setComposite(AlphaComposite comp);
	//
	// public void setStroke(BasicStroke s);

	public void restoreTransform();

	// //////

	public void setClip(int x, int y, int width, int height);

	public Rectangle getClipBounds();

	// //////

	public void setAlign(TextAlign align);

	public void setBaseline(TextBaseline baseline);

	public int[] getStringSize(String text);

	public int[] getImageSize(Image img);

	public void preloadImages(String[] urls, LoadImagesCallback callback);

	public interface LoadImagesCallback {
		public void onImagesLoaded();
	}

	// //////

	public int[] getRGBArray(int x, int y, int w, int h);

	public void setStroke(BasicStroke stroke);

	public void fill(Rectangle aroundCon);

	public BasicStroke getStroke();

	public void draw(Line2D l);

	public void setPaint(Gradient gradient);

	public double getLineWidth();

	public Surface setLineWidth(double lineWidth);

	public void clearRectangle(Rectangle2D.Double rectangle);

}

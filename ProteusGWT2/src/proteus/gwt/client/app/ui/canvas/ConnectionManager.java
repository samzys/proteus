package proteus.gwt.client.app.ui.canvas;

import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.shared.graphics.BasicStroke;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Cursor;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.graphics.geom.Line2D;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

public class ConnectionManager extends ModeHandler {
	private static final BasicStroke CONN_IN_PROGRESS_STROKE = new BasicStroke(
			2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0.0f,
			new float[] { 5.0f, 5.0f }, 0.0f);

	protected DiagramCanvas canvas;

	private List<Point> cornerPoints = new LinkedList<Point>();

	private boolean draw;

	private int mx, my;

	public ConnectionManager(DiagramCanvas canvas, int mode) {
		super(mode);
		this.canvas = canvas;
	}

	public void finish() {
		draw = false;
	}

	public boolean canPaint() {
		return true;
	}

	@Override
	public void init(Object... args) {
		 canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void paint(Graphics2D g2) {
		
		ConnectorItem src = canvas.getSource();		

		if (draw && src != null) {
			ComponentCanvasItem cci = src.getComponentCanvasItem();

			// This is to highlight the start point of connection while drawing
			// the connection line
			
			g2.setColor(Color.GREEN);
			
			g2.fillRect((int) ((cci.getX() + src.getX())), (int)((cci.getY() + src.getY())), (int)(src
					.getWidth()), (int) (src.getHeight()));

			BasicStroke s = g2.getStroke();
			g2.setStroke(CONN_IN_PROGRESS_STROKE);

			Point start = new Point((int)(cci.getX() + src.getX() + src.getHitX()),
					(int)(cci.getY() + src.getY() + src.getHitY()));

			// maryam commented start: because there is no more inserted intermediate points
			/*for (Point p : con.getCornerPoints()) {				
				g2.drawLine(start.x, start.y, p.x, p.y);				
				start = p;
			}*/

			// This is to show movement of mouse while drawing the connection line,
			// it depends on whether the movement is in X direction or Y direction
			Line2D l;
			if (Math.abs(start.getX() - mx) > Math.abs(start.getY() - my)) {
				l = new Line2D.Double(start.getX(), start.getY(), mx, start.getY());
				g2.draw(l);
				l = new Line2D.Double(mx, start.getY(), mx, my);
				g2.draw(l);
			} else {
				l = new Line2D.Double(start.getX(), start.getY(), start.getX(), my);
				g2.draw(l);
				l = new Line2D.Double(start.getX(), my, mx, my);
				g2.draw(l);				
			}
			g2.setStroke(s);			
		}
	}

	public Point[] getCornerPoints() {
		return cornerPoints.toArray(new Point[0]);
	}


	@Override
	public void onMouseMove(MouseMoveEvent event) {
		draw = true;
		Point p  = MiscUtils.scaleLoc(event.getX(), event.getY(), canvas.getCanvasScale());
		mx = p.x;
		my = p.y;
	}
}

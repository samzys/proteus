package proteus.gwt.client.app.ui.canvas;

import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Point2D;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.Placement.Transformation;

import com.google.gwt.user.client.ui.FocusWidget;

public abstract class Item extends FocusWidget {

	// These are the states of combined flip&rotate state-machine, please refer
	// to related document
	public enum State {
		A, B, C, D, E, F, G, H
	}

	// @maryam: To combine flip and rotate states machines
	// These are the states of combined flip & rotate state-machine, please
	// refer to the related document
	public static final String A = "A", B = "B", C = "C", D = "D", E = "E",
			F = "F", G = "G", H = "H";
	protected Rectangle _bounds = new Rectangle();

	protected Rectangle2D.Double bounds = new Rectangle2D.Double();

	public State ex_state = State.A;

	public boolean focus = false;

	public String name;

	protected Rectangle2D.Double normBounds = new Rectangle2D.Double();

	public State state = State.A;

	// 09 Oct 09 lt
	public Transformation transformation;

	public Rectangle get_bounds() {
		return new Rectangle(_bounds);
	}

	public double getAngleRadian() {
		if (state.equals(State.A)) {
			return 0.0;
		} else if (state.equals(State.D)) {
			return Math.PI / 2;
		} else if (state.equals(State.F)) {
			return Math.PI;
		} else if (state.equals(State.E)) {
			return Math.PI * 3 / 2;
		} else {
			return 0.0;
		}
	}

	/**
	 * @return the bounds
	 */
	public Rectangle2D.Double getBounds() {
		return bounds;
	}

	public Rectangle getBoundsRect() {
		return new Rectangle(Math.round((float) bounds.x), Math.round((float)  bounds.y),
				Math.round((float)  bounds.width), Math.round((float)  bounds.height));
	}

	public State getExState() {
		return ex_state;
	}

	public double getHeight() {
		return bounds.height;
	}

	public String getName() {
		return name;
	}

	public Rectangle2D.Double getNormBounds() {
		return new Rectangle2D.Double(normBounds.x, normBounds.y,
				normBounds.width, normBounds.height);
	}

	public Rectangle getNormBounds(Rectangle rect) {
		rect.x = Math.round((float)normBounds.x);
		rect.y = Math.round((float)normBounds.y);
		rect.width = Math.round((float)normBounds.width);
		rect.height = Math.round((float) normBounds.height);
		return rect;
	}

	public Rectangle2D.Double getNormBounds(Rectangle2D.Double rect) {
		rect.x = normBounds.x;
		rect.y = normBounds.y;
		rect.width = normBounds.width;
		rect.height = normBounds.height;
		return rect;
	}
	
	/**
	 * @return
	 * @this getExtentSign and getRotation method have to be worked in pair..
	 * because this is only one of the action which can lead to that state.
	 * and the order of flip and rotate matters. here we make rotate after flip..
	 */
	protected int[] getExtentSign() {
		int[] esign = new int[4];
		if (state.equals(State.A)||state.equals(State.E)||state.equals(State.D)||state.equals(State.F)) {
			esign[0] = -1;
			esign[1] = -1;
			esign[2] = 1;
			esign[3] = 1;
		} else if (state.equals(Item.State.B)||state.equals(State.H)) {
			esign[0] = 1;
			esign[1] = -1;
			esign[2] = -1;
			esign[3] = 1;
		} else if (state.equals(Item.State.C)||state.equals(State.G)) {
			esign[0] = -1;
			esign[1] = 1;
			esign[2] = 1;
			esign[3] = -1;
		}else {
			esign[0] = -1;
			esign[1] = -1;
			esign[2] = 1;
			esign[3] = 1;
		}
		return esign;
	}


	public int getRotation() {
		// return parent's rotation, which has been set already
		//@previous code is to return transformation rotation first...
		if (state.equals(State.A)) {// 0
			return 0;
		} else if (state.equals(State.D)) {
			return 90;
		} else if (state.equals(State.F)) {
			return 180;
		} else if (state.equals(State.E)) {
			return 270;
		} else if(state.equals(State.H)){
			return 90;
		}else if(state.equals(State.G)) {
			return 90;
		}else {
			return 0;
		}
		
//		if (transformation != null) {
//			return transformation.rotation;
//		} else {
//			
//		}
	}

	public int _getRotation() {
		// return parent's rotation, which has been set already
		//@previous code is to return transformation rotation first...
		if (state.equals(State.A)) {// 0
			return 0;
		} else if (state.equals(State.D)) {
			return 90;
		} else if (state.equals(State.F)) {
			return 180;
		} else if (state.equals(State.E)) {
			return 270;
		} else {
			// @sam default should be 0.
			return 0;
		}
		
//		if (transformation != null) {
//			return transformation.rotation;
//		} else {
//			
//		}
	}
	public Dimension getSize() {
		return new Dimension((int) bounds.width, (int) bounds.height);
	}

	public State getState() {
		return state;
	}

	// rectangle when selecting
	// an item, starting from
	// top, left most corner
	public double getWidth() {
		return bounds.width;
	}

	public double getX() {
		return bounds.x;
	}

	public double getY() {
		return bounds.y;
	}

	public boolean isFocus() {
		return focus;
	}

	public abstract void paint(Graphics2D g);

	public void set_bounds(Rectangle bounds) {
		_bounds = bounds;
	}

	public void setBounds(int x, int y, int width, int height) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
		updateNormBounds();
	}

	public void setBounds(double x, double y, double width, double height) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
		updateNormBounds();
	}

	public void setBounds(Rectangle r) {
		setBounds(r.x, r.y, r.width, r.height);
	}
	public void setBounds(Rectangle2D.Double r) {
		setBounds(r.x, r.y, r.width, r.height);
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public void setHeight(double height) {
		bounds.height = height;
		updateNormBounds();
	}

	public void setLocation(double x, double y) {
		bounds.x = x;
		bounds.y = y;
		updateNormBounds();
	}

	public void setLocation(Point2D.Double p) {
		setLocation(p.x, p.y);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNormBounds(double x, double y, double width, double height) {
		normBounds.x = x;
		normBounds.y = y;
		normBounds.width = width;
		normBounds.height = height;
	}

	public void setSize(double width, double height) {
		bounds.width = width;
		bounds.height = height;
		// bounds.setSize(width, height);
		updateNormBounds();
		// repaint();
	}

	public void setState(State value, State ex_value) {
		this.state = value;
		this.ex_state = ex_value;
		updateNormBounds();
	}

	public void setWidth(double width) {
		bounds.width = width;
		updateNormBounds();
	}

	public void setX(double x) {
		bounds.x = x;
		updateNormBounds();
	}

	public void setY(double y) {
		bounds.y = y;
		updateNormBounds();
	}

	private void updateNormBounds() {

		if (state.equals(State.D) || state.equals(State.E)
				|| state.equals(State.H) || state.equals(State.G)) {// 90, 270
			setNormBounds(bounds.x + (double) (bounds.width - bounds.height)
					/ 2,
					bounds.y + (double) (bounds.height - bounds.width) / 2,
					bounds.height, bounds.width);
		} else {
			setNormBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		}

	}

	protected void updateNormBounds(double x, double y, double width,
			double height) {
		if (state.equals(State.D) || state.equals(State.E)
				|| state.equals(State.H) || state.equals(State.G)) {// 90, 270
			// if(this instanceof ComponentCanvasItem) {
			setNormBounds(x + (width - height) / 2, y + (height - width) / 2,
					height, width);
			// }
			// setNormBounds(y + (height - width) / 2, x + (width - height) / 2,
			// height, width);
		} else {
			setNormBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}
}

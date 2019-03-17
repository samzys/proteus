package proteus.gwt.client.app.ui.canvas;

import proteus.gwt.client.app.ui.util.GraphicCanvas;
import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Font;
import proteus.gwt.shared.graphics.Graphics2D;

public abstract class Icon {

	protected int iconWidth, iconHeight;

	protected int shadowSize = 0;

	public boolean isDiagram = false;// added by @Gao Lei Nov.30 2010

	public float thickness = 3.0f;// @sam.March 7, 2011

	public Icon() {

	}
	
	public void paintText(Graphics2D g2, ComponentCanvasItem cci, Font font) {
	}

	public void setConnCoordis(int[] connCoords) {

	}

	/**
	 * @return the thickness
	 */
	public float getThickness() {
		return thickness;
	}

	/**
	 * @param thickness
	 *            the thickness to set
	 */
	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public int getIconWidth() {
		return iconWidth;
	}

	public int getWidth() {
		return iconWidth;
	}

	public int getHeight() {
		return iconHeight;
	}

	public void setIconWidth(int iconWidth) {
		setIconSize(iconWidth, iconHeight);
	}

	public int getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(int iconHeight) {
		setIconSize(iconWidth, iconHeight);
	}

	public Dimension getIconSize() {
		return new Dimension(iconWidth, iconHeight);
	}

	public abstract void paint(Graphics2D g);

	public abstract Icon copy(Object... args);

	public static class ImageImpl extends Icon {

		@Override
		public Icon copy(Object... args) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void paint(Graphics2D g) {
		}

	}

	public static class ModelicaImpl extends Icon {
		// private boolean libIcon = false;

		// private StringProperty textProp, coordSysProp;

		private final GraphicCanvas canvas;

		// private ComponentCanvasItem componentCanvasItem;

		// private ComponentCanvas componentCanvas;

		/**
		 * @return the canvas
		 */
		public GraphicCanvas getCanvas() {
			return canvas;
		}

		public static final int defaultSize = 60;

		// 09 Oct 12 lt
		private int[] extent = { -100, -100, 100, 100 };

		/**
		 * @sam extent the extent to set
		 */
		public void setExtent(int[] extent) {
			this.extent = extent;
			canvas.setParentCoords(extent);
		}

		public ModelicaImpl(Graphics2D g2, String[] str) {
			canvas = new GraphicCanvas(g2, str);
			setIconSize(defaultSize, defaultSize);
		}

		public double[] getconnScale() {
			return canvas.getScales();
		}

		public ModelicaImpl(Graphics2D g2, int[] extent2) {
			this.canvas = new GraphicCanvas(g2);
			int _width = Math.abs(extent2[2] - extent2[0]);
			int _height = Math.abs(extent2[3] - extent2[1]);
			int oldWidth = Math.abs(extent[2] - extent[0]);
			int oldHeight = Math.abs(extent[3] - extent[1]);
			int widthSize = _width * defaultSize / oldWidth;
			int heightSize = _height * defaultSize / oldHeight;
			setExtent(extent2);
			setIconSize(widthSize, heightSize);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see proteus.gwt.client.app.ui.canvas.Icon#getThickness()
		 */
		@Override
		public float getThickness() {
			return super.getThickness();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see proteus.gwt.client.app.ui.canvas.Icon#setThickness(float)
		 */
		@Override
		public void setThickness(float thickness) {
			super.setThickness(thickness);
			canvas.setThickness(thickness);
		}

		public int[] getExtent() {
			return this.extent;
		}

		// 09 oct 12 lt : fix for coordinate system conversion
		public void setParentCoords(int[] parentCoords) {
			canvas.setParentCoords(parentCoords);
		}

		public void setConnCoordis(int[] connCoords) {
			canvas.setConnCoords(connCoords);
		}

		public void setIconSize(int iconWidth, int iconHeight) {
			super.setIconSize(iconWidth, iconHeight);
			canvas.setSize(iconWidth, iconHeight);
			canvas.updateScale();
		}

		@Override
		public Icon copy(Object... args) {
			return null;
		}

		@Override
		public void paint(Graphics2D g) {
			g.saveTransform();
			canvas.paint(g);
			g.restoreTransform();
		}

		@Override
		public void paintText(Graphics2D g2, ComponentCanvasItem cci, Font font) {
			g2.saveTransform();
			canvas.setFont(font);
			canvas.paintText(g2, cci);
			g2.restoreTransform();
		}
	}

	public static class DefaultImpl extends Icon {

		private String caption;

		public DefaultImpl(String caption) {
		}

		public Icon copy(Object... args) {
			Icon copy = new DefaultImpl(caption);
			return copy;
		}

		@Override
		public void paint(Graphics2D g) {

		}

	}

	public void setIconSize(int iconWidth, int iconHeight) {
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}

	public int getX() {
		return 0;
	}

	public int getY() {
		return 0;
	}
	public double[] getconnScale() {
		return null;
	}
}

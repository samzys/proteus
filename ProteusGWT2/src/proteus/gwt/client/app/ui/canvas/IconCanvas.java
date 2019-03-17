package proteus.gwt.client.app.ui.canvas;

import java.util.List;

import proteus.gwt.client.app.event.MouseEventHandler;
import proteus.gwt.client.app.ui.canvas.Item.State;
import proteus.gwt.client.app.ui.util.Constant;
import proteus.gwt.shared.graphics.Dimension;
import proteus.gwt.shared.graphics.Font;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.graphics.geom.Rectangle2D;
import proteus.gwt.shared.modelica.ClassDef;
import proteus.gwt.shared.modelica.Component;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

/**
 * @author sam
 * 
 */
// this class can be used for user to draw shapes to define user defined icon
public class IconCanvas extends Canvas {
	private class MouseEventsHandler extends MouseEventHandler {
		IconCanvas canvas;

		public MouseEventsHandler(IconCanvas canvas) {
			this.canvas = canvas;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * proteus.gwt.client.app.event.MouseEventHandler#onMouseOut(com.google
		 * .gwt.event.dom.client.MouseOutEvent)
		 */
		@Override
		public void onMouseOut(MouseOutEvent event) {
			// TODO Auto-generated method stub
			super.onMouseOut(event);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * proteus.gwt.client.app.event.MouseEventHandler#onClick(com.google
		 * .gwt.event.dom.client.ClickEvent)
		 */
		@Override
		public void onClick(ClickEvent event) {
			super.onClick(event);
			repaint();
		}

	}

	private Icon icon;
	private Component comp;
	private List<ConnectorItem> connItems;

	public IconCanvas(CanvasModel model) {
		super(model);
		new MouseEventsHandler(this);
	}

	public void setClassDef(ClassDef cd) {
		// not handle non component classDef
		if (!(cd instanceof Component))
			return;
		comp = (Component) cd;
		connItems = comp.getConnectorItems(this);
		for (ConnectorItem connItem : connItems) {
			// set the flip and rotation information;
			if (connItem.connector.getTransformation() != null) {
				int[] ext = connItem.connector.getTransformation().oldExtent;
				// flip then rotate
				if (ext != null) {
					// set flip for each components
					if (ext[2] < ext[0] && ext[3] >= ext[1]) {
						CanvasUtils.flipHorizontal(connItem);
					} else if (ext[2] >= ext[0] && ext[3] < ext[1]) {
						CanvasUtils.flipVertical(connItem);
					} else if (ext[2] < ext[0] && ext[3] < ext[1]) {
						CanvasUtils.flipHorizontal(connItem);
						CanvasUtils.flipVertical(connItem);
					}
				}

				switch (connItem.connector.getRotation()) {
				case 0:
					// connItem.setState(State.A, State.A);
					break;
				case 90:
					// connItem.setState(State.D, State.D);
					CanvasUtils.rotateAntiClockwise(connItem);
					break;
				case 180:
					// connItem.setState(State.F, State.F);
					CanvasUtils.rotateAntiClockwise(connItem);
					CanvasUtils.rotateAntiClockwise(connItem);
					break;
				case 270:
					// connItem.setState(State.E, State.E);
					CanvasUtils.rotateAntiClockwise(connItem);
					CanvasUtils.rotateAntiClockwise(connItem);
					CanvasUtils.rotateAntiClockwise(connItem);
					break;
				case -1:
					// this item doesn't have rotation attribute
					break;
				default:
					connItem.setState(State.A, State.A);
					break;
				}

			}
		}

		icon = comp.getIconCanvasIcon(this);

		if (icon != null) {
			init();
		}
	}

	private void init() {
		int width = icon.getIconWidth();
		int height = icon.getIconHeight();

		icon.setIconSize(width * 3, height * 3);
		setConnItemBounds(0, 0, width * 3, height * 3);
		repaint();
	}

	public void repaint() {
		clear();
		saveTransform();
		paintCanvasItems(this);
		restoreTransform();
	}

	public void setConnItemBounds(int x, int y, int width, int height) {
		if (connItems != null) {
			for (ConnectorItem connItem : connItems) {
				// set bounds
				boolean boundsSet = false;
				Icon compCanvasItemIcon = icon;
				if (compCanvasItemIcon instanceof Icon.ModelicaImpl) {
					Icon.ModelicaImpl im = (Icon.ModelicaImpl) compCanvasItemIcon;

					im.setParentCoords(((Icon.ModelicaImpl) icon).getExtent());
					im.setIconSize(im.getIconWidth(), im.getIconHeight());
					int[] parentExtent = im.getExtent();
					int[] extent = connItem.connector.getExtent();
					int[] origin = connItem.connector.getOrigin();
					// origin[1] =-origin[1];
					if (extent != null && parentExtent != null) {
						Rectangle pr = new Rectangle(Math.min(parentExtent[2],
								parentExtent[0]), Math.min(parentExtent[3],
								parentExtent[1]), Math.abs(parentExtent[2]
								- parentExtent[0]), Math.abs(parentExtent[3]
								- parentExtent[1]));
						Rectangle r = new Rectangle(Math.min(extent[2],
								extent[0])
								+ origin[0], -origin[1]
								+ Math.min(extent[3], extent[1]), Math
								.abs(extent[2] - extent[0]), Math.abs(extent[3]
								- extent[1]));

						Rectangle rr = null;

						{
							rr = new Rectangle((r.x - pr.x) * width / pr.width,
									(r.y - pr.y) * height / pr.height, r.width
											* width / pr.width, r.height
											* height / pr.height);
							connItem.setBounds(rr);
							// System.out.println("injaa1: " + rr);
							// sam 30 Oct, 2010
							rr = new Rectangle((r.x - pr.x) * width / pr.width,
									(r.y - pr.y) * height / pr.height, r.width
											* width / pr.width, r.height
											* height / pr.height);
							connItem.set_bounds(rr);
						}

						boundsSet = true;
					} else if (extent != null) {
						// 09 Oct 13 lt : deal with connectors that has no
						// parent
						// extent
						Rectangle r = new Rectangle(Math.min(extent[2],
								extent[0])
								+ origin[0], Math.min(extent[3], extent[1])
								+ origin[1], Math.abs(extent[2] - extent[0]),
								Math.abs(extent[3] - extent[1]));
						connItem.setBounds(r);
					}
				}
			}
		}
	}

	protected void paintCanvasItems(Graphics2D g2) {
		Dimension canvasSize = Constant.diagramCanvasSize;
		Dimension diagramSize = Constant.diagramSize;
		double transWidth = (canvasSize.width - diagramSize.width ) / 2;
		double transHeight = (canvasSize.height - diagramSize.height) / 2;
		g2.translate(transWidth, transHeight);
		g2.saveTransform();
		if (icon != null) {
			icon.paint(g2);
			icon.paintText(g2, null, new Font(Font.VERDANA, Font.PLAIN, 40));
		}
		g2.restoreTransform();

		double[] scalesize = null;
		if (icon != null)
			scalesize = icon.getconnScale();

		g2.saveTransform();
		for (ConnectorItem c : connItems) {
			if (scalesize != null)
				c.setScalesize(scalesize);
			Rectangle2D.Double _bounds = c.getBounds();
			g2.saveTransform();
			g2.translate(_bounds.x, _bounds.y);
			c.paint(g2);
			g2.translate(-_bounds.x, -_bounds.y);
			g2.restoreTransform();
		}
		g2.restoreTransform();
	}
}

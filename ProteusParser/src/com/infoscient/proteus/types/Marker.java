package com.infoscient.proteus.types;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Abstract base class for 'markers' used to mark plot points on a graph.
 * Subclasses are responsible for the actual drawing of the marker, given the
 * location and size to be used. A number of different types of markers are
 * derived from this abstract class
 * 
 * @author jasleen
 * 
 */
public abstract class Marker {
	/**
	 * Name of the marker, usually specifying the shape
	 */
	public final String name;

	/**
	 * Icon to be used in the UI
	 */
	public final Icon icon;

	/**
	 * Constructor, with arguments used for initialization
	 * 
	 * @param name
	 *            Name of the marker, usually specifying the shape
	 * @param icon
	 *            Icon to be used in the UI
	 */
	public Marker(String name, Icon icon) {
		this.name = name;
		this.icon = icon;
	}

	/**
	 * Two marker objects are considered equal if they have the same name
	 */
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Marker)) {
			return false;
		}
		return name.equals(((Marker) obj).name);
	}

	/**
	 * Interface method to draw the marker, to be implemented by subclasses
	 * 
	 * @param g
	 *            Graphics context used for drawing
	 * @param x
	 *            X-coordinate
	 * @param y
	 *            Y-coordinate
	 * @param w
	 *            Width
	 * @param h
	 *            Height
	 * @param drawColor
	 *            Color used to draw the outline of the marker
	 * @param fillColor
	 *            Color used to fill the space inside the marker
	 */
	public abstract void paint(Graphics g, int x, int y, int w, int h,
			Color drawColor, Color fillColor);

	/**
	 * Collection of various 'marker' shapes. The following types are available:
	 * <ul>
	 * <li>None</li>
	 * <li>Circle</li>
	 * <li>Square</li>
	 * <li>Cross</li>
	 * <li>Dot</li>
	 * <li>Plus</li>
	 * <li>Sparkle</li>
	 * <li>Diamond</li>
	 * <li>North Triangle</li>
	 * <li>South Triangle</li>
	 * <li>West Triangle</li>
	 * <li>East Triangle</li>
	 * </ul>
	 */
	public static final Marker[] MARKERS = {
			new Marker("None", getIcon("resources/none.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
				}
			},
			new Marker("Circle", getIcon("resources/circle.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillOval(x, y, w, h);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawOval(x, y, w, h);
					}
				}
			},
			new Marker("Square", getIcon("resources/square.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillRect(x, y, w, h);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawRect(x, y, w, h);
					}
				}
			},
			new Marker("Cross", getIcon("resources/cross.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawLine(x, y, x + w, y + h);
						g.drawLine(x, y + h, x + w, y);
					}
				}
			},
			new Marker("Dot", getIcon("resources/dot.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					if (drawColor != null) {
						g.setColor(drawColor);
						g.fillOval(x + w / 8, y + h / 8, 3 * w / 4, 3 * h / 4);
					}
				}
			},
			new Marker("Plus", getIcon("resources/plus.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawLine(x, y + h / 2, x + w, y + h / 2);
						g.drawLine(x + w / 2, y, x + w / 2, y + h);
					}
				}
			},
			new Marker("Sparkle", getIcon("resources/sparkle.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawLine(x, y, x + w, y + h);
						g.drawLine(x, y + h, x + w, y);
						g.drawLine(x, y + h / 2, x + w, y + h / 2);
						g.drawLine(x + w / 2, y, x + w / 2, y + h);
					}
				}
			},
			new Marker("Diamond", getIcon("resources/diamond.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					Polygon polygon = new Polygon();
					polygon.addPoint(x + w / 2, y);
					polygon.addPoint(x + w, y + h / 2);
					polygon.addPoint(x + w / 2, y + h);
					polygon.addPoint(x, y + h / 2);
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillPolygon(polygon);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawPolygon(polygon);
					}
				}
			},
			new Marker("North Triangle", getIcon("resources/northTriangle.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					Polygon polygon = new Polygon();
					polygon.addPoint(x + w / 2, y);
					polygon.addPoint(x + w, y + h);
					polygon.addPoint(x, y + h);
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillPolygon(polygon);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawPolygon(polygon);
					}
				}
			},
			new Marker("South Triangle", getIcon("resources/southTriangle.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					Polygon polygon = new Polygon();
					polygon.addPoint(x + w / 2, y + h);
					polygon.addPoint(x + w, y);
					polygon.addPoint(x, y);
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillPolygon(polygon);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawPolygon(polygon);
					}
				}
			},
			new Marker("West Triangle", getIcon("resources/westTriangle.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					Polygon polygon = new Polygon();
					polygon.addPoint(x, y + h / 2);
					polygon.addPoint(x + w, y);
					polygon.addPoint(x + w, y + h);
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillPolygon(polygon);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawPolygon(polygon);
					}
				}
			},
			new Marker("East Triangle", getIcon("resources/eastTriangle.png")) {
				public void paint(Graphics g, int x, int y, int w, int h,
						Color drawColor, Color fillColor) {
					x -= w / 2;
					y -= h / 2;
					Polygon polygon = new Polygon();
					polygon.addPoint(x, y);
					polygon.addPoint(x + w, y + h / 2);
					polygon.addPoint(x, y + h);
					if (fillColor != null) {
						g.setColor(fillColor);
						g.fillPolygon(polygon);
					}
					if (drawColor != null) {
						g.setColor(drawColor);
						g.drawPolygon(polygon);
					}
				}
			} };

	// set this to true if generating marker images
	// used in GenMarkerImages class
	public static final boolean genMode = false;

	private static Icon getIcon(String name) {
		return genMode ? new ImageIcon() : new ImageIcon(Marker.class
				.getResource(name));
	}
}
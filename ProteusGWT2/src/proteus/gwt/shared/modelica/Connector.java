package proteus.gwt.shared.modelica;

import proteus.gwt.client.app.ui.canvas.Icon;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.modelica.Placement.Transformation;

public class Connector {

	public String[] connectorString;
	protected Icon icon, diagram;

	public String name;
	public final String prefix;

	protected Transformation transformation;

	public Connector(String name, String prefix, Transformation tran, String str) {
		this(name, prefix, tran, new String[] { str });
	}

	public Connector(String name, String prefix, Transformation tran,
			String[] str) {
		this.name = name;
		this.prefix = prefix;
		this.connectorString = str;
		this.transformation = tran;
	}

	public int[] getExtent() {
		if (transformation != null && transformation.extent != null) {
			return transformation.extent;
		} else if (icon != null && icon instanceof Icon.ModelicaImpl) {
			return ((Icon.ModelicaImpl) icon).getExtent();
		} else {
			return null;
		}
	}

	public Icon getIcon() {
		if (icon != null)
			return icon;
		else
			return null;
	}

	public Icon getIcon(Graphics2D g2) {
		return icon = new Icon.ModelicaImpl(g2, connectorString);
	}

	public int[] getOrigin() {
		if (transformation != null && transformation.origin != null) {
			return transformation.origin;
		} else {
			int[] ori = { 0, 0 };
			return ori;
		}
	}

	public int getRotation() {
		if (transformation != null) {
			return transformation.rotation;
		} else {
			return -1;
		}
	}

	/**
	 * @return the transformation
	 */
	public Transformation getTransformation() {
		return transformation;
	}

	/**
	 * @param transformation
	 *            the transformation to set
	 */
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
	}
}
package proteus.gwt.client.app.ui.util;

public interface GraphicConstants {

	public static enum LinePattern {
		NONE, SOLID, DASH, DOT, DASHDOT, DASHDOTDOT
	};

	public static enum FillPattern {
		NONE, SOLID, HORIZONTAL, VERTICAL, CROSS, HORIZONTALCYLINDER, SPHERE, VERTICALCYLINDER, BACKWARD
	}

	public static enum BorderPattern {
		NONE, RAISED, SUNKEN, ENGRAVED
	}
	
	

	public static enum Smooth {
		NONE, BEZIER
	}

	public static enum Arrow {
		NONE, OPEN, FILLED, HALF
	}

	public static enum TextStyle {
		BOLD, ITALIC, UNDERLINE
	}

	public static enum TextAlignment {
		LEFT, CENTER, RIGHT
	}
}

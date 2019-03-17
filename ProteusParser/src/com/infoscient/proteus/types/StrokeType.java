package com.infoscient.proteus.types;

import java.awt.BasicStroke;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify attributes for a <code>Stroke</code> variable
 * 
 * @author jasleen
 * 
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface StrokeType {
	/**
	 * Collection of built-in strokes. The following types are available:
	 * <ol>
	 * <li>Solid</li>
	 * <li>Dash</li>
	 * <li>Long Dash</li>
	 * <li>Dash Dot</li>
	 * <li>Long Dash Dot</li>
	 * </ol>
	 */

	/**
	 * Name of variable. Displayed as a label in the UI
	 * 
	 * @return Name as a string
	 */
	public String name();

	/**
	 * Category to which the variable belongs to. Variables belonging to the
	 * same category are shown grouped in the UI
	 * 
	 * @return The name of the category as a string
	 */
	public String category() default "";

	/**
	 * Short description of the variable, appears as a tool-tip of the name
	 * label. The tool-tip is shown when the mouse is positioned over the label
	 * 
	 * @return A string describing what the variable is used for
	 */
	public String description() default "";

	/**
	 * Whether this variable is constant or not. The corresponding UI element
	 * will be disabled if the variable is constant
	 * 
	 * @return true if the value shouldn't be changed, false otherwise
	 */
	public boolean readonly() default false;

	public static BasicStroke[] STROKES = {
			new BasicStroke(1), // Solid
			new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL,
					0.0f, new float[] { 10.0f }, 0.0f), // Dash
			new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL,
					0.0f, new float[] { 20.0f, 10.0f }, 0.0f), // Long Dash
			new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL,
					0.0f, new float[] { 10.0f, 10.0f, 5.0f, 10.0f }, 0.0f), // Dash
			// Dot
			new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL,
					0.0f, new float[] { 20.0f, 10.0f, 10.0f, 10.0f }, 0.0f) // Long
	// Dash
	// Dot
	};
}

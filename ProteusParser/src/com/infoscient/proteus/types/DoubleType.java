package com.infoscient.proteus.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify attributes for a double variable
 * 
 * @author jasleen
 * 
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DoubleType {
	/**
	 * Name of variable. Displayed as a label in the UI
	 * 
	 * @return
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

	/**
	 * Minimum value that the variable can have
	 * 
	 * @return a double value, with default as 0.0
	 */
	public double min() default 0.0;

	/**
	 * Minimum value that the variable can have
	 * 
	 * @return a double value, with default as 1.0
	 */
	public double max() default 1.0;

	/**
	 * Step by which this variable should be increased or decreased
	 * 
	 * @return a double value, with default as 0.1
	 */
	public double step() default 0.1;
}

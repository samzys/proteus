package com.infoscient.proteus.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify attributes for a integer variable
 * 
 * @author jasleen
 * 
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface IntegerType {
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

	/**
	 * Minimum value that the variable can have
	 * 
	 * @return an integer value, with default as Integer.MIN_VALUE
	 */
	public int min() default java.lang.Integer.MIN_VALUE;

	/**
	 * Maximum value that the variable can have
	 * 
	 * @return an integer value, with default as Integer.MAX_VALUE
	 */
	public int max() default java.lang.Integer.MAX_VALUE;

	/**
	 * Step by which this variable should be increased or decreased
	 * 
	 * @return an integer value, with default as 1
	 */
	public int step() default 1;
}
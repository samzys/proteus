package com.infoscient.proteus.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify attributes for a <code>File</code> variable, storing
 * a file type (not a directory)
 * 
 * @author jasleen
 * 
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FileType {
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
	 * Path to the default file to be used when none is selected
	 * 
	 * @return A valid path on the current filesystem
	 */
	public String default_();

	/**
	 * String array containing extensions of valid files. Extension names should
	 * consist of just the alphanumeric characters, or have '*.' as a prefix
	 * 
	 * @return Valid file extensions, as an array of <code>String</code>s
	 */
	public String[] filters();
}

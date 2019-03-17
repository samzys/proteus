package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating a code string. The underlying variable must be of
 * type <code>String</code>
 * 
 * @author jasleen
 * 
 */
public class CodeProperty extends Property {

	/**
	 * Language in which code has been written. Used to determine which syntax
	 * highlighter to use
	 */
	public final String language;

	/**
	 * @see Property#Property(String, String, String, boolean)
	 * @param language
	 *            Language in which code has been written
	 */
	public CodeProperty(String name, String category, String description,
			boolean readonly, String language) {
		super(name, category, description, readonly);
		this.language = language;
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 * @param language
	 *            Language in which code has been written
	 */
	public CodeProperty(PropertyDescriptor pd, Object obj, String category,
			boolean readonly, String language) {
		super(pd, obj, category, readonly);
		this.language = language;
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 * @param language
	 *            Language in which code has been written
	 */
	public CodeProperty(Field field, Object obj, String name, String category,
			String description, boolean readonly, String language) {
		super(field, obj, name, category, description, readonly);
		this.language = language;
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 * @param field
	 *            Field object that this property object is bound to
	 * @param obj
	 *            Parent object containing the property
	 * @param type
	 *            Annotation object storing property attributes
	 */
	public CodeProperty(Field field, Object obj, CodeType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly(), type.language());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 * @param language
	 *            Language in which code has been written
	 */
	public CodeProperty(CodeProperty prop, String name, String category,
			String description, boolean readonly, String language) {
		super(prop, name, category, description, readonly);
		this.language = language;
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

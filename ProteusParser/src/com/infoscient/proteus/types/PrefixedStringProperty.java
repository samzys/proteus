package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating a string, with a fixed prefix
 * 
 * @author jasleen
 * 
 */
public class PrefixedStringProperty extends StringProperty {

	/**
	 * Fixed prefix to be attached to the string value
	 */
	public final String prefix;

	/**
	 * @see Property#Property(String, String, String, boolean)
	 */
	public PrefixedStringProperty(String name, String prefix, String category,
			String description, boolean readonly) {
		super(name, category, description, readonly);
		this.prefix = prefix;
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 */
	public PrefixedStringProperty(PropertyDescriptor pd, String prefix,
			Object obj, String category, boolean readonly) {
		super(pd, obj, category, readonly);
		this.prefix = prefix;
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 */
	public PrefixedStringProperty(Field field, Object obj, String name,
			String prefix, String category, String description, boolean readonly) {
		super(field, obj, name, category, description, readonly);
		this.prefix = prefix;
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
	public PrefixedStringProperty(Field field, Object obj, StringType type,
			String prefix) {
		this(field, obj, type.name(), prefix, type.category(), type
				.description(), type.readonly());
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
	public PrefixedStringProperty(Field field, Object obj,
			PrefixedStringType type) {
		this(field, obj, type.name(), type.prefix(), type.category(), type
				.description(), type.readonly());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public PrefixedStringProperty(StringProperty prop, String name,
			String prefix, String category, String description, boolean readonly) {
		super(prop, name, category, description, readonly);
		this.prefix = prefix;
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public PrefixedStringProperty(PrefixedStringProperty prop, String name,
			String prefix, String category, String description, boolean readonly) {
		super(prop, name, category, description, readonly);
		this.prefix = prefix;
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

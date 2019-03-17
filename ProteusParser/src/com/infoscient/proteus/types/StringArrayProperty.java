package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating a string array
 * 
 * @author jasleen
 * 
 */
public class StringArrayProperty extends Property {

	/**
	 * @see Property#Property(String, String, String, boolean)
	 */
	public StringArrayProperty(String name, String category,
			String description, boolean readonly) {
		super(name, category, description, readonly);
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 */
	public StringArrayProperty(PropertyDescriptor pd, Object obj,
			String category, boolean readonly) {
		super(pd, obj, category, readonly);
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 */
	public StringArrayProperty(Field field, Object obj, String name,
			String description, String category, boolean readonly) {
		super(field, obj, name, category, description, readonly);
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
	public StringArrayProperty(Field field, Object obj, StringArrayType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public StringArrayProperty(StringArrayProperty prop, String name,
			String category, String description, boolean readonly) {
		super(prop, name, category, description, readonly);
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

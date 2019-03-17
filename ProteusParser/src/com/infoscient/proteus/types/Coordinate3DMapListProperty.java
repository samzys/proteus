package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating a Coordinate3DMapList object
 * 
 * @author jasleen
 * 
 */
public class Coordinate3DMapListProperty extends Property {

	/**
	 * @see Property#Property(String, String, String, boolean)
	 */
	public Coordinate3DMapListProperty(String name, String category,
			String description, boolean readonly) {
		super(name, category, description, readonly);
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 */
	public Coordinate3DMapListProperty(PropertyDescriptor pd, Object obj,
			String category, boolean readonly) {
		super(pd, obj, category, readonly);
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 */
	public Coordinate3DMapListProperty(Field field, Object obj, String name,
			String category, String description, boolean readonly) {
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
	public Coordinate3DMapListProperty(Field field, Object obj,
			Coordinate3DMapListType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public Coordinate3DMapListProperty(Coordinate3DMapListProperty prop,
			String name, String category, String description, boolean readonly) {
		super(prop, name, category, description, readonly);
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating an enumeration of String values. The underlying
 * variable must be of type <code>String</code> and should be initialized to
 * one of the allowed values
 * 
 * @author jasleen
 * 
 */
public class EnumProperty extends Property {

	/**
	 * Array of valid <code>String</code>s
	 */
	public final String[] allowed;

	/**
	 * Can be one of:
	 * 1. "c": ComboBox
	 * 2. "r": RadioButtons
	 */
	public final String type;
	
	/**
	 * @see Property#Property(String, String, String, boolean)
	 * @param allowed
	 *            Array of valid <code>String</code>s
	 */
	public EnumProperty(String name, String category, String description,
			boolean readonly, String[] allowed, String type) {
		super(name, category, description, readonly);
		this.allowed = allowed;
		this.type = type;
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 * @param allowed
	 *            Array of valid <code>String</code>s
	 */
	public EnumProperty(PropertyDescriptor pd, Object obj, String category,
			boolean readonly, String[] allowed, String type) {
		super(pd, obj, category, readonly);
		this.allowed = allowed;
		this.type = type;
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 * @param allowed
	 *            Array of valid <code>String</code>s
	 */
	public EnumProperty(Field field, Object obj, String name, String category,
			String description, boolean readonly, String[] allowed, String type) {
		super(field, obj, name, category, description, readonly);
		this.allowed = allowed;
		this.type = type;
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
	public EnumProperty(Field field, Object obj, EnumType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly(), type.allowed(), type.type());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public EnumProperty(EnumProperty prop, String name, String category,
			String description, boolean readonly) {
		super(prop, name, category, description, readonly);
		this.allowed = prop.allowed;
		this.type = prop.type;
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

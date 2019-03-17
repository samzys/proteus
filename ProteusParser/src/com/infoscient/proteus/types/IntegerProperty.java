package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating an integer
 * 
 * @author jasleen
 * 
 */
public class IntegerProperty extends Property {

	/**
	 * Minimum value that this property can have
	 */
	public final int min;

	/**
	 * Maximum value that this property can have
	 */
	public final int max;

	/**
	 * Step by which this property should be increased or decreased
	 */
	public final int step;

	/**
	 * @see Property#Property(String, String, String, boolean)
	 * @param min
	 *            Minimum value that this property can have
	 * @param max
	 *            Maximum value that this property can have
	 * @param step
	 *            Step by which this property should be increased or decreased
	 */
	public IntegerProperty(String name, String category, String description,
			boolean readonly, int min, int max, int step) {
		super(name, category, description, readonly);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 * @param min
	 *            Minimum value that this property can have
	 * @param max
	 *            Maximum value that this property can have
	 * @param step
	 *            Step by which this property should be increased or decreased
	 */
	public IntegerProperty(PropertyDescriptor pd, Object obj, String category,
			boolean readonly, int min, int max, int step) {
		super(pd, obj, category, readonly);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 * @param min
	 *            Minimum value that this property can have
	 * @param max
	 *            Maximum value that this property can have
	 * @param step
	 *            Step by which this property should be increased or decreased
	 */
	public IntegerProperty(Field field, Object obj, String name,
			String category, String description, boolean readonly, int min,
			int max, int step) {
		super(field, obj, name, category, description, readonly);
		this.min = min;
		this.max = max;
		this.step = step;
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
	public IntegerProperty(Field field, Object obj, IntegerType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly(), type.min(), type.max(), type.step());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public IntegerProperty(IntegerProperty prop, String name, String category,
			String description, boolean readonly) {
		super(prop, name, category, description, readonly);
		this.min = prop.min;
		this.max = prop.max;
		this.step = prop.step;
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

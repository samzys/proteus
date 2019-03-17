package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating a double
 * 
 * @author jasleen
 * 
 */
public class DoubleProperty extends Property {

	/**
	 * Minimum value that this property can have
	 */
	public final double min;

	/**
	 * Maximum value that this property can have
	 */
	public final double max;

	/**
	 * Step by which this property should be increased or decreased
	 */
	public final double step;
	
	public String unit;

	/**
	 * @see Property#Property(String, String, String, boolean)
	 * @param min
	 *            Minimum value that this property can have
	 * @param max
	 *            Maximum value that this property can have
	 * @param step
	 *            Step by which this property should be increased or decreased
	 */
	public DoubleProperty(String name, String category, String description,
			boolean readonly, double min, double max, double step) {
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
	public DoubleProperty(PropertyDescriptor pd, Object obj, String category,
			boolean readonly, double min, double max, double step) {
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
	public DoubleProperty(Field field, Object obj, String name,
			String category, String description, boolean readonly, double min,
			double max, double step) {
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
	public DoubleProperty(Field field, Object obj, DoubleType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly(), type.min(), type.max(), type.step());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public DoubleProperty(DoubleProperty prop, String name, String description,
			String category, boolean readonly) {
		super(prop, name, category, description, readonly);
		this.min = prop.min;
		this.max = prop.max;
		this.step = prop.step;
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

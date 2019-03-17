package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * Property type encapsulating a Modelica parameter, stored as a
 * <code>String</code>. This type is used to modify the state of a parameter
 * in Modelica code by the user
 * 
 * @author jasleen
 * 
 */
public class ParamProperty extends StringProperty {

	/**
	 * Default value of this property
	 */
	public final Object default_;

	/**
	 * Whether this property is a modifier (and belongs to <i>Modifier</i>
	 * category)
	 */
	public final boolean modifier;

	/**
	 * Minimum value this property can have
	 */
	public String min;

	/**
	 * Maximum value this property can have
	 */
	public String max;

	/**
	 * Start value this property should have
	 */
	public String start;

	/**
	 * Whether the parameter is fixed (constant) or not
	 */
	public boolean fixed;

	public String unit;
	/**
	 * Should be used as state or not. Can have one of the following values:
	 * <ul>
	 * <li>Never - Do not use as state at all</li>
	 * <li>Avoid - Use as state only if it cannot be avoided</li>
	 * <li>Default - Use as state whenever appropriate</li>
	 * <li>Prefer - Give priority as state over default one</li>
	 * <li>Always - Use as state</li>
	 * </ul>
	 */
	public String stateSelect;

	/**
	 * @see Property#Property(String, String, String, boolean)
	 * @param default_
	 *            Default value of this property
	 * @param modifier
	 *            Whether this property is a modifier
	 */
	public ParamProperty(String name, String category, String description,
			boolean readonly, Object default_, boolean modifier) {
		super(name, category, description, readonly);
		this.default_ = default_;
		this.modifier = modifier;
	}

	public ParamProperty(String name, String category, String unit, String description,
			boolean readonly, Object default_, boolean modifier) {
		super(name, category, description, readonly);
		this.unit = unit;
		this.default_ = default_;
		this.modifier = modifier;
	}
	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 */
	public ParamProperty(PropertyDescriptor pd, Object obj, String category,
			boolean readonly, Object default_, boolean modifier) {
		super(pd, obj, category, readonly);
		this.default_ = default_;
		this.modifier = modifier;
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 */
	public ParamProperty(Field field, Object obj, String name, String category,
			String description, boolean readonly, Object default_,
			boolean modifier) {
		super(field, obj, name, category, description, readonly);
		this.default_ = default_;
		this.modifier = modifier;
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
	public ParamProperty(Field field, Object obj, StringType type,
			Object default_, boolean modifier) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly(), default_, modifier);
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public ParamProperty(ParamProperty prop, String name, String category,
			String description, boolean readonly, Object default_,
			boolean modifier) {
		super(prop, name, category, description, readonly);
		this.default_ = default_;
		this.modifier = modifier;
	}

	/**
	 * Sets extra modifier values
	 * 
	 * @param min
	 *            Minimum value this property can have
	 * @param max
	 *            Maximum value this property can have
	 * @param start
	 *            Start value this property should have
	 * @param fixed
	 *            Whether the parameter is fixed (constant) or not
	 * @param stateSelect
	 *            Should be used as state or not. Can have one of the following
	 *            values:
	 *            <ul>
	 *            <li>Never - Do not use as state at all</li>
	 *            <li>Avoid - Use as state only if it cannot be avoided</li>
	 *            <li>Default - Use as state whenever appropriate</li>
	 *            <li>Prefer - Give priority as state over default one</li>
	 *            <li>Always - Use as state</li>
	 *            </ul>
	 * 
	 */
	public void setEx(String min, String max, String start, boolean fixed,
			String stateSelect) {
		this.min = min;
		this.max = max;
		this.start = start;
		this.fixed = fixed;
		this.stateSelect = stateSelect;
		firePropertyChanged();
	}

	/**
	 * Has the parameter been modified or not. A modified parameter is one whose
	 * at-least one modifier value is different from default
	 * 
	 * @return true if the parameter has been modified, false otherwise
	 */
	public boolean isModified() {
		return (min != null && min.length() > 0)
				|| (max != null && max.length() > 0)
				|| (start != null && start.length() > 0) || (fixed != false)
				|| (stateSelect != null && stateSelect.length() > 0);
	}

	/**
	 * Returns list of modifier values (only those different from default) as a
	 * Modelica code string.
	 * 
	 * @return Modelica code string snippet of modifiers whose value differs
	 *         from default
	 */
	public String getModificationString() {
		StringBuilder sb = new StringBuilder();
		String sep = ", ";
		int count = 0; // Used to determine if comma should be appended at the
						// beginning or not
		if (min != null && min.length() > 0) {
			sb.append("min = " + min);
			count++;
		}
		if (max != null && max.length() > 0) {
			if (count++ > 0) {
				sb.append(sep);
			}
			sb.append("max = " + max);
		}
		if (start != null && start.length() > 0) {
			if (count++ > 0) {
				sb.append(sep);
			}
			sb.append("start = " + start);
		}
		if (fixed != false) {
			if (count++ > 0) {
				sb.append(sep);
			}
			sb.append("fixed = " + fixed);
		}
		if (stateSelect != null && stateSelect.length() > 0) {
			if (count++ > 0) {
				sb.append(sep);
			}
			sb.append("stateSelect = " + stateSelect);
		}
		return sb.toString();
	}
}

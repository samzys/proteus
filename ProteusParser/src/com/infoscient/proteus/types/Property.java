package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class for property types. A property is a public variable which is
 * automatically exposed as a UI element by reflectively ascertaining its
 * attributes. The attributes of a property are specified through annotations.
 * Every property has its corresponding UI element which could be displayed in a
 * form and modified by the user.
 * 
 * @author jasleen
 * 
 */
public abstract class Property implements Serializable {
	private static final int MODE_FIELD = 0, MODE_WRAP = 1, MODE_CUSTOM = 2;

	/**
	 * Name of the property
	 */
	public final String name;

	/**
	 * Category to which this property belongs to
	 */
	public final String category;

	/**
	 * Short description of this property
	 */
	public final String description;

	/**
	 * Whether this property is a constant or not
	 */
	public boolean readonly;

	/**
	 * Whether this property is hidden or not. A hidden property won't show up
	 * in the automatically-generated form
	 */
	public boolean hidden;

	/**
	 * List of listeners interested in being informed when the value changes
	 */
	private List<PropertyListener> listeners = new LinkedList<PropertyListener>();

	/**
	 * Field object that this property object is bound to. The field must be
	 * public or have public getters and setters to allow modifying its value
	 * using reflection
	 */
	private Field field;

	/**
	 * Getters and setters for the Field object that this property object is
	 * bound to
	 */
	private Method getter, setter;

	/**
	 * Parent object containing the property
	 */
	private Object obj;

	/**
	 * Property object around which this object is wrapped. This object then
	 * passes all change events to the underlying <code>wrappedProp</code>
	 */
	private Property wrappedProp;

	/**
	 * Integer id for type of Property (Custom, Field or Wrapped)
	 */
	private final int mode;

	/**
	 * Constructor for custom Property object. The <code>custom_get()</code>
	 * and <code>custom_set()</code> methods must be over-ridden to manually
	 * get and set the underlying variable
	 * 
	 * @param name
	 *            Name of the property
	 * @param category
	 *            Category to which this property belongs to
	 * @param description
	 *            Short description of this property
	 * @param readonly
	 *            Whether this property is a constant or not
	 */
	public Property(String name, String category, String description,
			boolean readonly) {
		mode = MODE_CUSTOM;
		this.name = name;
		this.category = category;
		this.description = description;
		this.readonly = readonly;
	}

	/**
	 * Constructor for field Property object, using a PropertyDescriptor object
	 * to obtain property information
	 * 
	 * @param pd
	 *            PropertyDescriptor object
	 * @param obj
	 *            Parent object containing the property
	 * @param category
	 *            Category to which this property belongs to
	 * @param readonly
	 *            Whether this property is a constant or not
	 */
	public Property(PropertyDescriptor pd, Object obj, String category,
			boolean readonly) {		
		mode = MODE_FIELD;
		getter = pd.getReadMethod();
		setter = pd.getWriteMethod();
		this.obj = obj;
		this.name = pd.getDisplayName();
		this.category = category;
		this.description = pd.getShortDescription();
		this.readonly = readonly;
	}

	/**
	 * Constructor for field Property object. If a getter and setter (conforming
	 * to the JavaBeans specification) is available, those methods are used to
	 * get and set the field respectively. Otherwise, if the field is public,
	 * <i>reflection</i> is used to get and set the value. A runtime exception
	 * is thrown if the field is private with no getter and setter available
	 * 
	 * @param field
	 *            Field object that this property object is bound to
	 * @param obj
	 *            Parent object containing the property
	 * @param name
	 *            Name of the property
	 * @param category
	 *            Category to which this property belongs to
	 * @param description
	 *            Short description of this property
	 * @param readonly
	 *            Whether this property is a constant or not
	 */
	public Property(Field field, Object obj, String name, String category,
			String description, boolean readonly) {
		mode = MODE_FIELD;
		this.field = field;
		String fieldName = field.getName();
		try {
			getter = obj.getClass().getMethod(getterFor(fieldName));
		} catch (Exception e) {
			getter = null;
		}
		try {
			setter = obj.getClass().getMethod(setterFor(fieldName),
					field.getType());
		} catch (Exception e) {
			setter = null;
		}
		this.obj = obj;
		this.name = name;
		this.category = category;
		this.description = description;
		this.readonly = readonly;
	}

	/**
	 * Constructor for wrapped Property object. All change events are passed on
	 * to the underlying Property object
	 * 
	 * @param prop
	 *            Property object around which this object is wrapped
	 * @param name
	 *            Name of the property
	 * @param category
	 *            Category to which this property belongs to
	 * @param description
	 *            Short description of this property
	 * @param readonly
	 *            Whether this property is a constant or not
	 */
	public Property(Property prop, String name, String category,
			String description, boolean readonly) {
		mode = MODE_WRAP;
		this.wrappedProp = prop;
		for (PropertyListener pl : prop.listeners) {
			addListener(pl);
		}
		this.name = name;
		this.category = category;
		this.description = description;
		this.readonly = readonly;
	}

	/**
	 * Add a listener interested in being informed when this property changes
	 * 
	 * @param l
	 *            PropertyListener object interested in being informed when this
	 *            property changes
	 */
	public void addListener(PropertyListener l) {		
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Remove an already registered listener
	 * 
	 * @param l
	 *            PropertyListener object which has already registered its
	 *            interest
	 */
	public void removeListener(PropertyListener l) {
		listeners.remove(l);
	}

	/**
	 * Inform all listeners that this property has changed
	 */
	public void firePropertyChanged() {		
		for (PropertyListener l : listeners) {
			l.valueChanged(this);
		}
	}

	/**
	 * Fetch the value of this property. Depending on the type of property, the
	 * way in which the value is retrieved would differ
	 * 
	 * @return Value of this property
	 */
	public final Object get() {
		switch (mode) {
		case MODE_FIELD:
			try {
				Object value;
				if (getter != null) {
					value = getter.invoke(obj);
				} else {
					value = field.get(obj);
				}
				return value;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case MODE_WRAP:
			return wrappedProp.get();
		case MODE_CUSTOM:
			return custom_get();
		}
		return null;
	}

	/**
	 * Store a new value in this property. Depending on the type of property,
	 * the way in which the value is stored would differ
	 * 
	 * @param value
	 *            New value of this property
	 */
	public final void set(Object value) {
		switch (mode) {
		case MODE_FIELD:
			try {
				Object oldValue = get();
				if ((oldValue != value)
						&& (oldValue == null || !oldValue.equals(value))) {
					if (setter != null) {
						setter.invoke(obj, new Object[] { value });
					} else {
						field.set(obj, value);
					}
					firePropertyChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case MODE_WRAP:
			wrappedProp.set(value);
			firePropertyChanged();
			break;
		case MODE_CUSTOM:
			custom_set(value);
			firePropertyChanged();
			break;
		}
	}

	/**
	 * Uses JavaBeans convention to create the name of the getter (e.g. the
	 * getter for a field called <i>name</i> is <i>getName</i>)
	 * 
	 * @param fieldName
	 *            Name of the field whose getter
	 * @return Expected name of the getter according to JavaBeans convention
	 */
	public static String getterFor(String fieldName) {
		StringBuilder sb = new StringBuilder("get");
		sb.append(Character.toUpperCase(fieldName.charAt(0)));
		if (fieldName.length() > 1) {
			sb.append(fieldName.substring(1));
		}
		return sb.toString();
	}

	/**
	 * Uses JavaBeans convention to create the name of the setter (e.g. the
	 * setter for a field called <i>name</i> is <i>setName</i>)
	 * 
	 * @param fieldName
	 *            Name of the field whose getter
	 * @return Expected name of the setter according to JavaBeans convention
	 */
	public static String setterFor(String fieldName) {
		StringBuilder sb = new StringBuilder("set");
		sb.append(Character.toUpperCase(fieldName.charAt(0)));
		if (fieldName.length() > 1) {
			sb.append(fieldName.substring(1));
		}
		return sb.toString();
	}

	/**
	 * Placeholder getter for custom Property type, to be overridden by
	 * subclasses
	 * 
	 * @return Value of this property
	 */
	public Object custom_get() {
		throw new RuntimeException("Custom get method not over-ridden");
	}

	/**
	 * Placeholder setter for custom Property type, to be overridden by
	 * subclasses
	 * 
	 * @param value
	 *            New value of this property
	 */
	public void custom_set(Object value) {
		throw new RuntimeException("Custom set method not over-ridden");
	}

	/**
	 * Method to generate code using the <i>Visitor</i> design pattern
	 * 
	 * @param codeGen
	 *            PropertyCodeGenerator object responsible for generating code
	 *            depending on Property type
	 * @return Generated code snippet as a String
	 */
	public abstract String toCode(PropertyCodeGenerator codeGen);
}

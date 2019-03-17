package com.infoscient.proteus.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Property type encapsulating a Date object
 * 
 * @author jasleen
 * 
 */
public class DateProperty extends Property {

	/**
	 * @see Property#Property(String, String, String, boolean)
	 */
	public DateProperty(String name, String category, String description,
			boolean readonly) {
		super(name, category, description, readonly);
	}

	/**
	 * @see Property#Property(PropertyDescriptor, Object, String, boolean)
	 */
	public DateProperty(PropertyDescriptor pd, Object obj, String category,
			boolean readonly) {
		super(pd, obj, category, readonly);
	}

	/**
	 * @see Property#Property(Field, Object, String, String, String, boolean)
	 */
	public DateProperty(Field field, Object obj, String name, String category,
			String description, boolean readonly) {
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
	public DateProperty(Field field, Object obj, DateType type) {
		this(field, obj, type.name(), type.category(), type.description(), type
				.readonly());
	}

	/**
	 * @see Property#Property(Property, String, String, String, boolean)
	 */
	public DateProperty(DateProperty prop, String name, String category,
			String description, boolean readonly) {
		super(prop, name, category, description, readonly);
	}

	/**
	 * Converts a <code>String</code> representation of a date (as
	 * <i>dd-MM-yyyy</i>) into a <code>Date</code> object
	 * 
	 * @param str
	 *            <code>String</code> representation of a date (as
	 *            <i>dd-MM-yyyy</i>)
	 * @return a <code>Date</code> object constructed from the
	 *         <code>String</code> argument
	 */
	public static Date toDate(String str) {
		if (str == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date parsedDate = null;
		try {
			parsedDate = format.parse(str);
		} catch (ParseException e) {
			System.err.println("Error: Can't parse date: " + str);
		}
		return parsedDate;
	}

	/**
	 * Converts a <code>Date</code> object into its <code>String</code>
	 * representation
	 * 
	 * @param date
	 *            <code>Date</code> object to be converted
	 * @return a <code>String</code> representation of the date (as
	 *         <i>dd-MM-yyyy</i>) constructed from the <code>Date</code>
	 *         argument
	 */
	public static String fromDate(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(date);
	}

	public String toCode(PropertyCodeGenerator codeGen) {
		return codeGen.toCode(this);
	}
}

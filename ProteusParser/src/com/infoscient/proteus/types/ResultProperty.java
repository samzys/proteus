package com.infoscient.proteus.types;

import java.lang.reflect.Field;

public class ResultProperty extends BooleanProperty {

	public String unit;
	
	public ResultProperty(Field field, Object obj, BooleanType type) {
		super(field, obj, type);
	}
	public ResultProperty(String name, String category,  String unit, String description,
			boolean readonly) {
		super(name, category, description, readonly);
		this.unit = unit;
	}
}

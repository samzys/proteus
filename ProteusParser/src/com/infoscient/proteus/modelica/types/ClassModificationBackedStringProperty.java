package com.infoscient.proteus.modelica.types;

import java.io.StringReader;

import com.infoscient.proteus.modelica.parser.ModelicaParser;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.parser.ParseException;
import com.infoscient.proteus.types.StringProperty;

public class ClassModificationBackedStringProperty extends StringProperty {

	private OMModification m;

	public ClassModificationBackedStringProperty(OMModification m, String name,
			String category, String description, boolean readonly) {
		super(name, category, description, readonly);
		this.m = m;
	}

	public Object custom_get() {
		return m.toCode();
	}

	public void custom_set(Object value) {
		String s = (String) value;
		ModelicaParser parser = new ModelicaParser(new StringReader(s));
		try {
			m.classModification = parser.modification().classModification;
		} catch (ParseException e) {
			m.classModification = null;
			e.printStackTrace();
		}
	}
}
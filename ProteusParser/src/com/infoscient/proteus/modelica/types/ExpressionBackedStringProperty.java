package com.infoscient.proteus.modelica.types;

import java.io.StringReader;

import com.infoscient.proteus.modelica.parser.ModelicaParser;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.parser.ParseException;
import com.infoscient.proteus.types.StringProperty;

public class ExpressionBackedStringProperty extends StringProperty {

	private OMModification m;

	public ExpressionBackedStringProperty(OMModification m, String name,
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
			m.expression = parser.modification().expression;
		} catch (ParseException e) {
			m.expression = null;
			e.printStackTrace();
		}
	}
}
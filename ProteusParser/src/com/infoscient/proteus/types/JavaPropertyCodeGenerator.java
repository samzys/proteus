package com.infoscient.proteus.types;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

public class JavaPropertyCodeGenerator implements PropertyCodeGenerator {

	public String toCode(Property prop) {
		throw new RuntimeException("Not implemented yet");
	}

	public String toCode(BooleanProperty prop) {
		Boolean value = (Boolean) prop.get();
		if (value == null) {
			return "false";
		}
		return value.toString();
	}

	public String toCode(ColorProperty prop) {
		Color value = (Color) prop.get();
		if (value == null) {
			return "null";
		}
		return "new java.awt.Color(" + value.getRGB() + ")";
	}

//	public String toCode(FileProperty prop) {
//		File value = (File) prop.get();
//		if (value == null) {
//			return "null";
//		}
//		String path = value.getAbsolutePath();
//		path = path.replace(File.separatorChar, '/');
//		return "new java.io.File(\"" + path + "\")";
//	}

//	public String toCode(DirectoryProperty prop) {
//		File value = (File) prop.get();
//		if (value == null) {
//			return "null";
//		}
//		String path = value.getAbsolutePath();
//		path = path.replace(File.separatorChar, '/');
//		return "new java.io.File(\"" + path + "\")";
//	}

	public String toCode(EnumProperty prop) {
		String value = (String) prop.get();
		if (value == null) {
			return "\"\"";
		}
		return "\"" + value + "\"";
	}

	public String toCode(FontProperty prop) {
		Font value = (Font) prop.get();
		if (value == null) {
			return "\"\"";
		}
		return "new java.awt.Font(\"" + value.getName() + "\", "
				+ value.getStyle() + ", " + value.getSize() + ")";
	}

	public String toCode(IntegerProperty prop) {
		Integer value = (Integer) prop.get();
		if (value == null) {
			return "0";
		}
		return value.toString();
	}

	public String toCode(DoubleProperty prop) {
		Double value = (Double) prop.get();
		if (value == null) {
			return "0.0";
		}
		return value.toString();
	}

	public String toCode(StringProperty prop) {
		String value = (String) prop.get();
		if (value == null) {
			return "\"\"";
		}
		return "\"" + value + "\"";
	}
}

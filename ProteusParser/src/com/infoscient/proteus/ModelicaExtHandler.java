package com.infoscient.proteus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.ModelicaLoader;
import com.infoscient.proteus.modelica.parser.ModelicaParser;

public class ModelicaExtHandler implements FileExtHandler {

	private static final String EXT = "mo";

	public boolean canRead(String ext) {
		return EXT.equals(ext);
	}

	public boolean canWrite(String ext) {
		return EXT.equals(ext);
	}

	public String getExtension() {
		return EXT;
	}

	public String getDescription() {
		return "Modelica File (*.mo)";
	}

	public boolean read(File file, Object... args) throws ParseException,
			IllegalArgumentException, IOException {
		List<ClassDef> classDefList = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof List) {
				if (classDefList == null) {
					classDefList = (List<ClassDef>) args[i];
				}
			}
		}
		if (classDefList == null) {
			throw new IllegalArgumentException(
					"The following arguments are required: List<ClassDef>");
		}
		ModelicaParser parser = new ModelicaParser(new FileInputStream(file));
		ClassDef[] cds;
		try {
			cds = (ClassDef[]) parser.stored_definition().jjtAccept(
					new ModelicaLoader(), null);
			if (cds != null) {
				for (ClassDef cd : cds) {
					cd.sourceFilePath = file.getAbsolutePath();
				}
			}
			classDefList.addAll(Arrays.asList(cds));
			return true;
		} catch (com.infoscient.proteus.modelica.parser.ParseException e) {
			throw new ParseException(e.getMessage());
		}
	}

	public boolean write(File file, Object... args) throws ParseException,
			IllegalArgumentException, IOException {
		List<ClassDef> classDefList = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof List) {
				if (classDefList == null) {
					classDefList = (List<ClassDef>) args[i];
				}
			}
		}
		if (classDefList == null) {
			throw new IllegalArgumentException(
					"The following arguments are required: List<ClassDef>");
		}
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		for (ClassDef cd : classDefList) {
			pw.println(cd.toCode());
		}
		pw.flush();
		return true;
	}

}

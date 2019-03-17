package com.infoscient.proteus;

import java.io.File;
import java.util.Comparator;

import javax.swing.filechooser.FileFilter;

/**
 * Constants used by the application
 * 
 * @author jasleen
 * 
 */
public interface Constants {

	public static String BASE_URL = "http://155.69.254.104/proteus";

	public static String BASE_DIR = "BaseDir";

	public static String LICENSE_DIR = "LicenseDir";

	public static String WORK_DIR = "WorkDir";

	public static String SYSTEM_STORE = "SystemStore";

	public static String PLUGINS_DIR = "PluginsDir";

	public static String LICENSE_INFO_OBJECT = "LicenseInfo",
			OMC_PROXY_OBJECT = "OMCProxy",
			INSTANCE_MGR_OBJECT = "InstanceManager",
			LIBRARY_OBJECT = "Library";

	public static final String OPEN_PREFIX = "open:",
			EXPORT_PREFIX = "export:";

	public static final String MODELICA_LIB_PREFIX = "Modelica";

	public static final String VIEW_MODEL = "Model", VIEW_CODE = "Code",
			VIEW_DOC = "Documentation", VIEW_REF = "References";

	public static final String CATEGORY_PARAMETER = "parameter",
			CATEGORY_MODIFIER = "modifier", CATEGORY_RESULT = "result",
			CATEGORY_GENERAL = "general", CATEGORY_CODE = "code",
			CATEGORY_PRIMITIVE = "primitive", CATEGORY_GEOMETRY = "geometry",
			CATEGORY_OTHER = "other";

	public static final String REAL_PRIMITIVE = "Real",
			INTEGER_PRIMITIVE = "Integer", BOOLEAN_PRIMITIVE = "Boolean",
			STRING_PRIMITIVE = "String";

	// Has to start with lowercase, else some src-code comparisons would fail
	public static final String TYPE_CLASS = "class", TYPE_MODEL = "model",
			TYPE_RECORD = "record", TYPE_BLOCK = "block",
			TYPE_CONNECTOR = "connector", TYPE_TYPE = "type",
			TYPE_PACKAGE = "package", TYPE_FUNCTION = "function",
			TYPE_EXPANDABLECONNECTOR = "expandableconnector",
			TYPE_CONSTANT = "constant";

	public static final int LIVE_TRIAL_DURATION_MINS = 1;

	public static final Comparator<String> ICSTR_CMP = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	};

	public static final Comparator<File> ICFILE_CMP = new Comparator<File>() {
		public int compare(File o1, File o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};

	public static final FileFilter IMG_FILTER = new FileFilter() {
		private String[] EXTS = { ".png", ".jpg", ".jpeg" };

		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			String name = f.getName();
			for (String s : EXTS) {
				if (name.endsWith(s)) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() {
			return "Images";
		}
	};
}

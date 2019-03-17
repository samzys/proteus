/**
 * 
 */
package proteus.gwt.server.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServerContext {

	/**
	 * these parameters are only used when the values in web.xml can not be
	 * read.
	 * 
	 */
	protected static Map<String, String> defaultValues = new HashMap<String, String>();

	static {
		defaultValues.put("ProteusRootPath", "C:/Users/sam/workspace/ProteusGWT2/war");
		defaultValues.put("ProteusFilesPath", "/resources/files");
		defaultValues.put("ExportPath", "/resources/exports");
		defaultValues.put("UploadPath", "/resources/upload");
//		defaultValues.put("MoParserURL", "http://155.69.128.202:8080/proteus");
		defaultValues.put("PwebRoot", "c:");
		defaultValues.put("JsonFilePath",
				"/resources/files/ModelJSON");
	};

	public static boolean isLocal() {
		try {
			return InetAddress.getLocalHost().getHostName()
					.equals("sam-THINK");
		} catch (Exception e) {
		}
		return false;
	}

	public static String get(String key) {
		try {
			String computername = InetAddress.getLocalHost().getHostName();
			if (computername.equals("sam-THINK")) {
				// if(key.equals("ProteusRootPath")){
				// return "D:/workspace/ProteusGWT/war";
				// }else if(key.equals("ProteusFilesPath")) {
				// return "/resources/files";
				// }
			} else if ("Ting-Leis-MacBook-Pro.local".equals(computername)) {
				if (key.equals("ProteusRootPath")) {
					return "/Users/leiting/workspace/ProteusGWT/war";
				} else if (key.equals("ProteusFilesPath")) {
					return "/resources/files";
				}
			}
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}

		String value = "";
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			value = (String) ctx.lookup(key);
		} catch (NamingException e) {
			if (defaultValues.containsKey(key)) {
				value = defaultValues.get(key);
				Logger.getLogger(ServerContext.class.getName()).log(
						Level.WARNING,
						"retrieve " + key + " failed, using default: " + value);
			} else {
				Logger.getLogger(ServerContext.class.getName()).log(
						Level.SEVERE,
						"retrieve " + key + " failed. No such key.");
				return null;
			}
		}

		if (value.endsWith("/")) {
			value = value.substring(0, value.length() - 1);
		}

		return value;
	}

}

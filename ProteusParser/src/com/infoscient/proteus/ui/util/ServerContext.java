/**
 * 
 */
package com.infoscient.proteus.ui.util;

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
		defaultValues.put("ProteusRootPath", "D:/work/Proteus/Proteus/war");

		defaultValues.put("UploadPath", "/resources/upload");
	};

	public static String get(String key) {
		try {
			String computername = InetAddress.getLocalHost().getHostName();
			if (computername.equals("ntu-ea8c90c716d")) {
				if (key.equals("ProteusRootPath")) {
					return "D:/PO-NTU/Workspace/ProteusGWT/war";
				} else if (key.equals("ProteusFilesPath")) {
					return "/resources/files";
				}
			} else if (computername.equals("sam-laptop")) {
				if (key.equals("ProteusRootPath")) {
					return "D:/Workspace/ProteusGWT/war";
				} else if (key.equals("ProteusFilesPath")) {
					return "/resources/files";
				}else if(key.equals("JsonFilePath")) {
					return "D:/workspace/ProteusGWT/war/resources/files/ModelJSON";
				}
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

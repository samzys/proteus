package com.infoscient.proteus;

public class Utils {
	public static String join(Object[] array, String sep) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object o : array) {
			if (i++ > 0) {
				sb.append(sep);
			}
			sb.append(o);
		}
		return sb.toString();
	}

	public static String join(Object obj, int times, String sep) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) {
			if (i++ > 0) {
				sb.append(sep);
			}
			sb.append(obj);
		}
		return sb.toString();
	}

}

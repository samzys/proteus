package com.infoscient.proteus.ui.util;

public class MiscUtils {
	// @sam 3d animation
	public static String parseString(String s) {
		String s1 = s.substring(1, s.length() - 1);
		if (s1 != null)
			return s1;
		else
			return "";
	}
	public static int clamp(int n, int min, int max) {
		return Math.max(min, Math.min(max, n));
	}
}

package proteus.gwt.shared.util;

public class Utils {

	public static final double EPSILON = 0.0001;

	public static boolean DEBUG = true;
	
	public static boolean DEBUGUI = true;

	
	public static boolean DEBUGUI2 = true;
	
	public static boolean DEBUGTIME = true;
	
	public static double parseDouble(String s) {
		double startTime = 0;
		try {
			startTime = Double.parseDouble(s.trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return startTime;
	}

	public static int parseInteger(String stopT) {
		int val = 0;
		try {
			val = Integer.parseInt(stopT);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return val;
	}

	public static double clamp(double val, double min, double max) {
		if (val < min) {
			return min;
		} else if (val > max) {
			return max;
		}
		return val;
	}

	public static int clamp(int val, int min, int max) {
		if (val < min) {
			return min;
		} else if (val > max) {
			return max;
		}
		return val;
	}

	public static boolean isEqual(double n, double cmp) {
		return Math.abs(n - cmp) < EPSILON;
	}

	public static boolean isZero(double n) {
		return Math.abs(n) < EPSILON;
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

	public static void print(Object... objects) {
		int i = 0;
		for (Object object : objects) {
			if (i++ > 0) {
				System.out.print(", ");
			}
			System.out.print(object);
		}
		System.out.println("");
	}

	public static String randomAlphaNum(int length) {
		byte[] buf = new byte[length];
		for (int i = 0; i < buf.length; i++) {
			int n = (int) (Math.random() * 62);
			if (n < 10) {
				buf[i] = (byte) (0x30 + n);
			} else if (n < 36) {
				n -= 10;
				buf[i] = (byte) (0x41 + n);
			} else {
				n -= 36;
				buf[i] = (byte) (0x61 + n);
			}
		}
		return new String(buf);
	}

	public static double toPrecision(double val, int scale) {
		return ((double) Math.round((val * scale))) / scale;
	}

}

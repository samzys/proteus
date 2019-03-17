package com.infoscient.proteus.types;

import java.awt.Color;

/**
 * Class representing a gradient of colors. A new <code>Gradient</code> object
 * can be constructed by specifying a number of colors (and/or alpha values) and
 * their positions on a unit line. The colors in between two key-colors are
 * smoothly interpolated. The current implementation only provides linear
 * interpolation
 * 
 * The interpolated colors are stored in an array called <code>colors</code>.
 * The greater the size of this array, the 'smoother' the gradient (currently it
 * is 64)
 * 
 * @author jasleen
 * 
 */

public class Gradient {

	/**
	 * Name of <i>linear</i> interpolation method, used to linearly transition
	 * from one color into another
	 */
	public static final String INTERPOLATION_LINEAR = "Linear";

	/**
	 * Represents a custom gradient, shown with chromatic colors
	 */
	public static final Gradient CUSTOM = new Gradient("Custom...",
			new Color[] { Color.red, Color.yellow, Color.green, Color.cyan,
					Color.blue });

	/**
	 * Name of this gradient
	 */
	public String name;

	/**
	 * Array of 'key' colors used to create the gradient. The key colors are
	 * specified on a unit number line, with the first color at 0.0 and the last
	 * one at 1.0. Colors in between two such key-colors are generated using the
	 * linear interpolation method
	 */
	public Color[] keyColors;

	/**
	 * Position of key colors on the unit line. The size of this array must be
	 * the same as <code>keyColors</code>. If this array is null, then the
	 * colors are placed uniformly along the unit line
	 */
	public double[] ratioColors;

	/**
	 * Array of 'key' alpha values used to create the gradient. The alpha value
	 * controls transparency of a pixel, 0.0f being completely transparent and
	 * 1.0f being completely opaque. The key alpha values are specified on a
	 * unit number line, with the first value at 0.0 and the last one at 1.0.
	 * Values in between two such key-alphas are generated using the linear
	 * interpolation method
	 */
	public float[] keyAlphas;

	/**
	 * Position of key alpha values on the unit line. The size of this array
	 * must be the same as <code>keyAlphas</code>. If this array is null,
	 * then the alpha values are placed uniformly along the unit line
	 */
	public double[] ratioAlphas;

	/**
	 * Gradient colors generated
	 */
	public Color[] colors;

	/**
	 * Collection of various 'gradient' types. The following types are
	 * available:
	 * <ul>
	 * <li>Chromatic</li>
	 * <li>Inverse Chromatic</li>
	 * <li>Hot</li>
	 * <li>Cool</li>
	 * <li>Spring</li>
	 * <li>Summer</li>
	 * <li>Autumn</li>
	 * <li>Grayscale</li>
	 * </ul>
	 */
	public static final Gradient[] GRADIENTS = {
			new Gradient("Chromatic", new Color[] { Color.red, Color.yellow,
					Color.green, Color.cyan, Color.blue }),
			new Gradient("Inverse", new Color[] { Color.blue, Color.cyan,
					Color.green, Color.yellow, Color.red }),
			new Gradient("Hot", new Color[] { Color.black, Color.red,
					Color.yellow }),
			new Gradient("Cool", new Color[] { Color.cyan, Color.magenta }),
			new Gradient("Spring", new Color[] { Color.magenta, Color.yellow }),
			new Gradient("Summer", new Color[] { Color.green, Color.yellow }),
			new Gradient("Autumn", new Color[] { Color.red, Color.yellow }),
			new Gradient("Grayscale", new Color[] { Color.black, Color.white }) };

	/**
	 * Constructor for a gradient with key colors assumed to be placed uniformly
	 * on the unit line. All colors are fully opaque
	 * 
	 * @param name
	 *            Name of this gradient
	 * @param keyColors
	 *            Array of 'key' colors used to create the gradient
	 */
	public Gradient(String name, Color[] keyColors) {
		this(name, keyColors, null);
	}

	/**
	 * Constructor for a gradient with key colors located on specific points on
	 * the unit line. All colors are fully opaque
	 * 
	 * @param name
	 *            Name of this gradient
	 * @param keyColors
	 *            Array of 'key' colors used to create the gradient
	 * @param ratioColors
	 *            Position of key colors on the unit line
	 */
	public Gradient(String name, Color[] keyColors, double[] ratioColors) {
		this(name, keyColors, ratioColors, new float[] { 1.0f, 1.0f }, null);
	}

	/**
	 * Constructor for a gradient with key colors and alpha values located on
	 * specific points on the unit line
	 * 
	 * @param name
	 *            Name of this gradient
	 * @param keyColors
	 *            Array of 'key' colors used to create the gradient
	 * @param ratioColors
	 *            Position of key colors on the unit line
	 * @param keyAlphas
	 *            Array of 'key' alpha values used to create the gradient
	 * @param ratioAlphas
	 *            Position of key alphas on the unit line
	 */
	public Gradient(String name, Color[] keyColors, double[] ratioColors,
			float[] keyAlphas, double[] ratioAlphas) {
		this.name = name;
		this.keyColors = keyColors;
		this.ratioColors = ratioColors;
		this.keyAlphas = keyAlphas;
		this.ratioAlphas = ratioAlphas;
		this.colors = interpolate(64, keyColors, ratioColors, keyAlphas,
				ratioAlphas);
	}

	/**
	 * Generates an array of intermediate colors based on the given key colors
	 * assumed to be placed uniformly on the unit line. All colors are fully
	 * opaque
	 * 
	 * @param size
	 *            Size of the generated array of <code>Color</code>s
	 * @param keyColors
	 *            Array of 'key' colors used to create the gradient
	 * @return An array of <code>Color</code>s forming the gradient of
	 *         smoothly interpolated colors
	 */
	public static Color[] interpolate(int size, Color[] keyColors) {
		return interpolate(size, keyColors, null);
	}

	/**
	 * Generates an array of intermediate colors based on the given key colors
	 * placed uniformly on the unit line. All colors are fully opaque
	 * 
	 * @param size
	 *            Size of the generated array of <code>Color</code>s
	 * @param keyColors
	 *            Array of 'key' colors used to create the gradient
	 * @param ratioColors
	 *            Position of key colors on the unit line
	 * @return An array of <code>Color</code>s forming the gradient of
	 *         smoothly interpolated colors
	 */
	public static Color[] interpolate(int size, Color[] keyColors,
			double[] ratioColors) {
		return interpolate(size, keyColors, ratioColors, new float[] { 1.0f,
				1.0f }, null);
	}

	/**
	 * Generates an array of intermediate colors based on the given key colors
	 * placed uniformly on the unit line. All colors are fully opaque
	 * 
	 * @param size
	 *            Size of the generated array of <code>Color</code>s
	 * @param keyColors
	 *            Array of 'key' colors used to create the gradient
	 * @param ratioColors
	 *            Position of key colors on the unit line
	 * @param keyAlphas
	 *            Array of 'key' alpha values used to create the gradient
	 * @param ratioAlphas
	 *            Position of key alphas on the unit line
	 * @return An array of <code>Color</code>s forming the gradient of
	 *         smoothly interpolated colors
	 */
	public static Color[] interpolate(int size, Color[] keyColors,
			double[] ratioColors, float[] keyAlphas, double[] ratioAlphas) {
		if (keyColors.length < 2) {
			throw new IllegalArgumentException("keyColors.length < 2");
		}
		if (keyAlphas != null && keyAlphas.length < 2) {
			throw new IllegalArgumentException("keyAlphas.length < 2");
		}
		boolean useColorRatios = ratioColors != null, useAlphaRatios = ratioAlphas != null;
		if (useColorRatios && ratioColors.length != keyColors.length - 2) {
			throw new IllegalArgumentException(
					"ratioColors.length != keyColors.length - 2");
		}
		if (useAlphaRatios && ratioAlphas.length != keyAlphas.length - 2) {
			throw new IllegalArgumentException(
					"ratioAlphas.length != keyAlphas.length - 2");
		}
		Color[] gradient = new Color[size];
		int index = 0;
		int i = 0, alphaIndex = 0;
		int a1 = (int) (0xFF * keyAlphas[alphaIndex]);
		int a2 = (int) (0xFF * keyAlphas[alphaIndex + 1]);
		int cp1 = 0, cp2;
		int ap1 = 0, ap2 = useAlphaRatios && alphaIndex < ratioAlphas.length ? (int) (gradient.length * ratioAlphas[alphaIndex])
				: gradient.length;
		int ar = ap2 - ap1;
		for (; i < keyColors.length - 1; i++) {
			Color c1 = keyColors[i];
			Color c2 = keyColors[i + 1];
			cp2 = useColorRatios && i < ratioColors.length ? (int) (gradient.length * ratioColors[i])
					: (i + 1) * gradient.length / (keyColors.length - 1);
			int cr = cp2 - cp1;
			for (int j = i; j < (cr + i) && index < gradient.length; j++) {
				if (useAlphaRatios && index >= ap2) {
					alphaIndex++;
					a1 = (int) (0xFF * keyAlphas[alphaIndex]);
					a2 = (int) (0xFF * keyAlphas[alphaIndex + 1]);
					ap1 = ap2;
					ap2 = alphaIndex >= ratioAlphas.length ? gradient.length
							: (int) (gradient.length * ratioAlphas[alphaIndex]);
					ar = ap2 - ap1;
				}
				int r1 = c1.getRed(), g1 = c1.getGreen(), b1 = c1.getBlue();
				int r2 = c2.getRed(), g2 = c2.getGreen(), b2 = c2.getBlue();
				int red = Math.max(0, Math.min(
						r1 * (cr - j) / cr + r2 * j / cr, 0xFF));
				int green = Math.max(0, Math.min(g1 * (cr - j) / cr + g2 * j
						/ cr, 0xFF));
				int blue = Math.max(0, Math.min(b1 * (cr - j) / cr + b2 * j
						/ cr, 0xFF));
				int alpha = Math.max(0, Math.min(a1
						* (ap2 - index + alphaIndex) / ar + a2
						* (index - ap1 + alphaIndex) / ar, 0xFF));
				gradient[index++] = new Color(red, green, blue, alpha);
			}
			cp1 = cp2;
		}
		return gradient;
	}

	/**
	 * Two gradient objects are considered equal if they have the same name
	 */
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Gradient)) {
			return false;
		}
		return name.equals(((Gradient) obj).name);
	}

	/**
	 * Copy the colors of this gradient into another
	 * 
	 * @param g
	 *            Gradient whose values will be over-written
	 */
	public void copyInto(Gradient g) {
		g.keyColors = keyColors.clone();
		g.ratioColors = ratioColors == null ? ratioColors : ratioColors.clone();
		g.keyAlphas = keyAlphas.clone();
		g.ratioAlphas = ratioAlphas == null ? ratioAlphas : ratioAlphas.clone();
		g.colors = colors.clone();
	}

	public static void main(String[] args) {
		Color[] colors = interpolate(16, new Color[] { Color.red, Color.yellow,
				Color.green });
		int i = 1;
		for (Color c : colors) {
			System.out.println("new Color(" + c.getRed() + ", " + c.getGreen()
					+ ", " + c.getBlue() + "), ");
		}
	}
}

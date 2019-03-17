package proteus.gwt.shared.graphics;



/**
 * @author Lei Ting
 *
 */
public class Color extends proteus.gwt.shared.util.Color{
	/**
     * The color white.  In the default sRGB space.
     */
    public final static Color white     = new Color(255, 255, 255);
 
    /**
     * The color white.  In the default sRGB space.
     */
    public final static Color WHITE = white;

    /**
     * The color light gray.  In the default sRGB space.
     */
    public final static Color lightGray = new Color(192, 192, 192);

    /**
     * The color light gray.  In the default sRGB space.
     */
    public final static Color LIGHT_GRAY = lightGray;

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static Color gray      = new Color(128, 128, 128);

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static Color GRAY = gray;

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public final static Color darkGray  = new Color(64, 64, 64);

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public final static Color DARK_GRAY = darkGray;

    /**
     * The color black.  In the default sRGB space.
     */
    public final static Color black 	= new Color(0, 0, 0);
    
    /**
     * The color black.  In the default sRGB space.
     */
    public final static Color BLACK = black;
    
    /**
     * The color red.  In the default sRGB space.
     */
    public final static Color red       = new Color(255, 0, 0);

    /**
     * The color red.  In the default sRGB space.
     */
    public final static Color RED = red;

    /**
     * The color pink.  In the default sRGB space.
     */
    public final static Color pink      = new Color(255, 175, 175);

    /**
     * The color pink.  In the default sRGB space.
     */
    public final static Color PINK = pink;

    /**
     * The color orange.  In the default sRGB space.
     */
    public final static Color orange 	= new Color(255, 200, 0);

    /**
     * The color orange.  In the default sRGB space.
     */
    public final static Color ORANGE = orange;

    /**
     * The color yellow.  In the default sRGB space.
     */
    public final static Color yellow 	= new Color(255, 255, 0);

    /**
     * The color yellow.  In the default sRGB space.
     */
    public final static Color YELLOW = yellow;

    /**
     * The color green.  In the default sRGB space.
     */
    public final static Color green 	= new Color(0, 255, 0);

    /**
     * The color green.  In the default sRGB space.
     */
    public final static Color GREEN = green;

    /**
     * The color magenta.  In the default sRGB space.
     */
    public final static Color magenta	= new Color(255, 0, 255);

    /**
     * The color magenta.  In the default sRGB space.
     */
    public final static Color MAGENTA = magenta;

    /**
     * The color cyan.  In the default sRGB space.
     */
    public final static Color cyan 	= new Color(0, 255, 255);

    /**
     * The color cyan.  In the default sRGB space.
     */
    public final static Color CYAN = cyan;

    /**
     * The color blue.  In the default sRGB space.
     */
    public final static Color blue 	= new Color(0, 0, 255);

    /**
     * The color blue.  In the default sRGB space.
     */
    public final static Color BLUE = blue;
	  
	  private int value;
	  
	  public Color() {
		  this(0, 0, 0);
	  }
	  
	  public Color(Color color) {
		  this(color.getRed(), color.getGreen(), 
				  color.getBlue(), (float)color.getAlpha());
	  }
	  
	  /**
	   * Create a new Color object with the specified RGBA
	   * values.
	   * 
	   * @param r red value 0-255
	   * @param g green value 0-255
	   * @param b blue value 0-255
	   * @param a alpha value
	   */
	  public Color(int r, int g, int b, int a) {
		  super(r, g, b,(((float)a))/255);
		  
		  value = ((a & 0xFF) << 24) |
          ((r & 0xFF) << 16) |
          ((g & 0xFF) << 8)  |
          ((b & 0xFF) << 0);
	  }
	  
	  /**
	   * Create a new Color object with the specified RGB 
	   * values.
	   * 
	   * @param r red value 0-255
	   * @param g green value 0-255
	   * @param b blue value 0-255
	   */
	  public Color(int r, int g, int b) {
		  super(r, g, b);
		  //super(r, g, b, (float)1.0);

		  value = (0xFF << 24) |
		  ((r & 0xFF) << 16) |
		  ((g & 0xFF) << 8)  |
		  ((b & 0xFF) << 0);
	  }
	  
	  /**
	   * Create a new Color object with the specified RGBA 
	   * values.
	   * 
	   * @param r red value 0-255
	   * @param g green value 0-255
	   * @param b blue value 0-255
	   * @param a alpha channel value 0-1
	   */
	  public Color(int r, int g, int b, float a) {
		this(r,g,b,(int)(a*255));
	  }
	  
	  /**
	     * Creates an opaque sRGB color with the specified combined RGB value
	     * consisting of the red component in bits 16-23, the green component
	     * in bits 8-15, and the blue component in bits 0-7.  The actual color
	     * used in rendering depends on finding the best match given the
	     * color space available for a particular output device.  Alpha is
	     * defaulted to 255.
	     *
	     * @param rgb the combined RGB components
	     * @see java.awt.image.ColorModel#getRGBdefault
	     * @see #getRed
	     * @see #getGreen
	     * @see #getBlue
	     * @see #getRGB
	     */
	    public Color(int rgb) {
	    	this((0x00ff0000&rgb)>>16, (0x0000ff00&rgb)>>8, (0x000000ff&rgb));
	    }
	    
	    /**
	     * Creates an sRGB color with the specified combined RGBA value consisting
	     * of the alpha component in bits 24-31, the red component in bits 16-23,
	     * the green component in bits 8-15, and the blue component in bits 0-7.
	     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
	     * is defaulted to 255.
	     *
	     * @param rgba the combined RGBA components
	     * @param hasalpha <code>true</code> if the alpha bits are valid;
	     *        <code>false</code> otherwise
	     * @see java.awt.image.ColorModel#getRGBdefault
	     * @see #getRed
	     * @see #getGreen
	     * @see #getBlue
	     * @see #getAlpha
	     * @see #getRGB
	     */
	    public Color(int rgba, boolean hasalpha) {
	    	this(rgba);
	        if (hasalpha) {
	            value = rgba;
	        } else {
	            value = 0xff000000 | rgba;
	        }
	    }
	    
	  /**
	     * Returns the red component in the range 0-255 in the default sRGB
	     * space.
	     * @return the red component.
	     * @see #getRGB
	     */
	    public int getRed() {
		return (getRGB() >> 16) & 0xFF;
	    }

	    /**
	     * Returns the green component in the range 0-255 in the default sRGB
	     * space.
	     * @return the green component.
	     * @see #getRGB
	     */
	    public int getGreen() {
		return (getRGB() >> 8) & 0xFF;
	    }

	    /**
	     * Returns the blue component in the range 0-255 in the default sRGB
	     * space.
	     * @return the blue component.
	     * @see #getRGB
	     */
	    public int getBlue() {
		return (getRGB() >> 0) & 0xFF;
	    }

	    /**
	     * Returns the alpha component in the range 0-255.
	     * @return the alpha component.
	     * @see #getRGB
	     */
	    public double getAlpha() {
	        return (getRGB() >> 24) & 0xff;
	    }

	    /**
	     * Returns the RGB value representing the color in the default sRGB
	     * {@link ColorModel}.
	     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
	     * blue).
	     * @return the RGB value of the color in the default sRGB
	     *         <code>ColorModel</code>.
	     * @see java.awt.image.ColorModel#getRGBdefault
	     * @see #getRed
	     * @see #getGreen
	     * @see #getBlue
	     * @since JDK1.0
	     */
	    public int getRGB() {
	    	return value;
	    }

	    private static final double FACTOR = 0.7;
	    
	    /**
	     * Creates a new <code>Color</code> that is a brighter version of this
	     * <code>Color</code>.
	     * <p>
	     * This method applies an arbitrary scale factor to each of the three RGB 
	     * components of this <code>Color</code> to create a brighter version
	     * of this <code>Color</code>. Although <code>brighter</code> and
	     * <code>darker</code> are inverse operations, the results of a
	     * series of invocations of these two methods might be inconsistent
	     * because of rounding errors. 
	     * @return     a new <code>Color</code> object that is  
	     *                 a brighter version of this <code>Color</code>.
	     * @see        java.awt.Color#darker
	     * @since      JDK1.0
	     */
	    public Color brighter() {
	        int r = getRed();
	        int g = getGreen();
	        int b = getBlue();

	        /* From 2D group:
	         * 1. black.brighter() should return grey
	         * 2. applying brighter to blue will always return blue, brighter
	         * 3. non pure color (non zero rgb) will eventually return white
	         */
	        int i = (int)(1.0/(1.0-FACTOR));
	        if ( r == 0 && g == 0 && b == 0) {
	           return new Color(i, i, i);
	        }
	        if ( r > 0 && r < i ) r = i;
	        if ( g > 0 && g < i ) g = i;
	        if ( b > 0 && b < i ) b = i;

	        return new Color(Math.min((int)(r/FACTOR), 255),
	                         Math.min((int)(g/FACTOR), 255),
	                         Math.min((int)(b/FACTOR), 255));
	    }

	    /**
	     * Creates a new <code>Color</code> that is a darker version of this
	     * <code>Color</code>.
	     * <p>
	     * This method applies an arbitrary scale factor to each of the three RGB 
	     * components of this <code>Color</code> to create a darker version of
	     * this <code>Color</code>.  Although <code>brighter</code> and
	     * <code>darker</code> are inverse operations, the results of a series 
	     * of invocations of these two methods might be inconsistent because
	     * of rounding errors. 
	     * @return  a new <code>Color</code> object that is 
	     *                    a darker version of this <code>Color</code>.
	     * @see        java.awt.Color#brighter
	     * @since      JDK1.0
	     */
	    public Color darker() {
		return new Color(Math.max((int)(getRed()  *FACTOR), 0), 
				 Math.max((int)(getGreen()*FACTOR), 0),
				 Math.max((int)(getBlue() *FACTOR), 0));
	    }
	    
		public boolean equals(Color color) {
			return getRed() == color.getRed() && 
				getGreen() == color.getGreen() && 
				getBlue() == color.getBlue() &&
				getAlpha() == color.getAlpha();
		}
}

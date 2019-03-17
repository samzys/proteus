/**
 * 
 */
package proteus.gwt.shared.graphics;

/**
 * @author Lei Ting
 *
 */
public class TextAttribute {
	public static final TextAttribute FAMILY = new TextAttribute();
	public static final TextAttribute SIZE = new TextAttribute();
	public static final TextAttribute WEIGHT = new TextAttribute();
	public static final TextAttribute UNDERLINE = new TextAttribute();
	public static final TextAttribute POSTURE = new TextAttribute();
	public static final TextAttribute FOREGROUND = new TextAttribute();
	public static final TextAttribute BACKGROUND = new TextAttribute();
	
	public static final Float WEIGHT_BOLD = 2.0f;
	public static final Float WEIGHT_REGULAR = 1.0f;
	
	public static final Integer UNDERLINE_ON = 1;
	
	public static final Float POSTURE_OBLIQUE = 2.0f;
	public static final Float POSTURE_REGULAR = 1.0f;
	
	private TextAttribute(){};
}
